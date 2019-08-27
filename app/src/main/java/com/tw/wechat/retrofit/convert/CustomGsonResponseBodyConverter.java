package com.tw.wechat.retrofit.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.tw.wechat.utils.AssetsToString;
import com.tw.wechat.utils.JsonFormat;
import com.tw.wechat.utils.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 *
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = JsonFormat.format(value.string());
        if (response.contains("["))
            response = AssetsToString.xmlToString("tweets.json");
        LogUtil.e("response", response);
        response = response.replace("profile-image", "profileImage");
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
