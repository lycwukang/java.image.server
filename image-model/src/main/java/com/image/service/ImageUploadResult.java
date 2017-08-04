package com.image.service;

import java.io.Serializable;

public class ImageUploadResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String message;
    private String imagePath;
    private String imageName;
    private String imageSuffix;
    private String imageFullPath;
    private Integer imageWidth;
    private Integer imageHeight;
    private Long imageLength;

    public ImageUploadResult() {
    }

    public ImageUploadResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageSuffix() {
        return imageSuffix;
    }

    public void setImageSuffix(String imageSuffix) {
        this.imageSuffix = imageSuffix;
    }

    public String getImageFullPath() {
        return imageFullPath;
    }

    public void setImageFullPath(String imageFullPath) {
        this.imageFullPath = imageFullPath;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Long getImageLength() {
        return imageLength;
    }

    public void setImageLength(Long imageLength) {
        this.imageLength = imageLength;
    }
}