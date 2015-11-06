package de.teamrocket.relaxo.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.RelaxoModule;
import de.teamrocket.relaxo.controller.exceptions.DuplicateException;
import de.teamrocket.relaxo.controller.exceptions.FieldMissingException;
import de.teamrocket.relaxo.controller.exceptions.NotNullException;
import de.teamrocket.relaxo.controller.exceptions.UserGroupNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.UserNameTakenException;
import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemTypeException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowLockException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowStartItemExistException;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.models.workflow.Start;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.persistence.services.WorkflowService;

public class WorkflowControllerTest {
    static final Logger logger = Logger.getLogger("TestLogger");
    static WorkflowController workflowController;
    static WorkflowService workflowService;
    static UserManagementController userManagementController;
    static WorkflowItemController workflowItemController;

    @BeforeClass
    public static void setup() {    	
        Injector injector = Guice.createInjector(new ServiceModule(), new RelaxoModule());
        workflowController = injector.getInstance(WorkflowController.class);
        workflowItemController = injector.getInstance(WorkflowItemController.class);
        workflowService = injector.getInstance(WorkflowService.class);
        userManagementController = injector.getInstance(UserManagementController.class);
    }

    @Test
    public void testIsUserAbleToSeeWorkflow() throws UserNameTakenException, FieldMissingException, NotNullException, DuplicateException, PersistenceException, UserNotFoundException, UserGroupNotFoundException, WorkflowStartItemExistException, WorkflowItemTypeException, WorkflowLockException {
        User user1 = new User();
        user1.setUsername("workflowcontrollertestuser");
        user1.setName("workflowcontrollertestuser");
        user1.setPrename("workflowcontrollertestuser");
        user1.setPassword("workflowcontrollertestuser");
        userManagementController.createUser(user1);
        
        User user2 = new User();
        user2.setUsername("workflowcontrollertestuser2");
        user2.setName("workflowcontrollertestuser2");
        user2.setPrename("workflowcontrollertestuser2");
        user2.setPassword("workflowcontrollertestuser2");
        userManagementController.createUser(user2);
        
        UserGroup userGroup = new UserGroup();
        userGroup.setName("workflowcontrollertestusergroup");
        userManagementController.createUserGroup(userGroup);
        userManagementController.addUserToGroup(user2.getId(), userGroup.getId());
        
        Workflow workflow = workflowController.createWorkflow(user1, "workflowcontrollertestworkflow");
        Start start =  (Start) workflowItemController.createWorkflowItem(workflow, "START", 0, 0, user1);
        
        assertFalse(workflowController.isUserAbleToSeeWorkflow(workflow, user2));
        workflowItemController.setUserGroupsForWorkflowItem(start, Arrays.asList(userGroup.getId()));
        
        workflowController.updateRunnableStateForWorkflow(workflow, true, user1);
        assertTrue(workflowController.isUserAbleToSeeWorkflow(workflow, user2));
        
        workflowController.deleteWorkflow(workflow);
        userManagementController.deleteUser(user1);
        userManagementController.deleteUser(user2);
        userManagementController.deleteGroup(userGroup);
    }
}
