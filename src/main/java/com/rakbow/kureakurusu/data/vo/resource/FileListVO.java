package com.rakbow.kureakurusu.data.vo.resource;

import com.rakbow.kureakurusu.data.vo.EntityListVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/6/8 7:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileListVO extends EntityListVO {

    private String name;
    private String extension;
    private String size;
    private String path;

}
