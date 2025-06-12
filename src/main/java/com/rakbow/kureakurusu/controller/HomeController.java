package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Rakbow
 * @since 2022-07-25 2:10
 */
@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    //region ------引入实例------
    @Value("${system.path.img}")
    private String imgPath;

    //获取图像
    @GetMapping("/img/{fileName}")
    public void getCommonImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = STR."\{imgPath}/\{fileName}";
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType(STR."image/\{suffix}");
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int b;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            log.error(I18nHelper.getMessage("image.load.error") + e.getMessage());
        }
    }

    //获取图像
    @GetMapping("/db/{entity}/{id}/{fileName}")
    public void getImg(@PathVariable("entity") String entity, @PathVariable("fileName") String fileName,
                       @PathVariable("id") int entityId, HttpServletResponse response) {
        // 服务器存放路径
        fileName = STR."\{imgPath}/\{entity}/\{entityId}/\{fileName}";
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType(STR."image/\{suffix}");
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int b;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            log.error(STR."\{I18nHelper.getMessage("image.load.error")} \{e.getMessage()}");
        }
    }

}
