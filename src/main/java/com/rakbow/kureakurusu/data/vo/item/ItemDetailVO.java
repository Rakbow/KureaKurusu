package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.person.Personnel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/4/25 10:07
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDetailVO {

    private ItemVO item;
    private int type;
    private PageTraffic traffic;
    private List<Personnel> personnel;
    private Map<String, Object> options;

    private String cover;
    private List<Image> images;
    private int imageCount;
}
