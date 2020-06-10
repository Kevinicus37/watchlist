package org.launchcode.watchlist.Models;

import javax.persistence.Entity;

@Entity
public class ApiKey extends AbstractEntity{

    private String name;

    private String value;

    public ApiKey(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
