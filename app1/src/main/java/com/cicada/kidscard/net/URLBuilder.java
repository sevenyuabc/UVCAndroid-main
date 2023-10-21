package com.cicada.kidscard.net;

/**
 * <p/>
 * 创建时间: 16/10/25 上午10:49 <br/>
 *
 * @author zhaohaiyang
 * @since v0.0.1
 */

public class URLBuilder {

    private final String baseUrl;
    private final StringBuffer buffer;

    public URLBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        buffer = new StringBuffer(baseUrl);

    }

    public URLBuilder addParams(String param, String value) {

        if (buffer.indexOf(param) == -1) {
            if (buffer.indexOf("?") == -1) {
                buffer.append("?");
            } else {
                buffer.append("&");
            }
            buffer.append(param).append("=").append(value);
        }
        return this;
    }

    public String build() {
        return buffer.toString();
    }
}
