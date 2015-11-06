package de.teamrocket.relaxo.rest.models.formgroup.response;

import com.google.inject.Inject;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.rest.models.task.response.TaskComponentResponseFactory;

public class FormGroupResponseFactory {
    private final TaskComponentResponseFactory taskComponentResponseFactory;

    @Inject
    public FormGroupResponseFactory(TaskComponentResponseFactory taskComponentResponseFactory) {
        this.taskComponentResponseFactory = taskComponentResponseFactory;
    }

    public FormGroupResponse create(FormGroup formGroup) {
        return new FormGroupResponse(formGroup, taskComponentResponseFactory);
    }
}
