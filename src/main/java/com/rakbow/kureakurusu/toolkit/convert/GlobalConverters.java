package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import lombok.SneakyThrows;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
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

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <X, E extends Enum<E>> Attribute<X> toAttribute(E e) {
        Method getValueMethod = e.getClass().getMethod("getValue");
        Method getLabelMethod = e.getClass().getMethod("getLabelKey");
        String labelKey = getLabelMethod.invoke(e).toString();
        String label = I18nHelper.getMessage(labelKey);
        X value = (X) getValueMethod.invoke(e);
        return new Attribute<>(label, value);
    }

    @Named("entryThumb")
    public String entryThumb(String thumb) {
        return CommonImageUtil.getEntryThumb(thumb);
    }

    @Named("subName")
    public String subName(String nameEn, String nameZh) {
        return CommonUtil.getSubName(nameEn, nameZh);
    }

}
