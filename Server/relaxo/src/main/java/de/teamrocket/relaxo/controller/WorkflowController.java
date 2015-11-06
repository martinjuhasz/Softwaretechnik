package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.controller.exceptions.WorkflowLockException;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;

import java.util.List;

/**
 * Controller Interface fuer Workflow
 */
public interface WorkflowController {

    /**
     * Erstellt einen neuen Workflow
     *
     * @param user Ersteller
     * @param name Workflowtitel
     * @return der erstellte Workflow
     */
    public Workflow createWorkflow(User user, String name);

    /**
     * Update des uebergebenen Workflows
     *
     * @param workflow der uebergebene Workflow
     */
    public void updateWorkflow(Workflow workflow);

    /**
     * Gibt einen Workflow mit der uebergebenen ID zurueck
     *
     * @param id die ID des gesuchten Workflows
     * @return der gesuchte Workflow
     */
    public Workflow getWorkflowById(int id);

    /**
     * Gibt eine Liste mit Workflows des uebergebenen User zurueck
     *
     * @param user der User dessen Workflows abgefagt werden
     * @return die Liste der Workflows eines User
     */
    public List<Workflow> getWorkflowsForUser(User user);

    /**
     * Gibt alle vorhandenen Workflows zurueck
     *
     * @return alle vorhandenen Workflows
     */
    public List<Workflow> getAllWorkflows();

    /**
     * Loescht den uebergeben Workflow
     *
     * @param workflow der zu loeschende Workflow
     */
    public void deleteWorkflow(Workflow workflow);

    /**
     * Prueft ob der uebergebene Workflow aktive Jobs hat
     *
     * @param workflow der uebergebene Workflow
     * @return TRUE wenn der Workflow noch aktive Jobs hat, FALSE wenn nicht
     */
    public boolean hasActiveJobs(Workflow workflow);

    /**
     * Gibt zurueck ob der uebergebene User den Workflow sehen kann
     *
     * @param workflow der uebergebene Workflow
     * @param user     der uebergebene User
     * @return TRUE wenn der User den Workflow sehen kann, wenn nicht FALSE
     */
    public boolean isUserAbleToSeeWorkflow(Workflow workflow, User user);

    /**
     * Liefert true, falls dieser User berechtigt ist, einen Workflow zu erstellen.
     *
     * @param user Der zu prüfende User.
     * @return True, falls der user ein Workflow anlegen darf.
     */
    public boolean isUserAbleToCreateAnWorkflow(User user);

    /**
     * Liefer true, wenn das Workflow mit der übergebenen id exisitert.
     *
     * @param workflowId Die zu prüfende id.
     * @return True, wenn es exisitert.
     */
    public boolean workflowExists(int workflowId);

    /**
     * Aendert das runnable Flag eines Workflows
     *
     * @param runnable Wert des Flags true/false
     * @param workflow der Workflow
     * @param user     der Bearbeiter
     */
    public void updateRunnableStateForWorkflow(Workflow workflow, boolean runnable, User user) throws WorkflowLockException;
}
