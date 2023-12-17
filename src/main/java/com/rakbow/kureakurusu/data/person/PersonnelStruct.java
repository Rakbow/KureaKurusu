package com.rakbow.kureakurusu.data.person;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-17 22:19
 * @Description:
 */
@Data
public class PersonnelStruct {

    private List<Personnel> personnel;
    private List<PersonnelPair> editPersonnel;

    public PersonnelStruct() {
        personnel = new ArrayList<>();
        editPersonnel = new ArrayList<>();
    }

    public void addPersonnel(Personnel personnel) {
        this.personnel.add(personnel);
    }

    public void addEditPersonnel(PersonnelPair personnel) {
        this.editPersonnel.add(personnel);
    }

}
