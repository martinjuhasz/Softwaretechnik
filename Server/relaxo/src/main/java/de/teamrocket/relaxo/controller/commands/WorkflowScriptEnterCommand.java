package de.teamrocket.relaxo.controller.commands;

import java.util.HashMap;
import java.util.Map;

import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponentValueVisitor;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;
import de.teamrocket.relaxo.persistence.services.JobService;
import de.teamrocket.relaxo.util.ThreadSafePythonInterpreter;

/**
 * Das Command, das beim Erreichen eines {@link WorkflowScript}s ausgeführt wird.
 */
public class WorkflowScriptEnterCommand implements WorkflowItemCommand {

    // Vars

    /**
     * Instanz des JobServices.
     */
    private final JobService jobService;

    /**
     * Instanz der WorkflowItemCommandFactory zum wechsel des aktuellen JobWorkflowItems.
     */
    private final WorkflowItemCommandFactory workflowItemCommandFactory;

    /**
     * Der Python-Interpreter zum ausführen des Python-Codes.
     */
    private final ThreadSafePythonInterpreter interpreter;

    /**
     * Instanz des WorkflowScript-Objects.
     */
    private WorkflowScript workflowScript;

    /**
     * ID des Jobs.
     */
    private int jobId;

    // Construct

    public WorkflowScriptEnterCommand(WorkflowScript workflowScript, int jobId, JobService jobService, WorkflowItemCommandFactory workflowItemCommandFactory, ThreadSafePythonInterpreter interpreter) {
        this.workflowScript = workflowScript;
        this.jobId = jobId;
        this.jobService = jobService;
        this.workflowItemCommandFactory = workflowItemCommandFactory;
        this.interpreter = interpreter;

    }

    // Methods
    /**
     * Zu dem {@link WorkflowScript} wird ein JobWorkflowItem erstellt.
     * Der Inhalt der zu den Variablen entsprechenden JobTaskComponents wird dem {@link ThreadSafePythonInterpreter} übergeben
     * und der Interpreter führt das Script des {@link WorkflowScript}s aus und es wird zum nächsten {@link WorkflowItem} gewechselt.
     */
    @Override
    public void execute() {
        JobWorkflowItem jobWorkflowItem = new JobWorkflowItem();
        jobWorkflowItem.setJobId(jobId);
        jobWorkflowItem.setWorkflowItemId(workflowScript.getId());
        jobService.createJobWorkflowItem(jobWorkflowItem);

        Map<String, Object> variables = new HashMap<>();
        JobTaskComponentValueVisitor visitor = new JobTaskComponentValueVisitor();
        for (TaskVariable taskVariable : workflowScript.getVariables()) {
            JobTaskComponent jobTaskComponent = jobService.getInactiveJobTaskComponent(jobId, taskVariable.getTaskId(), taskVariable.getTaskComponentId());
            jobTaskComponent.accept(visitor);

            variables.put(taskVariable.getName(), visitor.getValue());
        }
        String script = workflowScript.getScript();
        interpreter.exec(script, variables);

        int nextWorkflowItemId = workflowScript.getNextWorkflowItemId();

        jobService.finishJobWorkflowItem(jobWorkflowItem.getId());

        SwitchWorkflowItemCommand switchWorkflowItemCommand = workflowItemCommandFactory.createSwitchWorkflowItemCommand(workflowScript, nextWorkflowItemId, jobId);
        switchWorkflowItemCommand.execute();
    }

}