package com.rakbow.kureakurusu.config;

import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * @author Rakbow
 * @since 2025/7/12 11:49
 */
@MapperConfig(
        componentModel = "spring",
        uses = {
                GlobalConverters.class // 👈 注册全局转换器
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface MappingConfig {
}
