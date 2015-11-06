package de.teamrocket.relaxo.rest.models.job.response;

public class JobTaskComponentObjectResponse extends JobTaskComponentResponse {

    private Object value;

    public JobTaskComponentObjectResponse(String type, int taskComponentId, Object value) {
        super(type, taskComponentId);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
