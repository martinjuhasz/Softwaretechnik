package de.teamrocket.relaxo.rest.models.start.response;

import java.util.LinkedList;
import java.util.List;

/**
 * REST-Antwort einer detailierten Anfrage eines Workflowitems.
 * Created by Johannes on 16.01.2015.
 */
public class StartDetailResponse {

    /**
     * Liste der UserGroups eines Startitems
     */
    private List<Integer> usergroups;

    public StartDetailResponse() {
        usergroups = new LinkedList<>();
    }

    public List<Integer> getUsergroups() {
        return usergroups;
    }

    public void setUsergroups(List<Integer> usergroups) {
        this.usergroups = usergroups;
    }
}
