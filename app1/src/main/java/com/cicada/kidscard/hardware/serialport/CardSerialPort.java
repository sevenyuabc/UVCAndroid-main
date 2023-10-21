package com.cicada.kidscard.hardware.serialport;

import android.text.TextUtils;

import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.utils.LogUtils;
import com.wits.serialport.SerialPort;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 读卡
 */
public class CardSerialPort {
    private static CardSerialPort instance = null;
    private SerialPort cardSerialPort;

    private InputStream mGateInputStream;

    private GateSerialPortThreadData mGateSerialPortThreadData;
    private ReadSerialDataCallBack readSerialDataCallBack;

    public static CardSerialPort getInstance() {
        if (instance == null) {
            synchronized (CardSerialPort.class) {
                instance = new CardSerialPort();
            }
        }
        return instance;
    }

    public void setReadSerialDataCallBack(ReadSerialDataCallBack readSerialDataCallBack) {
        this.readSerialDataCallBack = readSerialDataCallBack;
    }

    /**
     *
     */
    public void openSerialPort() {
        try {
            //设置串口信息
            if (AppContext.isIs32Device()) {
                cardSerialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
            } else if (AppContext.isIsYMDevice()) {
                cardSerialPort = new SerialPort(new File("/dev/ttyS3"), 9600, 0);
            }
            LogUtils.d("==yy==", "串口初始化操作成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mGateSerialPortThreadData && mGateSerialPortThreadData.isAlive()) {
            return;
        }
        mGateSerialPortThreadData = new GateSerialPortThreadData();
        mGateSerialPortThreadData.start();
    }


    public void destroyCardSerialPort() {
        LogUtils.d("==yy==***************", "读取卡头被销毁");
        if (mGateSerialPortThreadData != null) {
            mGateSerialPortThreadData.interrupt();
        }
        cardSerialPort = null;
    }


    private class GateSerialPortThreadData extends Thread {
        @Override
        public void run() {
            super.run();
            if (null == cardSerialPort) {
                return;
            }
            mGateInputStream = cardSerialPort.getInputStream();
            byte[] buffer = new byte[200];
            while (true) {
                try {
                    if (mGateInputStream != null) {
                        int gateSize = mGateInputStream.read(buffer);
                        if (gateSize > 0) {
                            String cardNo = getCardNo(buffer);
                            if (null != readSerialDataCallBack && !TextUtils.isEmpty(cardNo)) {
                                readSerialDataCallBack.onReadSerialData(cardNo);
                            }
                        }
                    } else {
                        LogUtils.d("==yy==sp***************", "没有读取卡号输入流");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    buffer = null;
                    LogUtils.d("==yy==sp***************", "读取卡号异常");
                }
            }
        }
    }

    /**
     * 获取卡号
     *
     * @param byteCardData
     * @return
     */
    public String getCardNo(byte[] byteCardData) {
        String strCardNumber = "";
        try {
            if (byteCardData == null || byteCardData.length < 8) {
                return "";
            }
            if (isNewCardReader(Arrays.toString(byteCardData))) {//新刷卡板
                String str16 = new String(byteCardData);
                str16 = str16.trim();
                if (str16.length() > 0) {
                    if (str16.length() >= 8) {
                        str16 = str16.substring(0, 8);
                    }
                    strCardNumber = String.format("%010d", Long.parseLong(str16, 16));
                }
            } else {//老刷卡板
                StringBuffer sb = new StringBuffer();
                // 检查是不是知了卡
                StringBuilder str = new StringBuilder();
                for (int i = byteCardData.length - 1; i > 0; i--) {
                    int n = byteCardData[i] & 0xFF;
                    String hn = Integer.toHexString(n);
                    if (hn.length() < 2) {
                        sb.append(0);
                    }
                    str.append(hn);
                }

                for (int i = 7; i >= 4; i--) {
                    int v = byteCardData[i] & 0xFF;
                    String hv = Integer.toHexString(v);
                    if (hv.length() < 2) {
                        sb.append(0);
                    }
                    sb.append(hv);
                }
                strCardNumber = String.format("%010d", Long.parseLong(sb.toString(), 16));
            }
            LogUtils.d("====yy==sp==", "cardnumber =" + strCardNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCardNumber;
    }

    /**
     * 是否是新版读卡器
     * 新版读卡器数据格式：（开始字符：02 结束字符：0D 0A 03）
     * STX(0X02)  DATA(8HEX) CR LF ETX(0X03)
     * STX 为开始字符 02(十六进制) DATA 为 8 位十六进制字符卡号
     * CR 为 0D(表示空格)
     * LF 为 0A(表示换行)
     * ETX 为结束字符 03(十六进制)
     *
     * @param byteCardDataArrStr
     * @return
     */
    public boolean isNewCardReader(String byteCardDataArrStr) {
        if (byteCardDataArrStr.indexOf("13, 10, 3,") > 0) {
            return true;
        }
        return false;
    }

    public interface ReadSerialDataCallBack {
        void onReadSerialData(String data);
    }

}
