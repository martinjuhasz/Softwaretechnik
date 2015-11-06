package de.teamrocket.relaxo.rest.models.task.response;

import com.google.inject.Inject;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.rest.models.formgroup.response.FormGroupResponseFactory;

import java.util.List;

public class TaskDetailResponseFactory {
    private final FormGroupResponseFactory formGroupResponseFactory;

    @Inject
    public TaskDetailResponseFactory(FormGroupResponseFactory formGroupResponseFactory) {
        this.formGroupResponseFactory = formGroupResponseFactory;
    }

    public TaskDetailResponse create(Task task, List<Integer> userGroups, List<FormGroup> formGroups) {
        return new TaskDetailResponse(task, userGroups, formGroups, formGroupResponseFactory);
    }
}
