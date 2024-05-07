package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.data.emun.LinkType;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2024/3/19 1:29
 */
@Data
public class LinkVO {

    private int type;
    private String url;
    private String name;

    public LinkVO(Link link) {
        type = link.getType();
        if(type == LinkType.TWITTER.getValue()) {
            if(link.getUrl().contains(AT)){
                url = STR."\{HTTPS_SUFFIX}\{TWITTER}\{SLASH}\{link.getUrl().replace(AT, "")}";
                name = link.getUrl();
            }else {
                url = link.getUrl();
                Pattern pattern = Pattern.compile("twitter.com/(\\w+)");
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) name = STR."\{AT}\{matcher.group(1)}";
            }
        }else {
            name = I18nHelper.getMessage(LinkType.get(type).getLabelKey());
            url = link.getUrl();
        }
        if(!url.contains(HTTP_SCHEME))
            url = STR."\{HTTPS_SUFFIX}\{link.getUrl()}";
    }

}
