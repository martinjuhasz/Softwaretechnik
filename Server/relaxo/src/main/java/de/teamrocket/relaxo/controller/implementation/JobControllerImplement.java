package de.teamrocket.relaxo.controller.implementation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.JobController;
import de.teamrocket.relaxo.controller.commands.TaskUpdateCommand;
import de.teamrocket.relaxo.controller.commands.WorkflowItemCommand;
import de.teamrocket.relaxo.controller.commands.WorkflowItemCommandFactory;
import de.teamrocket.relaxo.controller.exceptions.JobTaskLockException;
import de.teamrocket.relaxo.controller.exceptions.JobTaskNotFoundException;
import de.teamrocket.relaxo.events.models.TaskUpdateEvent;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobUpdateComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Start;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.persistence.services.JobService;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;
import de.teamrocket.relaxo.persistence.services.WorkflowService;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

/**
 * Implementierung des Job-Controllers.
 */
public class JobControllerImplement implements JobController {

    // STATIC

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.CONTROLLER);

	// VARS

    /**
     * Instanz des JobService.
     */
    private final JobService jobService;

    /**
     * Instanz des WorkflowService.
     */
    private final WorkflowService workflowService;

    /**
     * Instanz des WorkflowItemService.
     */
    private final WorkflowItemService workflowItemService;

    /**
     * Instanz des WorkflowItemCommandFactory zum instanziieren von Commands.
     */
    private final WorkflowItemCommandFactory workflowItemCommandFactory;

    /**
     *  Instanz des Eventsbusses.
     */
    private final EventBus eventBus;

	// CONSTRUCT

    @Inject
    public JobControllerImplement(JobService jobService, WorkflowItemService workflowItemService, WorkflowItemCommandFactory workflowItemCommandFactory, WorkflowService workflowService, EventBus eventBus) {
        this.jobService = jobService;
        this.workflowItemService = workflowItemService;
        this.workflowItemCommandFactory = workflowItemCommandFactory;
        this.workflowService = workflowService;
        this.eventBus = eventBus;
    }

	// METHODS

    @Override
    public List<Job> getJobsForWorkflow(Workflow workflow) {
        return jobService.getJobsForWorkflow(workflow);
    }

    @Override
    public Job getJob(int jobId) {
        return jobService.getJobById(jobId);
    }

    @Override
    public Map<Job, Boolean> getJobsForTask(Task task) {
        return jobService.getJobsForTask(task.getId());
    }

    @Override
    public List<JobTask> getJobTasks(int jobId) {
        return jobService.getJobTasks(jobId);
    }

    @Override
    public List<JobTask> getJobTasks(int jobId, int taskId) {
        return jobService.getJobTasksForJobAndTask(jobId, taskId);
    }

    @Override
    public void updateJobTask(int jobId, int taskId, List<JobUpdateComponent> components, int editorUserId) {
        Task task = workflowItemService.getTaskById(taskId);
        TaskUpdateCommand taskUpdateCommand = workflowItemCommandFactory.createTaskUpdateCommand(task, jobId, components, editorUserId);
        taskUpdateCommand.execute();

        LOGGER.info("Ein JobTask vom Job mit der ID: "+jobId+" und der Task-ID: "+taskId+" wurde von dem User mit der ID: "+editorUserId+" geupdatet.");
    }

    @Override
    public boolean isUserAbleToStartJobsForWorkflow(Workflow workflow, User user) {
        return jobService.isUserAbleToStartJobsForWorkflow(workflow, user);
    }

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = new LinkedList<>();

        for (Workflow workflow : workflowService.getAllWorkflows()) {
            for (Job job : jobService.getJobsForWorkflow(workflow)) {
                jobs.add(job);
            }
        }

        return jobs;
    }

    @Override
    public boolean isJobRunning(Job job) {
        return !getCurrentJobWorkflowItemsForJob(job).isEmpty();
    }

    @Override
    public List<JobWorkflowItem> getCurrentJobWorkflowItemsForJob(Job job) {
        List<JobWorkflowItem> returnList = new LinkedList<>();

        for (JobWorkflowItem jobWorkflowItem : jobService.getAllJobWorkflowItemsForJob(job.getId())) {
            if (!jobWorkflowItem.isDone()) {
                returnList.add(jobWorkflowItem);
            }
        }

        return returnList;
    }

    @Override
    public List<JobWorkflowItem> getAllJobWorkflowItemsForJob(Job job) {
        return jobService.getAllJobWorkflowItemsForJob(job.getId());
    }

    @Override
    public Job createJob(Workflow workflow, User user) {
        Job job = new Job();
        job.setWorkflowId(workflow.getId());
        job.setCreatorId(user.getId());

        jobService.createJob(job);

        LOGGER.info("Ein Job, von dem Workflow: "+workflow.getId()+" mit dem Namen: "+workflow.getName()+" wurde von dem User "+user.getName()+" mit der ID: "+user.getId()+" erstellt.");

        return job;
    }

    @Override
    public WorkflowItem moveNewJobToFirstWorkflowItem(Workflow workflow, User user, Job job) {

        // hole Start-Item und rücke Job vor
        Start start = (Start) workflowItemService.getWorkflowItemById(workflow.getStartItemId());
        LOGGER.info(start.getName());

        WorkflowItem workflowItem = workflowItemService.getWorkflowItemById(start.getNextWorkflowItemId());
        WorkflowItemCommand workflowItemCommand = workflowItemCommandFactory.createWorkflowItemEnterCommand(workflowItem, start, job.getId());

        workflowItemCommand.execute();

        if (workflowItemService.canUserAccessWorkflowItem(workflowItem.getId(), user.getId())) {
            return workflowItem;
        }
        // Wenn User keine Rechte hat Workflow-Item zu sehen -> null
        return null;
    }

    @Override
    public boolean deleteJob(Job job) {
        LOGGER.info("Ein Job mit der ID: "+job.getId()+" wurde entfernt.");
        jobService.deleteJob(job.getId());
        return true;
    }


    @Override
    public void lockJobTask(int taskId, int jobId, int userId) throws JobTaskLockException, JobTaskNotFoundException {
        jobService.lockJobTask(taskId, jobId, userId);

        Task task = workflowItemService.getTaskById(taskId);
        eventBus.post(new TaskUpdateEvent(task));
    }

    @Override
    public void unlockJobTask(int taskId, int jobId, int userId) throws JobTaskLockException, JobTaskNotFoundException {
        jobService.unlockJobTask(taskId, jobId, userId);

        Task task = workflowItemService.getTaskById(taskId);
        eventBus.post(new TaskUpdateEvent(task));
    }
}
