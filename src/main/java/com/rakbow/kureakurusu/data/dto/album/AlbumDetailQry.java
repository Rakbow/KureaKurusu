package com.rakbow.kureakurusu.data.dto.album;

import com.rakbow.kureakurusu.data.dto.DetailQry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDetailQry extends DetailQry {

    private long id;

}
