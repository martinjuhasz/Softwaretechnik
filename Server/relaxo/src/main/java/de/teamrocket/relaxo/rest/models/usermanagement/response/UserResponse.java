package de.teamrocket.relaxo.rest.models.usermanagement.response;

import de.teamrocket.relaxo.models.usermanagement.User;

/**
 * Created by nakih on 09.01.15.
 */
public class UserResponse {

    private final String username;
    private final String name;
    private final String prename;
    private final boolean isAdmin;
    private final boolean isActive;
    private final int id;

    public UserResponse(User user) {
        this.name = user.getName();
        this.prename = user.getPrename();
        this.username = user.getUsername();
        this.isAdmin = user.isAdmin();
        this.isActive = user.isActive();
        this.id = user.getId();
    }


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPrename() {
        return prename;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getId() {
        return id;
    }
}
