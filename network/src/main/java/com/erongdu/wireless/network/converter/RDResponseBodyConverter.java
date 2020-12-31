/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.erongdu.wireless.network.converter;

import com.erongdu.wireless.network.entity.HttpResult;
import com.erongdu.wireless.network.entity.Params;
import com.erongdu.wireless.network.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/6 9:35
 * <p/>
 * Description:  JSON response 解析
 */
@SuppressWarnings("unused")
final class RDResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson           gson;
    private final TypeAdapter<T> adapter;

    RDResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string().trim();
        value.close();

        try {
            JSONObject object = new JSONObject(response);
            System.out.println(object.toString());
            // 解析 resCode ,对不成功的返回做统一处理
//            if (object.getInt(Params.RES_CODE) != Params.RES_SUCCEED && object.getInt(Params.RES_CODE) != Params.PWD_ERROR) {
//                throw new ApiException(new Gson().fromJson(response, HttpResult.class));
//            }
            //todo 起点人力~ errorcode=0代表接口正常
        /*    if (object.getInt("errorcode") != 0 ) {
                throw new ApiException(new Gson().fromJson(response, HttpResult.class));
            }*/


            //todo 起点在这~ type=0代表info消息接口类型，type=0代表消息接口类型，type=1代表successs，type=2代表warning,type=3代表error
       /*     if (object.getInt("type") != 1 ) {
                throw new ApiException(new Gson().fromJson(response, HttpResult.class));
            }

            if(!object.getBoolean("Success")){
                throw new ApiException(new Gson().fromJson(response, HttpResult.class));
            }
*/
            StringReader reader     = new StringReader(response);
            JsonReader   jsonReader = gson.newJsonReader(reader);
            return adapter.read(jsonReader);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
