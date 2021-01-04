package com.qidian.kuaitui.api;

import com.erongdu.wireless.network.converter.RDConverterFactory;
import com.erongdu.wireless.network.interceptor.HttpLoggingInterceptor;
import com.erongdu.wireless.tools.log.MyLog;
import com.qidian.base.common.BaseParams;
import com.qidian.base.utils.SharedInfo;
import com.qidian.kuaitui.common.KTConstant;

import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;


public class STClient {

    static boolean  isRecreate = false;
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

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String method=original.method();
                if("POST".equals(method)){
                    StringBuilder sb = new StringBuilder();
                    if (original.body() instanceof FormBody) {
                        MyLog.d("FormBody");
                        FormBody body = (FormBody) original.body();
                        for (int i = 0; i < body.size(); i++) {
                            sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                        }
                        sb.delete(sb.length() - 1, sb.length());
                        MyLog.d(sb.toString());
                    }

                    if(original.body() instanceof MultipartBody){
                        MyLog.d("MultipartBody");
                    }
                    if(original.body() instanceof RequestBody){
                        MyLog.d(original.toString());

                    }
                }

                MyLog.e("add token");
                Request.Builder requestBuilder = original.newBuilder()
                        .header("token", (String) SharedInfo.getInstance().getValue(KTConstant.TOKEN,""));
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };


        // 添加签名参数
        builder.addInterceptor(interceptor);
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
            MyLog.e("new STClient()");
        }
        return instance;
    }

    /**
     * 创建单例对象
     */

    static STClient instance ;

    public static void reCreate(){
        isRecreate = true;
        try {
            instance = new STClient();
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }

        MyLog.e("STClient reCreate");
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

        if (getServiceMap().containsKey(clazz.getSimpleName())&&!isRecreate) {
            isRecreate = false;
            return (T) getServiceMap().get(clazz.getSimpleName());
        }
        MyLog.e("create a new Service");
        T service = STClient.getInstance().retrofit.create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }
}
