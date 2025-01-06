package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.vo.entry.CharaVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/6/22 12:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "chara", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = CharaVO.class, reverseConvertGenerate = false)
})
public class Chara extends Entry {

    private Long id;

    @AutoMapping(qualifiedByName = "toAttribute")
    private Gender gender;
    private String birthDate;

}
