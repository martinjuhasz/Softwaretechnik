package de.teamrocket.relaxo.rest.models.workflowscript.request;

import java.util.List;

import de.teamrocket.relaxo.rest.models.WorkflowDecision.request.TaskVariableRequest;

public class WorkflowScriptUpdateRequest {
    private List<TaskVariableRequest> variables;
    private String script;

    public List<TaskVariableRequest> getVariables() {
        return variables;
    }

    public void setVariables(List<TaskVariableRequest> variables) {
        this.variables = variables;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
