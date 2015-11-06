package de.teamrocket.relaxo.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.JoinBranch;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;

/**
 * Interface fuer Job-Statements an die Datenbank
 */
public interface JobMapper {
    public void createJob(Job job);

    public void deleteJob(int jobId);

    public Job getJobById(int jobId);

    public List<JobTask> getJobTasks(int jobId);

    public Integer getSecondItemId(@Param("userId") int userId, @Param("workflowId") int workflowId);

    public List<Job> getJobsForTask(int taskId);

    public List<Job> getJobsForWorkflow(Workflow workflow);

    public void createJobWorkflowItem(JobWorkflowItem jobWorkflowItem);

    public JobWorkflowItem getJobWorkflowItemById(int id);

    public List<JobWorkflowItem> getAllJobWorkflowItemsForJob(int jobId);

    public void deleteJobWorkflowItem(int jobWorkflowItemId);

    public void insertJobTask(JobTask jobTask);

    public void insertJoinBranch(JoinBranch joinBranch);
    
    public JoinBranch getActiveJoinBranch(@Param("jobId") int jobId, @Param("workflowJoinId") int workflowJoinId, @Param("prevWorkflowItemId") int prevWorkflowItemId);
    
    public List<JobTask> getJobTasksForJobAndTask(@Param("jobId") int jobId, @Param("taskId") int taskId);

    public JobTask getActiveJobTaskForJobAndTask(@Param("jobId") int jobId, @Param("taskId") int taskId);

    public JobTaskComponent getActiveJobTaskComponent(@Param("jobId") int jobId, @Param("taskId") int taskId, @Param("taskComponentId") int taskComponentId);

    public JobTaskComponent getInactiveJobTaskComponent(@Param("jobId") int jobId, @Param("taskId") int taskId, @Param("taskComponentId") int taskComponentId);

    public void createJobTaskComponents(@Param("taskComponents") List<TaskComponent> taskComponents, @Param("jobTask") JobTask jobTask);

    public void updateJobTaskComponent(JobTaskComponent jobTaskComponent);

    public void finishJob(int jobId);

    public void finishJobTask(@Param("jobId") int jobId, @Param("taskId") int taskId, @Param("editorUserId") int editorUserId);

    public void finishJobWorkflowItem(int jobWorkflowItemId);

    public boolean isUserAbleToStartJobsForWorkflow(@Param("workflow") Workflow workflow, @Param("user") User user);

    public void lockJobTask(@Param("jobTaskId")int jobTaskId, @Param("blockerUserId")int blockerUserId);

    public void unlockJobTask(int jobTaskId);
}
