package org.launchcode.watchlist.Models;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractMovieField extends AbstractEntity{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
