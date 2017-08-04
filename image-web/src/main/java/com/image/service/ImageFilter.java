package com.image.service;

import com.google.common.io.Files;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;

public class ImageFilter implements Filter {

    private String imageDiskPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        imageDiskPath = context.getBean(PropertyPlaceHolder.class).getProperty("image.disk.path");
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            File file = new File(imageDiskPath + request.getRequestURI());

            if (file.exists() && file.isFile()) {

                Collection<MimeType> mimeType = MimeUtil.getMimeTypes(file);
                String fileMimeType = "application/octet-stream";
                if (mimeType.iterator().hasNext()) {
                    fileMimeType = mimeType.iterator().next().toString();
                }
                response.setContentType(fileMimeType);

                String process = request.getParameter("process");
                if (StringUtils.isEmpty(process)) {
                    Files.copy(file, response.getOutputStream());
                } else {
                    // 图片缩放
                    String resizeMethod = null;
                    int resizeWidth = 0, resizeHeight = 0, resizeLimit = 1;

                    for (String cmd : StringUtils.split(process, "/")) {
                        String[] controls = StringUtils.split(cmd, ",");
                        if ("resize".equals(controls[0])) {
                            for (String control : controls) {
                                if (control.startsWith("m_")) {
                                    resizeMethod = control.substring(2);
                                } else if (control.startsWith("w_")) {
                                    resizeWidth = Integer.parseInt(control.substring(2));
                                } else if (control.startsWith("h_")) {
                                    resizeHeight = Integer.parseInt(control.substring(2));
                                } else if (control.startsWith("limit_")) {
                                    resizeLimit = Integer.parseInt(control.substring(6));
                                }
                            }
                        }
                    }

                    BufferedImage image = ImageIO.read(file);

                    Thumbnails.Builder imageBuilder = Thumbnails.of(image).outputFormat(Files.getFileExtension(file.getName()));
                    // 图片缩放
                    if (resizeWidth > 0 || resizeHeight > 0) {
                        if (StringUtils.isEmpty(resizeMethod)) {
                            resizeMethod = "lfit";
                        }
                        if (resizeWidth <= 0) {
                            resizeWidth = image.getWidth();
                        }
                        if (resizeHeight <= 0) {
                            resizeHeight = image.getHeight();
                        }

                        if (resizeLimit == 1) {
                            if (resizeWidth > image.getWidth()) {
                                resizeWidth = image.getWidth();
                            }
                            if (resizeHeight > image.getHeight()) {
                                resizeHeight = image.getHeight();
                            }
                        }

                        if ("mfit".equals(resizeMethod)) {
                            if (resizeWidth < resizeHeight) {
                                resizeWidth = image.getWidth();
                            } else if (resizeHeight < resizeWidth) {
                                resizeHeight = image.getHeight();
                            }
                        }

                        if ("fixed".equals(resizeMethod)) {
                            imageBuilder.forceSize(resizeWidth, resizeHeight);
                        } else {
                            imageBuilder.size(resizeWidth, resizeHeight);
                        }
                    }

                    imageBuilder.toOutputStream(response.getOutputStream());
                }
            }
        } catch (Exception e) {
            response.getWriter().print("error");
        }
    }

    @Override
    public void destroy() {
    }
}