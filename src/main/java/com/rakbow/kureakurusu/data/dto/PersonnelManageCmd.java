package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.person.PersonnelPair;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonnelManageCmd extends Command {

    private int entityType;
    private long entityId;
    private List<PersonnelPair> personnel;

}
