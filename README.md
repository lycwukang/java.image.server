项目介绍：
=============

项目包含图片上传和图片处理功能，使用`image-client`客户端上传文件至`image-service`服务器，文件会被保存在指定文件目录，使用者访问`image-web`获取图片信息，可以添加参数处理图片

项目部署步骤：
-------------
`image-client`和`image-model`上传至`maven`提供给用户上传图片<br>
`image-service`部署在内网环境，配置`application.properties`，提供图片存储服务<br>
```properties
# 图片存放路径
image.disk.path=/app/images
```

`image-web`部署在外网环境，配置`application.properties`，提供图片访问服务（需要使用cdn服务，否则大量图片处理会拖垮服务器）
```properties
# 图片存放路径
image.disk.path=/app/images
```

客户端使用方式：
--------------

```java
ImageUploadResult result =
    new ResourceClient("http://image-service").uploadImage(new File("图片.jpg"), "2017-07-19/图片.jpg");
if (result.getSuccess()) {
    System.out.println("图片上传成功，图片路径：" + result.getImageFullPath());
} else {
    System.out.println("图片上传失败，原因：" + result.getMessage());
}
```