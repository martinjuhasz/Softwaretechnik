package de.teamrocket.relaxo.persistence.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.guice.transactional.Transactional;

import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.exceptions.JobTaskLockException;
import de.teamrocket.relaxo.controller.exceptions.JobTaskNotFoundException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.JoinBranch;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponentUpdateVisitor;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponentUpdateVisitorFactory;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobUpdateComponent;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.mapper.JobMapper;

/**
 * Service-Klasse zum Persistieren von Job Daten
 */
public class JobService {
    /**
     * JobMapper-Interface
     */
    @Inject
    JobMapper jobMapper;

    /**
     * Instanz der VisitorFactory
     */
    @Inject
    JobTaskComponentUpdateVisitorFactory jobTaskComponentUpdateVisitorFactory;

    /**
     * Gibt einen Job an Hand seiner ID zurueck
     * @param jobId die uebergebene ID des Job
     * @return der gesuchte Job
     */
    @Transactional
    public Job getJobById(int jobId) {
        return jobMapper.getJobById(jobId);
    }

    /**
     * Erstellen eines Job in der Datenbank
     * @param job der uebergebene Job
     */
    @Transactional
    public void createJob(Job job) {
        jobMapper.createJob(job);
    }

    /**
     * Loeschen eines Job an Hand seiner ID
     * @param jobId die uebergebene ID des Job
     */
    @Transactional
    public void deleteJob(int jobId) {
        jobMapper.deleteJob(jobId);
    }

    /**
     * Gibt eine Liste von JobTasks fuer einen Job zurueck
     * @param jobId die uebergebene ID des Job
     * @return Liste der JobTasks eines Jobs
     */
    @Transactional
    public List<JobTask> getJobTasks(int jobId) {
        return jobMapper.getJobTasks(jobId);
    }

    /**
     * Gibt eine Liste von JobTasks selektiert nach Job und Task zurueck
     * @param jobId die uebergebene ID des Job
     * @param taskId die uebergebene ID des Task
     * @return Liste der JobTasks selektiert nach Job und Task
     */
    @Transactional
    public List<JobTask> getJobTasksForJobAndTask(int jobId, int taskId) {
        return jobMapper.getJobTasksForJobAndTask(jobId, taskId);
    }

    /**
     * Überprüft, ob das Workflow Item nach dem Start Item eines Workflows vom Typ Task ist.
     * Überprüft anschließend, ob User zugriff auf dieses Item hat
     *
     * @param userId     anfragender User
     * @param workflowId betroffener Workflow
     * @return Die Task ID des zweiten Items, NULL falls kein Zugriff oder anderer Workflow-Item Typ
     */
    @Transactional
    public Integer getSecondItemId(int userId, int workflowId) {
        return jobMapper.getSecondItemId(userId, workflowId);
    }

    /**
     * Gibt eine Map von Jobs mit locked Status fuer einen Task zurueck
     * @param taskId die uebergebene ID des Task
     * @return eine Map von Jobs mit locked Status
     */
    @Transactional
    public Map<Job, Boolean> getJobsForTask(int taskId) {
        List<Job> jobs = jobMapper.getJobsForTask(taskId);
        Map<Job, Boolean> jobAtTaskLocked = new HashMap<>();

        JobTask jobTask;
        boolean locked;
        for(Job job : jobs){
            jobTask = jobMapper.getActiveJobTaskForJobAndTask(job.getId(), taskId);
            locked = false;
            if(jobTask.getBlockerUserId() != null){
                locked = true;
            }
            jobAtTaskLocked.put(job, locked);
        }

        return jobAtTaskLocked;
    }

    /**
     * Gibt alle aktiven Jobs des uebergebenen Workflows zurueck
     *
     * @param workflow der Workflow dessen aktive Jobs gesucht sind
     * @return die aktiven Jobs eines Workflows
     */
    @Transactional
    public List<Job> getJobsForWorkflow(Workflow workflow) {
        return jobMapper.getJobsForWorkflow(workflow);
    }

    /**
     * Erstellen eines JobWorkflowItems in der Datenbank
     * @param jobWorkflowItem das uebergebene JobWorkflowItem
     */
    @Transactional
    public void createJobWorkflowItem(JobWorkflowItem jobWorkflowItem) {
        jobMapper.createJobWorkflowItem(jobWorkflowItem);
    }

    /**
     * Gibt ein JobWorkflowItem an Hand seiner ID zurueck
     * @param jobWorkflowItemId die uebergeben ID des JobWorkflowItems
     * @return das gesuchte JobWorkflowItem
     */
    @Transactional
    public JobWorkflowItem getJobWorkflowItemById(int jobWorkflowItemId) {
        return jobMapper.getJobWorkflowItemById(jobWorkflowItemId);
    }

    /**
     * Gibt eine Liste von JobWorkflowItems eines Jobs zurueck
     * @param jobId die uebergebene ID des Job
     * @return eine Liste von JobWorkflowItems eines Jobs
     */
    public List<JobWorkflowItem> getAllJobWorkflowItemsForJob(int jobId) {
        return jobMapper.getAllJobWorkflowItemsForJob(jobId);
    }

    /**
     * Loeschen eines JobWorkflowItems an Hand seiner ID
     * @param jobWorkflowItemId die uebergebene ID des JobWorkflowItems
     */
    @Transactional
    public void deleteJobWorkflowItem(int jobWorkflowItemId) {
        jobMapper.deleteJobWorkflowItem(jobWorkflowItemId);
    }

    /**
     * Erstellen eines JobTask in der Datenbank
     * @param jobTask der uebergebene JobTask
     */
    @Transactional
    public void insertJobTask(JobTask jobTask) {
        jobMapper.insertJobTask(jobTask);
    }

    /**
     * Erstellen eines JoinBranch in der Datenbank
     * @param joinBranch das uebergebene JoinBranch
     */
    @Transactional
    public void insertJoinBranch(JoinBranch joinBranch){
    	jobMapper.insertJoinBranch(joinBranch);
    }

    /**
     * Gibt das aktive JoinBranch zurueck
     * @param jobId die uebergebene ID des Jobs
     * @param workflowJoinId die uebergebene ID des Joins
     * @param prevWorkflowItemId die uebergebene ID des vorherigen WorkflowItems
     * @return das gesuchte JoinBranch
     */
    @Transactional
    public JoinBranch getActiveJoinBranch(int jobId, int workflowJoinId, int prevWorkflowItemId){
    	return jobMapper.getActiveJoinBranch(jobId, workflowJoinId, prevWorkflowItemId);
    }

    /**
     * Erstellt mehrere TaskComponents fuer einen JobTask in der Datenbank
     * @param taskComponents die uebergebene Liste der TaskComponents
     * @param jobTask der uebergebene JobTask
     */
    @Transactional
    public void createJobTaskComponents(List<TaskComponent> taskComponents, JobTask jobTask) {
        jobMapper.createJobTaskComponents(taskComponents, jobTask);
    }

    /**
     * Gibt die aktive JobTaskComponent selektiert nach Job, Task und Component zurueck
     * @param jobId die uebergebene ID des Job
     * @param taskId die uebergebene ID des Task
     * @param taskComponentId die uebergebene ID der TaskComponent
     * @return die aktive JobTaskComponent
     */
    @Transactional
    public JobTaskComponent getActiveJobTaskComponent(int jobId, int taskId, int taskComponentId) {
        return jobMapper.getActiveJobTaskComponent(jobId, taskId, taskComponentId);
    }

    /**
     * Gibt die inaktive JobTaskComponent selektiert nach Job, Task und Component zurueck
     * @param jobId die uebergebene ID des Job
     * @param taskId die uebergebene ID des Task
     * @param taskComponentId die uebergebene ID der TaskComponent
     * @return die inaktive JobTaskComponent
     */
    @Transactional
    public JobTaskComponent getInactiveJobTaskComponent(int jobId, int taskId, int taskComponentId) {
        return jobMapper.getInactiveJobTaskComponent(jobId, taskId, taskComponentId);
    }

    /**
     * Update einer JobTaskComponent in der Datenbank
     * @param jobTaskComponent die uebergebene JobTaskComponent
     */
    @Transactional
    public void updateJobTaskComponent(JobTaskComponent jobTaskComponent) {
        jobMapper.updateJobTaskComponent(jobTaskComponent);
    }

    /**
     * Beenden eines Jobs in der Datenbank
     * @param jobId die uebergebene ID das Job
     */
    @Transactional
    public void finishJob(int jobId) {
        jobMapper.finishJob(jobId);
    }

    /**
     * Beenden eines JobTask in der Datenbank
     * @param jobId die uebergebene ID das Job
     * @param taskId die uebergebene ID des Task
     * @param editorUserId die uebergebene ID das User der editiert
     */
    @Transactional
    public void finishJobTask(int jobId, int taskId, int editorUserId) {
        jobMapper.finishJobTask(jobId, taskId, editorUserId);
    }

    /**
     * Erstellen eines JobTask in der Datenbank
     * @param task der uebergebene Task
     * @param jobId die uebergebene ID des Job
     */
    @Transactional
    public void createJobTask(Task task, int jobId) {
        JobTask jobTask = new JobTask();
        jobTask.setWorkflowItemId(task.getId());
        jobTask.setJobId(jobId);
        jobMapper.createJobWorkflowItem(jobTask);
        jobMapper.insertJobTask(jobTask);
        jobMapper.createJobTaskComponents(task.getTaskComponents(), jobTask);
    }

    /**
     * Beenden eines JobTask in der Datenbank
     * @param task der uebergebene Task
     * @param jobId die uebergebene ID des Job
     * @param components eine Liste von JobTaskComponents die geupdated werden
     * @param editorUserId die uebergebene ID das User der editiert
     */
    @Transactional
    public void finishJobTask(Task task, int jobId, List<JobUpdateComponent> components, int editorUserId) {
        JobTaskComponentUpdateVisitor jobTaskComponentUpdateVisitor = jobTaskComponentUpdateVisitorFactory.create();
        JobTaskComponent jobTaskComponent;
        for (JobUpdateComponent jobUpdateComponent : components) {
            jobTaskComponent = jobMapper.getActiveJobTaskComponent(jobId, task.getId(), jobUpdateComponent.getTaskComponentId());
            jobTaskComponentUpdateVisitor.setNewValue(jobUpdateComponent.getValue());
            jobTaskComponent.accept(jobTaskComponentUpdateVisitor);
            jobMapper.updateJobTaskComponent(jobTaskComponent);
        }

        jobMapper.finishJobTask(jobId, task.getId(), editorUserId);
    }

    /**
     * Beenden eines JobWorkflowItem in der Datenbank
     * @param jobWorkflowItemId die uebergebene ID des JobWorkflowItem
     */
    @Transactional
    public void finishJobWorkflowItem(int jobWorkflowItemId) {
        jobMapper.finishJobWorkflowItem(jobWorkflowItemId);
    }

    /**
     * Gibt zurueck ob ein User in einem bestimmten Workflow einen Job starten kann
     *
     * @param workflow der uebergebene Workflow
     * @param user     der uebergebene User
     * @return TRUE wenn der User Jobs starten kann, wenn nicht FALSE
     */
    @Transactional
    public boolean isUserAbleToStartJobsForWorkflow(Workflow workflow, User user) {
        return jobMapper.isUserAbleToStartJobsForWorkflow(workflow, user);
    }

    /**
     * Locken eines JobTask durch einen User
     * @param taskId die uebergebene ID das Task
     * @param jobId die uebergebene ID das Job
     * @param userId die uebergebene ID des Users
     * @throws JobTaskNotFoundException
     * @throws JobTaskLockException
     */
    @Transactional
    public void lockJobTask(int taskId, int jobId, int userId) throws JobTaskNotFoundException, JobTaskLockException {
        JobTask jobTask = jobMapper.getActiveJobTaskForJobAndTask(jobId, taskId);
        if(jobTask == null){
            throw new JobTaskNotFoundException("JobTask doesn't exist!");
        }
        if(jobTask.getBlockerUserId() != null){
            throw new JobTaskLockException("JobTask already locked by userId: " + jobTask.getBlockerUserId());
        }
        jobMapper.lockJobTask(jobTask.getId(), userId);
    }

    /**
     * Unlocken eines JobTask durch einen User
     * @param taskId die uebergebene ID das Task
     * @param jobId die uebergebene ID das Job
     * @param userId die uebergebene ID des Users
     * @throws JobTaskNotFoundException
     * @throws JobTaskLockException
     */
    @Transactional
    public void unlockJobTask(int taskId, int jobId, int userId) throws JobTaskNotFoundException, JobTaskLockException {
        JobTask jobTask = jobMapper.getActiveJobTaskForJobAndTask(jobId, taskId);
        if(jobTask == null) {
            throw new JobTaskNotFoundException("JobTask doesn't exist!");
        }
        if(jobTask.getBlockerUserId() == null){
            throw new JobTaskLockException("JobTask already unlocked!");
        }
        if(userId != jobTask.getBlockerUserId()){
            throw new JobTaskLockException("JobTask was locked by different user (" + jobTask.getBlockerUserId() + ")");
        }
        jobMapper.unlockJobTask(jobTask.getId());
    }
}
