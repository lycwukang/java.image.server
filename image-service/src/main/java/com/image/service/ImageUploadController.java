package com.image.service;

import com.google.common.io.Files;
import com.image.service.ImageUploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@RestController
public class ImageUploadController {

    @Value(value = "${image.disk.path}")
    private String imageDiskPath;

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    public ImageUploadResult upload(@RequestParam("file") MultipartFile file) throws Exception {

        try {
            CommonsMultipartFile commonFile = ((CommonsMultipartFile) file);
            String filePath = imageDiskPath + "/" + commonFile.getFileItem().getName();

            File saveFile = new File(filePath);
            if (saveFile.exists()) {
                return new ImageUploadResult(false, "文件已存在");
            }
            Files.createParentDirs(saveFile);
            Files.write(file.getBytes(), saveFile);

            BufferedImage image = ImageIO.read(saveFile);

            ImageUploadResult result = new ImageUploadResult(true, "上传成功");
            result.setImagePath(saveFile.getParent().substring(imageDiskPath.length()) + "/");
            result.setImageName(Files.getNameWithoutExtension(filePath));
            result.setImageSuffix("." + Files.getFileExtension(filePath));
            result.setImageFullPath(result.getImagePath() + result.getImageName() + result.getImageSuffix());
            result.setImageWidth(image.getWidth());
            result.setImageHeight(image.getHeight());
            result.setImageLength(saveFile.length());
            return result;
        } catch (Exception e) {
            return new ImageUploadResult(false, "操作异常:" + e.getMessage());
        }
    }
}