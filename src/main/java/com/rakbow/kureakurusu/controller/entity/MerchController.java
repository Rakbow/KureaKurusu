package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOAlpha;
import com.rakbow.kureakurusu.data.entity.Merch;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.MerchVOMapper;
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
 * @since 2023-01-04 16:57
 */

@Controller
@RequestMapping("/db/merch")
public class MerchController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(MerchController.class);

}
