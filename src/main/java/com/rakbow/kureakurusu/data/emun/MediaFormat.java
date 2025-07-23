package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 媒体类型
 *
 * @author Rakbow
 * @since 2022-08-19 23:04
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum MediaFormat {

    DEFAULT(0, "enum.default"),
    CD(1, "enum.media_format.cd"),
    DVD(2, "enum.media_format.dvd"),
    BLU_RAY(3, "enum.media_format.blu_ray"),
    CASSETTE(4, "enum.media_format.cassette"),
    VINYL(5, "enum.media_format.vinyl"),
    VHS(6, "enum.media_format.vhs"),
    DIGITAL(7, "enum.media_format.digital"),
    CD_VIDEO(8, "enum.media_format.cd_video"),
    SA_CD(9, "enum.media_format.sa_cd"),
    LASER_DISC(10, "enum.media_format.laser_disc"),
    FLOPPY_DISC(11, "enum.media_format.floppy_disc"),
    FLEXI_DISC(12, "enum.media_format.flexi_disc"),
    UHQCD(13, "enum.media_format.uhqcd"),
    BLU_SPEC_CD(14, "enum.media_format.blu_spec_cd"),
    BLU_SPEC_CD2(15, "enum.media_format.blu_spec_cd2"),
    HQCD(16, "enum.media_format.hqcd"),
    SHM_CD(17, "enum.media_format.shm_cd"),
    PLAYBUTTON(18, "enum.media_format.playbutton"),
    MINI_DISC(19, "enum.media_format.mini_disc"),
    HDCD(20, "enum.media_format.hdcd"),
    BETAMAX(21, "enum.media_format.betamax"),
    USB(22, "enum.media_format.usb"),
    DOWNLOAD_CODE(23, "enum.media_format.download_code");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static MediaFormat get(int value) {
        for (MediaFormat format : MediaFormat.values()) {
            if(format.value == value)
                return format;
        }
        return DEFAULT;
    }

}
