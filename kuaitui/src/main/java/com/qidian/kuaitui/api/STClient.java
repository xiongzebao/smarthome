package com.qidian.kuaitui.api;

import com.erongdu.wireless.network.converter.RDConverterFactory;
import com.erongdu.wireless.network.interceptor.HttpLoggingInterceptor;
import com.qidian.base.common.BaseParams;
import com.qidian.base.utils.SharedInfo;
import com.qidian.kuaitui.common.KTConstant;

import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;


public class STClient {
    // 网络请求超时时间值(s)
    private static final int    DEFAULT_TIMEOUT = 30;
    // 请求地址
    private static final String BASE_URL        = BaseParams.URI;
    // retrofit实例
    private Retrofit retrofit;

    /**
     * 私有化构造方法
     */
    private STClient() {
        // 创建一个OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置网络请求超时时间
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 添加签名参数
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("token", (String) SharedInfo.getInstance().getValue(KTConstant.TOKEN,""));

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        // 打印参数
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));


        // 失败后尝试重新请求
        builder.retryOnConnectionFailure(true);

        //修改服务器地址的Url
     //   String inputUrl = (String) SharedInfo.getInstance().getValue("input_url", "");



            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(RDConverterFactory.create())

                    .build();

    }

    /**
     * 调用单例对象
     */
    private static STClient getInstance() {
        if(instance==null){
            instance = new STClient();
        }
        return instance;
    }

    /**
     * 创建单例对象
     */

    static STClient instance ;

    public static void reCreate(){
        instance = new STClient();
    }


    ///////////////////////////////////////////////////////////////////////////
    // service
    ///////////////////////////////////////////////////////////////////////////
    private static TreeMap<String, Object> serviceMap;

    private static TreeMap<String, Object> getServiceMap() {
        if (serviceMap == null)
            serviceMap = new TreeMap<>();
        return serviceMap;
    }

    /**
     * @return 指定service实例
     */
    public static <T> T getService(Class<T> clazz) {
        if (getServiceMap().containsKey(clazz.getSimpleName())) {
            return (T) getServiceMap().get(clazz.getSimpleName());
        }
        T service = STClient.getInstance().retrofit.create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }
}
