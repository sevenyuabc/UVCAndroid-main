package com.cicada.kidscard.net.retrofit;


import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.net.BaseURL;
import com.cicada.kidscard.net.SSLSocketFactoryUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitUtils {

    private static Retrofit instance;

    public static <T> T createService(Class<T> clazz) {
        return createService(clazz, true);
    }

    public static <T> T createService(Class<T> clazz, boolean isConvert) {

        if (null == instance) {
            synchronized (RetrofitUtils.class) {
                if (null == instance) {
                    OkHttpClient.Builder clientBuilder = getClientBuilder();
                    OkHttpClient client =
                            clientBuilder.sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory())
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .writeTimeout(60, TimeUnit.SECONDS)
                                    .build();
                    instance = new Retrofit.Builder()
                            .baseUrl(BaseURL.getBaseURL())
                            .addConverterFactory(ResponseConverterFactory.create(isConvert))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                            .client(client)
                            .build();
                }
            }
        }
        return instance.create(clazz);
    }


    public static <T> T createTimeOutService(Class<T> clazz, long time) {
        OkHttpClient.Builder clientBuilder = getClientBuilder();
        OkHttpClient client =
                clientBuilder.sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory())
                        .connectTimeout(time, TimeUnit.SECONDS)
                        .readTimeout(time, TimeUnit.SECONDS)
                        .writeTimeout(time, TimeUnit.SECONDS)
                        .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.getBaseURL())
                .addConverterFactory(ResponseConverterFactory.create(true))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(client)
                .build();
        return retrofit.create(clazz);
    }

    private static OkHttpClient.Builder getClientBuilder() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(AppContext.isRelease() ? HttpLoggingInterceptor.Level.NONE : HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(logging);

//        return new OkHttpClient.Builder();
    }


    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createUrlParamsWithBaseUrl(Class<T> clazz, String baseUrl) {
        OkHttpClient.Builder clientBuilder = getClientBuilder();
        OkHttpClient client =
                clientBuilder.sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory())
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(client)
                .build();

        return retrofit.create(clazz);
    }

    private static class TokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            return chain.proceed(newBuilder.build());
        }
    }
}
