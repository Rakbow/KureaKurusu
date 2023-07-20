package com.rakbow.kureakurusu.service;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-19 18:56
 * @Description:
 */
@Service
public class GeneralService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 刷新redis中的选项缓存
     *
     * @author rakbow
     */
    public void refreshRedisEnumData() {

        Map<String, List<Attribute>> enumOptionsRedisKeyPair = EnumUtil.getOptionRedisKeyPair();
        enumOptionsRedisKeyPair.forEach((k, v) -> {
            redisUtil.set(k, v);
        });

    }

}
