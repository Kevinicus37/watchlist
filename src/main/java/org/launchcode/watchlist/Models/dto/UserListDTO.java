package org.launchcode.watchlist.Models.dto;

import org.launchcode.watchlist.Models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListDTO extends AbstractListDTO {

    private List<User> users = new ArrayList<>();

    private int totalUsers;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
}
