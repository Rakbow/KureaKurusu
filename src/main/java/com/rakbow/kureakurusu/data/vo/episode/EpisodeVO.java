package com.rakbow.kureakurusu.data.vo.episode;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/5/27 13:06
 */
@Data
public class EpisodeVO {

    private Long id;
    private long relatedType;
    private long relatedId;
    private String title;
    private String titleEn;
    private String premiereDate;
    private String duration;
    private int discNo;
    private int serial;
    private String detail;
    private int episodeType;
    private String addedTime;
    private String editedTime;
    private boolean status;

    private PageTraffic traffic;
    private String cover;

}
