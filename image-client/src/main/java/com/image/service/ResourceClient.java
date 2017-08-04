package com.image.service;

import java.io.*;

public class ResourceClient {

    private String endpoint;

    public ResourceClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public ImageUploadResult uploadImage(File file, String name) throws Exception {
        return HttpImageUpload.upload(endpoint, file, null, name);
    }

    public ImageUploadResult uploadImage(InputStream stream, String name) throws Exception {
        InputStream newStream;
        if (stream instanceof BufferedInputStream) {
            newStream = stream;
        } else {
            newStream = new BufferedInputStream(stream);
        }
        return HttpImageUpload.upload(endpoint, null, newStream, name);
    }
}