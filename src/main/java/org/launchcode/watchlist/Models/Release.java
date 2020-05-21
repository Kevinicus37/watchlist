package org.launchcode.watchlist.Models;

import java.util.List;

public class Release {

    private String iso31661;
    private List<ReleaseDate> releaseDates;

    public Release(){}
    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public List<ReleaseDate> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(List<ReleaseDate> releaseDates) {
        this.releaseDates = releaseDates;
    }
}
