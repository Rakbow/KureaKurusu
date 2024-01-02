package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCommand extends Command {

    @Serial
    private static final long serialVersionUID = 1L;

}
