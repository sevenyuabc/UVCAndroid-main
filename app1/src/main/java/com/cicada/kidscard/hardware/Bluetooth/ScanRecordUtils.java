package com.cicada.kidscard.hardware.Bluetooth;

import android.os.ParcelUuid;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * TODO
 * <p>
 * Create time: 2020-02-24 10:20
 *
 * @author liuyun.
 */
public class ScanRecordUtils {
    private static final String TAG = "ScanRecordUtils";

    // The following data type values are assigned by Bluetooth SIG.
// For more details refer to Bluetooth 4.1 specification, Volume 3, Part C, Section 18.
    private static final int DATA_TYPE_FLAGS = 0x01;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 0x02;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 0x03;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 0x04;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 0x05;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 0x06;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 0x07;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 0x08;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 0x09;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 0x0A;
    private static final int DATA_TYPE_SERVICE_DATA_16_BIT = 0x16;
    private static final int DATA_TYPE_SERVICE_DATA_32_BIT = 0x20;
    private static final int DATA_TYPE_SERVICE_DATA_128_BIT = 0x21;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xFF;

    public static final ParcelUuid BASE_UUID =
            ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");
    /**
     * Length of bytes for 16 bit UUID
     */
    public static final int UUID_BYTES_16_BIT = 2;
    /*
    Length of bytes for 32 bit UUID
    */
    public static final int UUID_BYTES_32_BIT = 4;
    /*
    Length of bytes for 128 bit UUID
    */
    public static final int UUID_BYTES_128_BIT = 16;
    // Flags of the advertising data.
    private final int mAdvertiseFlags;

    private final List mServiceUuids;

    private final SparseArray<byte[]> mManufacturerSpecificData;

    private final Map<ParcelUuid, byte[]> mServiceData;

    // Transmission power level(in dB).
    private final int mTxPowerLevel;

    // Local name of the Bluetooth LE device.
    private final String mDeviceName;

    // Raw bytes of scan record.
    private final byte[] mBytes;

    /**
     * Parse scan record bytes
     */
    public static ScanRecordUtils parseFromBytes(byte[] scanRecord) {
        if (scanRecord == null) {
            return null;
        }

        int currentPos = 0;
        int advertiseFlag = -1;
        List serviceUuids = new ArrayList();
        String localName = null;
        int txPowerLevel = Integer.MIN_VALUE;

        SparseArray<byte[]> manufacturerData = new SparseArray<byte[]>();
        Map<ParcelUuid, byte[]> serviceData = new ArrayMap<ParcelUuid, byte[]>();

        try {
            while (currentPos < scanRecord.length) {
// length is unsigned int.
                int length = scanRecord[currentPos++] & 0xFF;
                if (length == 0) {
                    break;
                }
// Note the length includes the length of the field type itself.
                int dataLength = length - 1;
// fieldType is unsigned int.
                int fieldType = scanRecord[currentPos++] & 0xFF;
                switch (fieldType) {
                    case DATA_TYPE_FLAGS:
                        advertiseFlag = scanRecord[currentPos] & 0xFF;
                        break;
                    case DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL:
                    case DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE:
                        parseServiceUuid(scanRecord, currentPos,
                                dataLength, UUID_BYTES_16_BIT, serviceUuids);
                        break;
                    case DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL:
                    case DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE:
                        parseServiceUuid(scanRecord, currentPos, dataLength,
                                UUID_BYTES_32_BIT, serviceUuids);
                        break;
                    case DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL:
                    case DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE:
                        parseServiceUuid(scanRecord, currentPos, dataLength,
                                UUID_BYTES_128_BIT, serviceUuids);
                        break;
                    case DATA_TYPE_LOCAL_NAME_SHORT:
                    case DATA_TYPE_LOCAL_NAME_COMPLETE:
                        localName = new String(
                                extractBytes(scanRecord, currentPos, dataLength));
                        break;
                    case DATA_TYPE_TX_POWER_LEVEL:
                        txPowerLevel = scanRecord[currentPos];
                        break;
                    case DATA_TYPE_SERVICE_DATA_16_BIT:
                    case DATA_TYPE_SERVICE_DATA_32_BIT:
                    case DATA_TYPE_SERVICE_DATA_128_BIT:
                        int serviceUuidLength = UUID_BYTES_16_BIT;
                        if (fieldType == DATA_TYPE_SERVICE_DATA_32_BIT) {
                            serviceUuidLength = UUID_BYTES_32_BIT;
                        } else if (fieldType == DATA_TYPE_SERVICE_DATA_128_BIT) {
                            serviceUuidLength = UUID_BYTES_128_BIT;
                        }

                        byte[] serviceDataUuidBytes = extractBytes(scanRecord, currentPos,
                                serviceUuidLength);
                        ParcelUuid serviceDataUuid = parseUuidFrom(
                                serviceDataUuidBytes);
                        byte[] serviceDataArray = extractBytes(scanRecord,
                                currentPos + serviceUuidLength, dataLength - serviceUuidLength);
                        serviceData.put(serviceDataUuid, serviceDataArray);
                        break;
                    case DATA_TYPE_MANUFACTURER_SPECIFIC_DATA:
                        // The first two bytes of the manufacturer specific data are
                        // manufacturer ids in little endian.
                        int manufacturerId = ((scanRecord[currentPos + 1] & 0xFF) << 8) +
                                (scanRecord[currentPos] & 0xFF);
                        byte[] manufacturerDataBytes = extractBytes(scanRecord, currentPos + 2,
                                dataLength - 2);
                        manufacturerData.put(manufacturerId, manufacturerDataBytes);
                        break;
                    default:
                        // Just ignore, we don't handle such data type.
                        break;
                }
                currentPos += dataLength;
            }

            if (serviceUuids.isEmpty()) {
                serviceUuids = null;
            }
            return new ScanRecordUtils(serviceUuids, manufacturerData, serviceData,
                    advertiseFlag, txPowerLevel, localName, scanRecord);
        } catch (Exception e) {
            Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
// As the record is invalid, ignore all the parsed results for this packet
// and return an empty record with raw scanRecord bytes in results
            return new ScanRecordUtils(null, null, null, -1, Integer.MIN_VALUE, null, scanRecord);
        }
    }

    private ScanRecordUtils(List serviceUuids,
                            SparseArray<byte[]> manufacturerData,
                            Map<ParcelUuid, byte[]> serviceData,
                            int advertiseFlags, int txPowerLevel,
                            String localName, byte[] bytes) {
        mServiceUuids = serviceUuids;
        mManufacturerSpecificData = manufacturerData;
        mServiceData = serviceData;
        mDeviceName = localName;
        mAdvertiseFlags = advertiseFlags;
        mTxPowerLevel = txPowerLevel;
        mBytes = bytes;
    }

    // Parse service UUIDs.
    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength,
                                        int uuidLength, List serviceUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = extractBytes(scanRecord, currentPos,
                    uuidLength);
            serviceUuids.add(parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    // Helper method to extract bytes from byte array.
    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }

    /**
     * Parse UUID from bytes. The {@code uuidBytes} can represent a 16-bit, 32-bit or 128-bit UUID,
     * but the returned UUID is always in 128-bit format.
     * Note UUID is little endian in Bluetooth.
     *
     * @param uuidBytes Byte representation of uuid.
     * @return {@link ParcelUuid} parsed from bytes.
     * @throws IllegalArgumentException If the {@code uuidBytes} cannot be parsed.
     */
    private static ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length != UUID_BYTES_16_BIT && length != UUID_BYTES_32_BIT
                && length != UUID_BYTES_128_BIT) {
            throw new IllegalArgumentException("uuidBytes length invalid - " + length);
        }
// Construct a 128 bit UUID.
        if (length == UUID_BYTES_128_BIT) {
            ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
            long msb = buf.getLong(8);
            long lsb = buf.getLong(0);
            return new ParcelUuid(new UUID(msb, lsb));
        }
// For 16 bit and 32 bit UUID we need to convert them to 128 bit value.
// 128_bit_value = uuid * 2^96 + BASE_UUID
        long shortUuid;
        if (length == UUID_BYTES_16_BIT) {
            shortUuid = uuidBytes[0] & 0xFF;
            shortUuid += (uuidBytes[1] & 0xFF) << 8;
        } else {
            shortUuid = uuidBytes[0] & 0xFF;
            shortUuid += (uuidBytes[1] & 0xFF) << 8;
            shortUuid += (uuidBytes[2] & 0xFF) << 16;
            shortUuid += (uuidBytes[3] & 0xFF) << 24;
        }
        long msb = BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32);
        long lsb = BASE_UUID.getUuid().getLeastSignificantBits();
        return new ParcelUuid(new UUID(msb, lsb));
    }

    /**
     * Returns a list of service UUIDs within the advertisement that are used to identify the
     * bluetooth GATT services.
     */
    public List getServiceUuids() {
        return mServiceUuids;
    }
}
