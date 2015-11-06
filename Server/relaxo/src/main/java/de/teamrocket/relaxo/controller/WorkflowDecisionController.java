package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;

import java.util.List;

/**
 * Controller Interface fuer WorkflowDecision stellt Methoden bereit, um spezifisch auf WorkflowDecision zuzugreifen.
 */
public interface WorkflowDecisionController {

    /**
     * Aktualisiert die Daten von WorkflowDecision-Objekten.
     *
     * @param workflowDecisionId die ID des WorkflowDecision
     * @param variables Liste der Variablen
     * @param condition die Bedingung als Python-Code
     * @param nextWorkflowItemOnTrue das WorkflowItem, zu welchem der flow fließen soll, sofern die in condition zutrifft.
     * @throws WorkflowItemNotFoundException
     */
    public void updateWorkflowDecision(int workflowDecisionId, List<TaskVariable> variables, String condition, int nextWorkflowItemOnTrue) throws WorkflowItemNotFoundException;

    /**
     * Gibt das WorkflowDecision-Objekten wieder, welches mit der übergeben ID übereinstimmt.
     *
     * @param workflowDecisionId Die id des WorkflowDecision
     * @return das WorkflowDecision-Objekten
     * @throws WorkflowItemNotFoundException
     */
    public WorkflowDecision getWorkflowDecision(int workflowDecisionId) throws WorkflowItemNotFoundException;
}