package de.teamrocket.relaxo.messaging.beans;

import de.teamrocket.relaxo.models.usermanagement.User;

/**
 * Bean für Payload einer Message für eine Änderung im WorkflowEditor.
 */
public class MessagingUserBean {
    /**
     * User ID des Benutzers, der die Änderung durchgeführt hat, für die eine Message geschickt wird.
     */
    private int userId;

    public MessagingUserBean(User user) {
        this.userId = user.getId();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
