package de.teamrocket.relaxo.models.workflow;

import java.util.List;

/**
 * Klasse zur Repraesentation eines FormGroup
 */
public class FormGroupForTask {
    /**
     * ID der FormGroup
     */
    private int formGroupId;

    /**
     * Liste der IDs der zugeordneten TaskComponents
     */
    private List<Integer> taskComponentIds;

    public int getFormGroupId() {
        return formGroupId;
    }

    public void setFormGroupId(int formGroupId) {
        this.formGroupId = formGroupId;
    }

    public List<Integer> getTaskComponentIds() {
        return taskComponentIds;
    }

    public void setTaskComponentIds(List<Integer> taskComponentIds) {
        this.taskComponentIds = taskComponentIds;
    }
}
