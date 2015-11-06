package de.teamrocket.relaxo.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.RelaxoModule;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.persistence.services.JobService;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.persistence.services.UserManagementService;
import de.teamrocket.relaxo.persistence.services.WorkflowService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Created by mobbitz on 18.01.15.
 */
public class JobControllerTest {
    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script_for_tests.sql";
    
    static final Logger logger = Logger.getLogger("TestLogger");
    static JobController jobController;
    static WorkflowService workflowService;
    static JobService jobService;
    static UserManagementService userManagementService;
    static Job job;

    @BeforeClass
    public static void setup() {
    	SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.executeSqlScript(SQL_CREATE);
        sqlExecutor.executeSqlScript(SQL_VIEWS);
        sqlExecutor.executeSqlScript(SQL_TESTDATA);
        Injector injector = Guice.createInjector(new ServiceModule(), new RelaxoModule());
        jobController = injector.getInstance(JobController.class);
        workflowService = injector.getInstance(WorkflowService.class);
        jobService = injector.getInstance(JobService.class);
        userManagementService = injector.getInstance(UserManagementService.class);
        job = new Job();
        job.setCreatorId(1);
        job.setWorkflowId(1);
        jobService.createJob(job);
    }

    @AfterClass
    public static void teardown() {
        jobService.deleteJob(job.getId());
    }

    @Test
    public void testGetAllJobs() {
        List<Job> jobs = new LinkedList<>();
        jobs = jobController.getAllJobs();
        assertNotNull(jobs.get(0));
    }

    @Test
    public void testGetCurrentJobWorkflowItemsForJob() {
        List<JobWorkflowItem> jobWorkflowItems = new LinkedList<>();
        JobWorkflowItem jobWorkflowItem = new JobWorkflowItem();
        jobWorkflowItem.setJobId(job.getId());
        jobWorkflowItem.setWorkflowItemId(1);
        jobService.createJobWorkflowItem(jobWorkflowItem);
        jobWorkflowItems = jobController.getCurrentJobWorkflowItemsForJob(job);
        assertNotNull(jobWorkflowItems.get(0));
    }

    @Test
    public void testMoveNewJobToFirstWorkflowItem() {
        User user = userManagementService.selectUserById(1);
        Workflow workflow = workflowService.getWorkflowById(1);
        WorkflowItem workflowItem = jobController.moveNewJobToFirstWorkflowItem(workflow, user, job);
        assertNotNull(workflowItem);
    }

    @Test(expected = RuntimeException.class)
    public void testMoveNewJobToFirstWorkflowItemException() {
        User user = userManagementService.selectUserById(1);
        jobController.moveNewJobToFirstWorkflowItem(new Workflow(), user, job);
    }

}
