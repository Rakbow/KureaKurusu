package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOAlpha;
import com.rakbow.kureakurusu.data.entity.Disc;
import com.rakbow.kureakurusu.service.DiscService;
import com.rakbow.kureakurusu.service.EntityService;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.DiscVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-15 20:57
 */
@Controller
@RequestMapping("/db/disc")
public class DiscController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(DiscController.class);

}
