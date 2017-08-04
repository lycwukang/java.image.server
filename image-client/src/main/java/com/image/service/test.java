package com.image.service;

import java.io.File;

/**
 * Created by kangwu on 2017/7/19.
 */
public class test {

    public static void main(String[] args) throws Exception {
        String test = "a/b/c.jpg";
        String test2 = test.substring("a/b".length());

        ImageUploadResult result = new ResourceClient("http://172.30.2.103:7010").uploadImage(new File("/users/kangwu/Desktop/新建文件夹/129823_120.jpg"), "2017-07-19/test.jpg");
    }
}
