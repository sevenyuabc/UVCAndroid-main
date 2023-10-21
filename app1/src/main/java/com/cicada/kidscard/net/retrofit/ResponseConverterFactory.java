package com.cicada.kidscard.net.retrofit;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.R;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.net.BaseURL;
import com.cicada.kidscard.net.domain.Result;
import com.cicada.kidscard.net.exception.BusinessException;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.Preconditions;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ResponseConverterFactory extends Converter.Factory {


    public static ResponseConverterFactory create(boolean isConvert) {
        return create(new Gson(), isConvert);
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static ResponseConverterFactory create(Gson gson, boolean isConvert) {
        return new ResponseConverterFactory(gson, isConvert);
    }

    private final Gson gson;
    private final boolean isConvert;

    private ResponseConverterFactory(Gson gson, boolean isConvert) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.isConvert = isConvert;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (isConvert) {
            return new GsonResponseBodyConverter<>(gson, type);
        } else {
            return new StringResponseBodyConverter();
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

}

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;
    private final Type mType;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.mGson = gson;
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        try {
            AppSharedPreferences.getInstance().setBooleanValue(Constants.NET_AVAILABLE, true);
            Result result = JSON.parseObject(response, Result.class);
            if (result.isSuccess()) {
                AppSharedPreferences.getInstance().setServerTimeStamp(result.getTs());
                Object resultObj = Preconditions.isNotEmpty(result.getBizData()) ? result.getBizData() : result.getData();
                String resultStr = mGson.toJson(resultObj);
                if (Preconditions.isEmpty(resultObj) || resultStr.equals("{}")) {
                    return JSON.parseObject(response, mType);
                } else {
                    return JSON.parseObject(JSON.toJSONString(resultObj), mType);
                }
            } else {
                String code =  Preconditions.isNotEmpty(result.getRtnCode()) ? result.getRtnCode() : String.valueOf(result.getCode());
                throw new BusinessException(code, result.getMsg());
            }

        } catch (BusinessException e) {
            LogUtils.d("返回数据解析异常==5==", e.toString());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("返回数据解析异常==6==", e.toString());
            throw new BusinessException(BaseURL.APP_EXCEPTION_HTTP_OTHER,
                    AppContext.getContext().getResources().getString(R.string.app_exception_connect_no));
        }
    }
}


final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readString(UTF_8));
    }
}

class StringConverterFactory extends Converter.Factory {
    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    private StringConverterFactory() {

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new StringResponseBodyConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestBodyConverter();
    }
}


class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}


class StringRequestBodyConverter implements Converter<String, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    StringRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(String value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        writer.write(value);
        writer.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readString(UTF_8));
    }
}

