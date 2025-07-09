package com.rakbow.kureakurusu.data.vo.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/8 11:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRelatedVO {

    private List<FileListVO> files;
    private String size;

}
