package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.controller.exceptions.JobTaskLockException;
import de.teamrocket.relaxo.controller.exceptions.JobTaskNotFoundException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobUpdateComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;

import java.util.List;
import java.util.Map;

/**
 * Controller Interface fuer Job
 */
public interface JobController {

    /**
     * Gibt alle aktiven Jobs eines Workflows zurueck
     *
     * @param workflow der uebergebene Workflow
     * @return Liste aller aktiven Jobs des Workflows
     */
    public List<Job> getJobsForWorkflow(Workflow workflow);

    /**
     * Liefer den Job mit der uebergebenen jobId
     *
     * @param jobId die ID des Jobs
     * @return Der Job, wenn er existiert, null wenn nicht
     */
    public Job getJob(int jobId);

    /**
     * Liefert alle Jobs eines Tasks
     *
     * @param task der Task
     * @return Liste alles Jobs
     */
    public Map<Job, Boolean> getJobsForTask(Task task);

    /**
     * Liefert alle JobTasks des Jobs mit der übergebenen jobId
     *
     * @param jobId die Id des Jobs
     * @return Liste aller JobTasks des Jobs und des Tasks
     */
    public List<JobTask> getJobTasks(int jobId);


    /**
     * Liefert alle JobTasks des Jobs mit der uebergebenen jobId zu dem uebergebeben Task
     *
     * @param jobId  die Id des Jobs
     * @param taskId die Id des Tasks
     * @return Liste aller JobTasks des Jobs und des Tasks
     */
    public List<JobTask> getJobTasks(int jobId, int taskId);

    /**
     * Aktuallisiert die JobComponente eines JobTask, welche auf ein Task zeigen, der die taskId hat und dem Job
     * mit der übergebenden jobId gehört.
     *
     * @param jobId        die Job-ID
     * @param taskId       die Task-ID
     * @param components   die zu hinzufügenden Componenten
     * @param editorUserId der User, der dies aktualisiert
     */
    public void updateJobTask(int jobId, int taskId, List<JobUpdateComponent> components, int editorUserId);

    /**
     * Gibt zurueck ob ein User in einem bestimmten Workflow Jobs starten kann
     *
     * @param workflow der uebergebene Workflow
     * @param user     der uebergebene User
     * @return TRUE wenn der User einen Job starten kann, wenn nicht FALSE
     */
    public boolean isUserAbleToStartJobsForWorkflow(Workflow workflow, User user);

    /**
     * Gibt alle Jobs wieder, die existieren.
     *
     * @return Eine Liste mit allen Jobs.
     */
    public List<Job> getAllJobs();

    /**
     * Prüft, ob ein Job noch läuft. Ein Job läuft dann noch, wenn dieser noch CurrentJobWorkflowItems besitzt.
     *
     * @param job Der zu prüfende Job
     * @return true, falls der Job noch läuft, sonst false.
     */
    public boolean isJobRunning(Job job);

    /**
     * Gibt alle JobWorkflowItems wieder, die dem übergebenden Job angehören und noch nicht done sind.
     *
     * @param job Der zu prüfende Job
     * @return Liste mit den JobWorkflowItems, die nicht done sind
     */
    public List<JobWorkflowItem> getCurrentJobWorkflowItemsForJob(Job job);

    /**
     * Gibt alle JobWorkflowItems wieder, die dem übergebenden Job angehören.
     *
     * @param job Der zu prüfende Job
     * @return Liste mit den JobWorkflowItems
     */
    public List<JobWorkflowItem> getAllJobWorkflowItemsForJob(Job job);

    /**
     * Erstellt einen Job aus dem übergebenen Job (ohne id).
     *
     * @param workflow Workflow, in dem der Job erstellt werden soll
     * @param user     User, der den Job erstellt
     * @return gibt den erzeugen Job mit Id zurück
     */
    public Job createJob(Workflow workflow, User user);

    /**
     * Verschiebt einen neuen Job zum nächsten WorkflowItem. Prüft gleichzeitig, ob der User
     * das nächste Item sehen kann.
     *
     * @param workflow aktueller Workflow
     * @param user     aktueller User
     * @param job      Job, der verschoben werden soll
     * @return WorkflowItem, wenn User Item sehen kann. Ansonsten null
     */
    public WorkflowItem moveNewJobToFirstWorkflowItem(Workflow workflow, User user, Job job);

    /**
     * Entfernt ein Job.
     *
     * @param job der zu löschende Job
     * @return true, bei Erfolg.
     */
    public boolean deleteJob(Job job);

    /**
     * Sperrt einen JobTask, wenn ein User im Client diesen geöffnet hat. Verhindert,
     * dass zwei User gleichzeitig den selben JobTask bearbeiten
     * @param taskId von dem Task um den es sich handelt
     * @param jobId in der sich der JobTask befindet
     * @param userId des Benutzers, der das Item sperrt
     * @throws JobTaskLockException wenn JobTask bereits gesperrt ist
     * @throws JobTaskNotFoundException wenn JobTask nicht existiert
     */
    public void lockJobTask(int taskId, int jobId, int userId) throws JobTaskLockException, JobTaskNotFoundException;

    /**
     * Entsperrt ein JobTask, sobald ein User in nicht mehr bearbeitet.
     * @param taskId von dem Task um den es sich handelt
     * @param jobId in der sich der JobTask befindet
     * @param userId des Benutzers, der das Item entsperrt
     * @throws JobTaskLockException wenn JobTask bereits entsperrt ist
     * @throws JobTaskNotFoundException wenn JobTask nicht existiert
     */
    public void unlockJobTask(int taskId, int jobId, int userId) throws JobTaskLockException, JobTaskNotFoundException;
}
