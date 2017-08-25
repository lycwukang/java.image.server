package com.image.service;

import com.alibaba.fastjson.JSON;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

class HttpImageUpload {

    private static String[] imageMimeTypes = {
            "image/bmp",
            "image/gif",
            "image/jpeg",
            "image/jpeg",
            "image/png"
    };

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    public static ImageUploadResult upload(String endpoint, File file, InputStream inputStream, String name) throws Exception {

        Collection<MimeType> mimeType;
        if (null != file) {
            mimeType = MimeUtil.getMimeTypes(file);
        } else {
            mimeType = MimeUtil.getMimeTypes(inputStream);
        }
        String fileMimeType = "application/octet-stream";
        if (mimeType.iterator().hasNext()) {
            fileMimeType = mimeType.iterator().next().toString();
        }
        if (!ArrayUtils.contains(imageMimeTypes, fileMimeType)) {
            throw new RuntimeException("mimeType不是图片类型");
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(endpoint + "/image/upload");
        ContentType contentType = ContentType.create(fileMimeType);

        ContentBody contentBody;
        if (null != file) {
            contentBody = new FileBody(file, contentType, name);
        } else {
            contentBody = new InputStreamBody(inputStream, contentType, name);
        }

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart("file", contentBody);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String content = EntityUtils.toString(response.getEntity());
        return JSON.parseObject(content, ImageUploadResult.class);
    }
}