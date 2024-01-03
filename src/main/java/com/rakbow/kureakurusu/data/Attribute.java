package com.rakbow.kureakurusu.data;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-04-30 18:40
 */
@Data
public class Attribute<T> {

    private String label;
    private T value;

    public Attribute() {
        this.label = "";
        this.value = null;
    }

    public Attribute(String label, T value) {
        this.label = label;
        this.value = value;
    }

}
