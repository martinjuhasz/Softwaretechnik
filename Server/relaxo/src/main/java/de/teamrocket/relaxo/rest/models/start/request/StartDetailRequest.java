package de.teamrocket.relaxo.rest.models.start.request;

import java.util.LinkedList;
import java.util.List;

/**
 * REST-Anfrage einer detailierten Anfrage eines Workflowitems.
 * Created by Johannes on 16.01.2015.
 */
public class StartDetailRequest {

    /**
     * Liste der UserGroups eines Startitems
     */
    private List<Integer> usergroups;

    public StartDetailRequest() {
        usergroups = new LinkedList<>();
    }

    public List<Integer> getUsergroups() {
        return usergroups;
    }

    public void setUsergroups(List<Integer> usergroups) {
        this.usergroups = usergroups;
    }
}
