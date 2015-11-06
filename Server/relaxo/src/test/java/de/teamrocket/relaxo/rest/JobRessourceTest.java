package de.teamrocket.relaxo.rest;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.teamrocket.relaxo.controller.exceptions.DuplicateException;
import de.teamrocket.relaxo.controller.exceptions.NextWorkflowItemException;
import de.teamrocket.relaxo.controller.exceptions.NotNullException;
import de.teamrocket.relaxo.controller.exceptions.UserGroupNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemTypeException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowNotEditableException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowStartItemExistException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.models.workflow.End;
import de.teamrocket.relaxo.models.workflow.Start;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.exceptions.WorkflowItemConnectionException;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

/**
 * Testet die Ressource Job der REST-Schnittstelle
 */
public class JobRessourceTest extends RessourceTest {

    static final RelaxoLogger logger = new RelaxoLogger(RelaxoLoggerType.TEST);
    private static String testUserToken;
    private static String gehaltsUserToken;
    private static String adminUserToken;

    private static User testUser;
    private static User gehaltsUser;
    private static User adminUser;

    private static UserGroup testGroup;
    private static List<UserGroup> groups;

    private static Start startTask;
    private static End endTask;
    private static Task testTask;
    private static Workflow workflow;

    private static Job job, old_job;


    /**
     * Setup a logged in user for testing
     */
    @BeforeClass
    public static void setUpWorkflowTest() throws WorkflowStartItemExistException, WorkflowItemTypeException, NotNullException, DuplicateException, UserNotFoundException, UserGroupNotFoundException {


        // Benutzer erstellen
        testUser = new User();
        testUser.setName("__testuser_testUser");
        testUser.setPrename("__testuser_testUser");
        testUser.setUsername("__testuser_testUser");
        testUser.setPassword("123456");
        testUser.setAdmin(true);
        testUser.setActive(true);
        try {
            userManagementController.createUser(testUser);
        } catch (Exception e) {
            testUser = userManagementController.getUserByUsername("__testuser_testUser");
        }


        testGroup = new UserGroup();
        testGroup.setName("__testgroup");
        try {
            userManagementController.createUserGroup(testGroup);
        } catch (NotNullException e) {
            logger.warning(e.getMessage());
        } catch (DuplicateException e) {
            logger.warning(e.getMessage());
        }

        try {
            userManagementController.addUserToGroup(testUser.getId(),testGroup.getId());
        } catch (UserNotFoundException e) {
            logger.warning(e.getMessage());
        } catch (UserGroupNotFoundException e) {
            logger.warning(e.getMessage());
        }

        groups = userManagementController.getAllGroups();
        List<Integer> groupIds = new LinkedList<>();
        for(UserGroup ug : groups){
            groupIds.add(ug.getId());
        }

        workflow = workflowController.createWorkflow(testUser, "Testworkflow");

        workflow.setName("__testWorkflow");

        startTask = (Start) workflowItemController.createWorkflowItem(workflow, "START", 0, 0, null);
        testTask = (Task) workflowItemController.createWorkflowItem(workflow, "TASK", 0, 0, null);
        endTask = (End) workflowItemController.createWorkflowItem(workflow, "END", 0, 0, null);

        workflowItemController.setUserGroupsForWorkflowItem(startTask,groupIds);
        workflowItemController.setUserGroupsForWorkflowItem(testTask,groupIds);
        workflowItemController.setUserGroupsForWorkflowItem(endTask,groupIds);


        workflow.setStartItemId(startTask.getId());
        workflowController.updateWorkflow(workflow);

        List<Integer> nextWFIs = new LinkedList<>();
        nextWFIs.add(testTask.getId());

        try {
            workflowItemController.setNextWorkflowItems(startTask.getId(), nextWFIs,testUser);
        } catch (NextWorkflowItemException e) {
            logger.warning(e.getMessage());
        } catch (WorkflowItemNotFoundException e) {
            logger.warning(e.getMessage());
        } catch (WorkflowItemConnectionException e) {
            logger.warning(e.getMessage());
        }

        nextWFIs = new LinkedList<>();
        nextWFIs.add(endTask.getId());

        try {
            workflowItemController.setNextWorkflowItems(testTask.getId(), nextWFIs, testUser);
        } catch (NextWorkflowItemException e) {
            logger.warning(e.getMessage());
        } catch (WorkflowItemNotFoundException e) {
            logger.warning(e.getMessage());
        } catch (WorkflowItemConnectionException e) {
            logger.warning(e.getMessage());
        }


        job = jobController.createJob(workflow, testUser);
        logger.info(""+job.getId()+" "+job.getCreatorId()+" "+job.getStartTime()+" "+job.getWorkflowId());
        jobController.moveNewJobToFirstWorkflowItem(workflow, testUser, job);

        testUserToken = userManagementController.createTokenForUser(testUser);
    }

    @AfterClass
    public static void shutdownTest() {


        try {
            workflowItemController.deleteWorkflowItem(endTask.getId(),testUser);
            workflowItemController.deleteWorkflowItem(startTask.getId(),testUser);
            workflowItemController.deleteWorkflowItem(testTask.getId(),testUser);
        } catch (WorkflowNotEditableException e) {
            logger.warning(e.getMessage());
        } catch (WorkflowItemNotFoundException e) {
            logger.warning(e.getMessage());
        }
        userManagementController.removeUserFromUserGroup(testUser.getId(), testGroup.getId());
        workflowController.deleteWorkflow(workflow);
        userManagementController.deleteUser(testUser);
        userManagementController.deleteGroup(testGroup);

    }

    /**
     * Testen ob ein eingeloggter User einen Job zurückgeliefert bekommt
     */
    @Test
    public void testJobDetails() {

        Response response = client
                .target("http://localhost:8081")
                .path("jobs/" + job.getId())
                .request(MediaType.APPLICATION_JSON)
                .header("Token", testUserToken)
                .get();

        Assert.assertTrue(response.getStatus() == 200);
/*
        RestEnvelope<JobResponse> jobResponse = response.readEntity(new GenericType<RestEnvelope<JobResponse>>() {});

        Assert.assertTrue(jobResponse.getData().getCreator().equals("testuser"));*/
    }

    /**
     * Testen ob ein eingeloggter User einen Job zurückgeliefert bekommt
     */
    @Test
    public void testJobDetailsRights() {


        Response response = client
                .target("http://localhost:8081")
                .path("jobs/" + job.getId())
                .request(MediaType.APPLICATION_JSON)
                .header("Token", testUserToken)
                .get();

        System.out.println(response.getStatus());

        Assert.assertTrue(response.getStatus() == 200);
    }

    /**
     * Testen ob ein Admin einen Job zurückgeliefert bekommt
     */
    @Test
    public void testJobDetailsRightsAdmin() {
        Response response = client
                .target("http://localhost:8081")
                .path("jobs/" + job.getId())
                .request(MediaType.APPLICATION_JSON)
                .header("Token", testUserToken)
                .get();

        System.out.println(response.getStatus());

        Assert.assertTrue(response.getStatus() == 200);
    }

    /**
     * Testen ob ein eingeloggter User einen Job zurückgeliefert bekommt
     */
    @Test
    public void testJobDetailsFiltering() {
        Response response = client
                .target("http://localhost:8081")
                .path("jobs/" + job.getId())
                .queryParam("filter_by_task", jobController.getJobTasks(job.getId()).get(0).getId())
                .request(MediaType.APPLICATION_JSON)
                .header("Token", testUserToken)
                .get();

        Assert.assertTrue(response.getStatus() == 200);
/* TODO?
        RestEnvelope<JobResponse> jobResponse = response.readEntity(new GenericType<RestEnvelope<JobResponse>>() {});

        Assert.assertTrue(jobResponse.getData().getJobTasks().size() >= 1);*/
    }


    @Test
    public void testGetWorkflowJobs() {

        Response response = client
                .target("http://localhost:8081")
                .path("workflows/" + workflow.getId() + "/jobs")
                .request(MediaType.APPLICATION_JSON)
                .header("Token", testUserToken)
                .put(Entity.json(""));

        Assert.assertTrue(response.getStatus() == 200);

    }

}
