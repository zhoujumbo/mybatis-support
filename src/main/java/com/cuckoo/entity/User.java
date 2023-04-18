package com.cuckoo.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


@Getter
@Setter
public class User implements Serializable {
    private Integer id;
    private String name;

    private String phone;
    private Integer version;

    public User() {
}

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("name", name)
                .append("phone", phone)
                .append("version", version)
                .toString();
    }
}
