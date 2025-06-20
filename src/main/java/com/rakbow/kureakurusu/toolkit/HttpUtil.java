package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import com.rakbow.kureakurusu.exception.ApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rakbow
 * @since 2025/6/19 17:03
 */
public class HttpUtil {

    public static EntityMiniVO getEntityInfoByRequestURI(String uri) {
        Pattern pattern = Pattern.compile("^/db/([^/]+)/detail/(\\d+)$");
        Matcher matcher = pattern.matcher(uri);
        if (!matcher.matches()) throw new ApiException("");
        EntityMiniVO res = new EntityMiniVO();
        String type = matcher.group(1);
        long id = Long.parseLong(matcher.group(2));
        switch (type) {
            case "item" -> res.setType(EntityType.ITEM.getValue());
            case "entry" -> res.setType(EntityType.ENTRY.getValue());
            case "ep" -> res.setType(EntityType.EPISODE.getValue());
        }
        res.setId(id);
        return res;
    }

}
