package de.teamrocket.relaxo.models.formgroup;

/**
 * Repräsentiert die Sortierung einer Component
 */
public class TaskComponentOrder {

    private int id;
    private int order;

    public TaskComponentOrder(int id, int order) {
        this.id = id;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
