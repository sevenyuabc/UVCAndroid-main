package com.cicada.kidscard.utils;

import java.text.DecimalFormat;

/**
 * 体温解析
 * <p>
 * Create time: 2020-03-04 20:04
 *
 * @author liuyun.
 */
public class ParseTemperature {
    final static String TAG = "==yy== temperature";
    static DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足1位,会以0补足

    /**
     * 解析BF4030设备温度
     *
     * @param bytes
     * @return
     */
    public static String getTemperature(byte[] bytes) {
        String hexString = ByteUtils.bytesToHexString(bytes, bytes.length);
        String temperature = ByteUtils.HexStringToTemperature(hexString);
        return temperature;
    }

    /**
     * 解析Comper设备温度
     * 头码为0xFE,0xFD;尾码为0x0D,0x0A
     * Byte9、Byte10为体温数据
     *
     * @param bytes
     * @return
     */
    public static String getComperTemperature(byte[] bytes) {
        String hexString = ByteUtils.bytesToHexString(bytes, bytes.length);
        String temperature = "";
        if (hexString.length() >= 26) {
            temperature = hexString.substring(16, 20);
            temperature = String.valueOf(Long.parseLong(temperature, 16) / 100.0);
        }
        return temperature;
    }


    /**
     * 解析红外测温设备温度
     * <p>
     * 第四五位为人体温度数据
     *
     * @param buffer
     * @return
     */
    public static float getInfraredTemperature(byte[] buffer) {
        byte[] temp = new byte[1];
        float temperature = 0.0f;

        //接收数据:返回长度为1548,第四五位为人体温度数据
        temp[0] = buffer[4];
        int temperature1 = Integer.parseInt(ByteUtils.Bytes2HexString(temp), 16);
        temp[0] = buffer[5];
        int temperature2 = Integer.parseInt(ByteUtils.Bytes2HexString(temp), 16);
        temperature = (temperature1 + 256 * temperature2) / 100.0f;

        //体温 在30 到 35之间，做补偿，小数点后数据保持不变，整数部分设为35
        if (temperature < 35.0 && temperature >= 30.0) {
            int redress = 35 - (int) temperature;
            temperature = temperature + redress;
            LogUtils.d("==yy== infrared", "补偿后的温度：" + temperature);
        }

        /**
         * 高温、低温测试场景模拟：
         *
         * 设置-算法阈值：
         *   低：红外温度-7
         *   较低：红外温度 +7
         */
//        if (temperature > 0.0) {
//            if (Constants.SECURITYLEVEL0 == AppSharedPreferences.getInstance().getKidsCardSecrityLevel()) {
//                temperature -= 7;
//            } else if (Constants.SECURITYLEVEL1 == AppSharedPreferences.getInstance().getKidsCardSecrityLevel()) {
//                temperature += 7;
//            }
//        }

        temperature = Float.parseFloat(decimalFormat.format(temperature));
        return temperature;
    }

    /**
     * 华安（佳讯）体温枪温度
     *
     * @param data
     * @return
     */
    static public float getHuaAnTemperature(byte[] data) {
        if (data == null || data.length != 16
                || (data[0] & 0xff) != 0xcc || (data[1] & 0xff) != 0xbb
                || data[2] != 0x11 || data[3] != 0x02) {
            LogUtils.e(TAG, "data format error !!!!!");
            return -1f;
        }
        int hi = data[10] & 0xff;
        int low = data[11] & 0xff;
        int val = (hi << 8) | low;
        if (val > 440) {
            LogUtils.d(TAG, "temperature is too hi: " + val * 0.1);
            val = 440;
        }
        return val / 10f;
    }

    /**
     * 获取体温状态：
     * 体温数据大于等于35°，小于等于37.5°标记为『正常』
     * 体温数据小于35°，或大于等于45°，标记为『异常』
     * 体温数据大于37.5，小于45°，标记为『高温』
     *
     * @param temp
     * @return 0-正常 1-高温 -1-异常
     */
    static public int getTemperatureStatus(String temp) {
        int status = 0;
        if (temp.compareTo("35.0") < 0 || temp.compareTo("45.0") >= 0) {
            status = -1;
        } else if (temp.compareTo("37.5") > 0 && temp.compareTo("45.0") < 0) {
            status = 1;
        } else {//体温数据大于等于35°，小于等于37.5°标记为『正常』
            status = 0;
        }
        return status;
    }
}
