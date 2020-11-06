package org.launchcode.watchlist.Models.dto;

import org.launchcode.watchlist.Models.ProductionMember;

public class PersonListDTO extends MovieDbListDTO {

    private ProductionMember person;

    private String profileUrl;

    public ProductionMember getPerson() {
        return person;
    }

    public void setPerson(ProductionMember person) {
        this.person = person;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
