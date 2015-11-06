package de.teamrocket.relaxo.rest.models.usermanagement.response;

/**
 * Created by henry on 17.01.15.
 */
public class CreateUserResponse {

    private int id;

    public CreateUserResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
