package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.util.I18nHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Rakbow
 * @since 2022-07-25 2:10
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    //region ------引入实例------
    @Value("${kureakurusu.path.img}")
    private String imgPath;
    @Value("${kureakurusu.path.audio}")
    private String audioPath;
    
//    @Resource
//    private MeiliSearchUtils meiliSearchUtils;

    //endregion

//    @RequestMapping(path = "", method = RequestMethod.GET)
//    public String getIndexPage() {
//        return "/index";
//    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getIndexPage() {
        return "database/database";
    }

    //获取图像
    @RequestMapping(path = "/img/{fileName}", method = RequestMethod.GET)
    public void getCommonImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error(I18nHelper.getMessage("image.load.error") + e.getMessage());
        }
    }

    //获取音频文件
    @RequestMapping(path = "/file/audio/{fileName}", method = RequestMethod.GET)
    public void getAudio(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = audioPath + "/" + fileName;
        // 响应文件
        response.setContentType("audio/mp3");
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error(I18nHelper.getMessage("file.load.error") + e.getMessage());
        }
    }

    //获取错误页面
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }

    //权限不足时
    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(Model model) {
        model.addAttribute("errorMessage", I18nHelper.getMessage("auth.denied"));
        return "/error/404";
    }

    //获取图像
    @RequestMapping(path = "/db/{entity}/{id}/{fileName}", method = RequestMethod.GET)
    public void getImg(@PathVariable("entity") String entity, @PathVariable("fileName") String fileName,
                       @PathVariable("id") int entityId, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/" + entity + "/" + entityId + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取图片失败: " + e.getMessage());
        }
    }

    //获取缩略图
    @RequestMapping(path = "/db/{entity}/{id}/compress/{fileName}", method = RequestMethod.GET)
    public void getCompressImage(@PathVariable("entity") String entity, @PathVariable("fileName") String fileName,
                                 @PathVariable("id") int entityId, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/compress/" + entity + "/" + entityId + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取图片失败: " + e.getMessage());
        }
    }

    // @RequestMapping(path = "/db/simpleSearch", method = RequestMethod.POST)
    // @ResponseBody
    // public String simpleSearch(@RequestBody String json, HttpServletRequest request) throws MeilisearchException {
    //     ApiResult res = new ApiResult();
    //     try {
    //         String keyword = JSON.parseObject(json).getString("keyword");
    //         int entityType = JSON.parseObject(json).getInteger("entityType");
    //         int offset = JSON.parseObject(json).getInteger("offset");
    //         int limit = JSON.parseObject(json).getInteger("limit");
    //
    //         res.data = meiliSearchUtils.simpleSearch(keyword, entityType, offset, limit);
    //     } catch (Exception e) {
    //         res.setErrorMessage(e);
    //     }
    //     return res.toJson();
    // }

}
