package de.teamrocket.relaxo.controller.commands;

import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponentValueVisitor;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;
import de.teamrocket.relaxo.persistence.services.JobService;
import de.teamrocket.relaxo.util.ThreadSafePythonInterpreter;
import org.python.core.Py;
import org.python.core.PyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Das Command, das beim Erreichen einer {@link WorkflowDecision} im Workflow ausgeführt wird.
 */
public class WorkflowDecisionEnterCommand implements WorkflowItemCommand {

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
     * Instanz des WorkflowDecision-Objects.
     */
    private WorkflowDecision workflowDecision;

    /**
     * ID des Jobs.
     */
    private int jobId;

    // Construct

    public WorkflowDecisionEnterCommand(WorkflowDecision workflowDecision, int jobId, JobService jobService, WorkflowItemCommandFactory workflowItemCommandFactory, ThreadSafePythonInterpreter interpreter) {
        this.workflowDecision = workflowDecision;
        this.jobId = jobId;
        this.jobService = jobService;
        this.workflowItemCommandFactory = workflowItemCommandFactory;
        this.interpreter = interpreter;

    }

    // Methods

    /**
     * Zu der Decision wird ein JobWorkflowItem erstellt.
     * Der Inhalt der zu den Variablen entsprechenden JobTaskComponents wird dem {@link ThreadSafePythonInterpreter} übergeben
     * und der Interpreter wertet die Condition aus. Je nach ergebnis wird entweder zum WorkflowItem, das als nextWorkflowItemOnTrue
     * angegeben ist, weitergrückt oder zu dem anderen WorkflowItem, das als nextWorkflowItem der Decision gespeichert ist.
     */
    @Override
    public void execute() {
        JobWorkflowItem jobWorkflowItem = new JobWorkflowItem();
        jobWorkflowItem.setJobId(jobId);
        jobWorkflowItem.setWorkflowItemId(workflowDecision.getId());
        jobService.createJobWorkflowItem(jobWorkflowItem);

        Map<String, Object> variables = new HashMap<>();
        JobTaskComponentValueVisitor visitor = new JobTaskComponentValueVisitor();
        for (TaskVariable taskVariable : workflowDecision.getVariables()) {
            JobTaskComponent jobTaskComponent = jobService.getInactiveJobTaskComponent(jobId, taskVariable.getTaskId(), taskVariable.getTaskComponentId());
            jobTaskComponent.accept(visitor);

            variables.put(taskVariable.getName(), visitor.getValue());
        }
        String condition = workflowDecision.getCondition();
        PyObject result = interpreter.eval(condition, variables);

        int nextWorkflowItemId = workflowDecision.getNextWorkflowItemOnTrue();

        if (!Py.py2boolean(result)) {
            int nextWorkflowItemOnTrue = workflowDecision.getNextWorkflowItemOnTrue();

            for (Integer workFlowItemId : workflowDecision.getNextWorkflowItems()) {
                if (workFlowItemId != nextWorkflowItemOnTrue) {
                    nextWorkflowItemId = workFlowItemId;
                    break;
                }
            }
        }

        jobService.finishJobWorkflowItem(jobWorkflowItem.getId());

        SwitchWorkflowItemCommand switchWorkflowItemCommand = workflowItemCommandFactory.createSwitchWorkflowItemCommand(workflowDecision, nextWorkflowItemId, jobId);
        switchWorkflowItemCommand.execute();
    }

}
