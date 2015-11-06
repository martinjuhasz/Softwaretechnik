package de.teamrocket.relaxo.rest.models.job.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class JobTaskComponentDateResponse extends JobTaskComponentResponse {

    private Date value;

    public JobTaskComponentDateResponse(String type, int taskComponentId, Date value) {
        super(type, taskComponentId);
        this.value = value;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

}
