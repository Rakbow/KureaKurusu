package com.rakbow.kureakurusu.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/01/07 1:28
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Audio {

    private String title;
    private String artist;
    private String url;
    private String cover;
    private String lrc;

}
