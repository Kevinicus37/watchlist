package org.launchcode.watchlist.Models.dto;

import org.launchcode.watchlist.Models.CastMember;

public class CastListDTO extends MovieDbListDTO {

    private CastMember cast;

    private String profileUrl;

    public CastMember getCast() {
        return cast;
    }

    public void setCast(CastMember cast) {
        this.cast = cast;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
