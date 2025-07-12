package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.toolkit.DateHelper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/7/12 11:51
 */
@Component
public class GlobalConverters {

    public String convert(Timestamp ts) {
        return DateHelper.timestampToString(ts);
    }

}
