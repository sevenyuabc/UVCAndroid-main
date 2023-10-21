package com.cicada.kidscard.config;

public enum AppEnvConfig {
    /**
     * 测试环境
     */
    TEST(1,
            "测试环境",
            "https://testsaas.imeduplus.com/"),

    RELEASE(2,
            "正式环境",
            "https://web.imeduplus.com/");

    private final int index;
    private final String name;
    private final String apiUrl;

    AppEnvConfig(int index, String name, String apiUrl) {
        this.index = index;
        this.name = name;
        this.apiUrl = apiUrl;
    }

    public int getIndex() {
        return index;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getName() {
        return name;
    }

    public static AppEnvConfig typeOf(String appEnvName) {
        return valueOf(appEnvName);
    }
}
