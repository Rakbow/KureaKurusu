package com.rakbow.kureakurusu.util.common;

import com.rakbow.kureakurusu.data.dto.AlbumItemUpdateDTO;
import com.rakbow.kureakurusu.data.dto.ItemUpdateDTO;
import com.rakbow.kureakurusu.data.emun.ItemType;

/**
 * @author Rakbow
 * @since 2024/4/26 17:04
 */
public class ItemUtil {

    public static ItemUpdateDTO getItemUpdateDTO(String param) {
        int type = JsonUtil.getIntValueByKey("type", param);
        ItemUpdateDTO dto = null;
        if(type == ItemType.ALBUM.getValue()){
            dto = JsonUtil.to(param, AlbumItemUpdateDTO.class);
        }
        return dto;
    }

}
