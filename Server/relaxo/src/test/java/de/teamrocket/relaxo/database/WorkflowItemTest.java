package de.teamrocket.relaxo.database;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.RelaxoModule;
import de.teamrocket.relaxo.controller.exceptions.NextWorkflowItemException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemLockException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowNotEditableException;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.persistence.exceptions.WorkflowItemConnectionException;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentDate;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentInteger;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentText;
import de.teamrocket.relaxo.models.workflow.*;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;
import de.teamrocket.relaxo.persistence.services.WorkflowService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by mmoel001 on 10.12.14.
 */
public class WorkflowItemTest {
    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script_for_tests.sql";
	
    static WorkflowItemService workflowItemService;
    static WorkflowService workflowService;
    static Workflow workflow;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setup() {
    	SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.executeSqlScript(SQL_CREATE);
        sqlExecutor.executeSqlScript(SQL_VIEWS);
        sqlExecutor.executeSqlScript(SQL_TESTDATA);
    	
        Injector injector = Guice.createInjector(new RelaxoModule(), new ServiceModule());
        workflowItemService = injector.getInstance(WorkflowItemService.class);
        workflowService = injector.getInstance(WorkflowService.class);
        workflow = new Workflow();
        workflow.setName("TestWorkflow");
        workflow.setCreatorId(1);
        workflowService.createWorkflow(workflow);
    }

    @AfterClass
    public static void teardown() {
        workflowService.deleteWorkflow(workflow.getId());
    }

    @Test
    public void testGetTaskById() {
        Task task = (Task) workflowItemService.getWorkflowItemById(2);
        assertEquals(2, task.getId());
        WorkflowItem workflowItem = workflowItemService.getWorkflowItemById(1);
        assertThat("TaskComponentText", not(workflowItem.getClass().getSimpleName()));
    }

    @Test
    public void testGetUserTasks() {
        List<Task> tasks;
        tasks = workflowItemService.getUserTasks(1, 1);
        assertTrue(!tasks.isEmpty());
    }

    @Test
    public void testCanUserAccessWorkflowItem() {
        boolean canAccess;
        canAccess = workflowItemService.canUserAccessWorkflowItem(2, 1);
        assertTrue(canAccess);
        canAccess = workflowItemService.canUserAccessWorkflowItem(3, 2);
        assertFalse(canAccess);
    }

    @Test
    public void testGetTaskComponents() {
        List<TaskComponent> taskComponents = workflowItemService.getTaskComponents(10);

        TaskComponent taskComponent = taskComponents.get(0);
        assertEquals("TaskComponentText", taskComponent.getClass().getSimpleName());
        assertEquals("Ihr Nachname", ((TaskComponentText) taskComponent).getDefaultValue());

        taskComponent = taskComponents.get(1);
        assertEquals("TaskComponentText", taskComponent.getClass().getSimpleName());
        assertEquals("Ihr Vorname", ((TaskComponentText) taskComponent).getDefaultValue());

        taskComponent = taskComponents.get(2);
        assertEquals("TaskComponentInteger", taskComponent.getClass().getSimpleName());
        assertEquals(65197, ((TaskComponentInteger) taskComponent).getDefaultValue());

        taskComponent = taskComponents.get(3);
        assertEquals("TaskComponentDate", taskComponent.getClass().getSimpleName());
        assertEquals((new GregorianCalendar(1970, 0, 1, 0, 0)).getTime(), ((TaskComponentDate) taskComponent).getDefaultValue());
    }

    @Test
    public void testCreateWorkflowItem() {
        WorkflowItem wfi = new Task();
        wfi.setWorkflowId(1);
        wfi.setxPos(50);
        wfi.setyPos(150);
        workflowItemService.createWorkflowItem(wfi);
        assertNotNull(wfi.getId());
    }

    @Test
    public void testGetWorkflowItemsForWorkflow() {
        /*
        List<WorkflowItem> workflowItems = workflowItemService.getWorkflowItemsByWorkflowId(1);
        workflowItems = workflowItemService.getWorkflowItemsByWorkflowId(2);*/
    }

    @Test
    public void testDeleteWorkflowItem() throws WorkflowNotEditableException, WorkflowItemNotFoundException {
        Workflow wf = new Workflow();
        wf.setCreatorId(1);
        wf.setName("UnitTestWorkflow");
        workflowService.createWorkflow(wf);
        WorkflowItem wfi = new Task();
        wfi.setWorkflowId(wf.getId());
        wfi.setxPos(50);
        wfi.setyPos(150);
        workflowItemService.createWorkflowItem(wfi);
        workflowItemService.deleteWorkflowItem(wfi.getId());
        wfi = workflowItemService.getWorkflowItemById(wfi.getId());
        workflowService.deleteWorkflow(wf.getId());
        //wf = workflowService.getWorkflowById(wf.getId());
        assertNull(wfi);
    }

    @Test
    public void testUpdatePosition() {
        workflowItemService.updatePosition(2, 300, 300);
        WorkflowItem wfi = workflowItemService.getWorkflowItemById(2);
        assertEquals(300, wfi.getxPos());
        assertEquals(300, wfi.getyPos());
    }

    @Test
    public void testSetNextWorkflowItem() throws WorkflowNotEditableException, WorkflowItemNotFoundException, NextWorkflowItemException, WorkflowItemConnectionException {
        Workflow wf = new Workflow();
        wf.setCreatorId(1);
        wf.setName("UnitTestWorkflow");
        workflowService.createWorkflow(wf);
        Task wfi = new Task();
        wfi.setWorkflowId(wf.getId());
        wfi.setxPos(50);
        wfi.setyPos(150);
        workflowItemService.createWorkflowItem(wfi);
        List<Integer> nextWorkflowItemIds = new LinkedList<>();
        nextWorkflowItemIds.add(2);
        workflowItemService.setNextWorkflowItems(wfi.getId(), nextWorkflowItemIds);
        wfi = (Task) workflowItemService.getWorkflowItemById(wfi.getId());
        assertEquals(2, wfi.getNextWorkflowItemId().intValue());
        workflowItemService.deleteWorkflowItem(wfi.getId());
        workflowService.deleteWorkflow(wf.getId());
    }

    @Test
    public void testLockingWorkflowItem() throws WorkflowItemLockException, WorkflowItemNotFoundException {
        workflowItemService.lockWorkflowItem(2);
        assertTrue(workflowItemService.isWorkflowItemLocked(2));
        workflowItemService.unlockWorkflowItem(2);
        assertFalse(workflowItemService.isWorkflowItemLocked(2));
    }

    @Test
    public void testSetUserGroupsForTask() {
        List<Integer> groupIds = new LinkedList<>();
        groupIds.add(1);
        groupIds.add(3);
        workflowItemService.setUserGroupsForWorkflowItem(11, groupIds);
        workflowItemService.removeUserGroupFromWorkflowItem(11, 1);
        workflowItemService.removeUserGroupFromWorkflowItem(11, 3);
    }

    @Test
    public void testGetUserGroupsForWorkflowItem() {
        List<Integer> groupIds = workflowItemService.getUserGroupForWorkflowItem(1);
        assertEquals(3, groupIds.size());
    }

    @Test
    public void testGetComponentsAndFormGroupsForTask() {
        List<FormGroupForTask> fgft = workflowItemService.getComponentsAndFormGroupsForTask(3);
        assertEquals(1, fgft.get(0).getFormGroupId());
    }

    @Test
    public void testCreateTaskComponent() {
        TaskComponentText taskComponentText = new TaskComponentText();
        taskComponentText.setName("Hobby");
        taskComponentText.setComment("Hier das Lieblingshobby eintragen");
        taskComponentText.setRequired(false);
        taskComponentText.setFormGroupId(1);
        taskComponentText.setDefaultValue("zocken");
        taskComponentText.setRegex("");
        workflowItemService.createTaskComponent(taskComponentText);
        workflowItemService.deleteTaskComponent(taskComponentText.getId());
    }

    @Test
    public void testUpdateTask() {
        List<TaskComponentForTask> taskComponents = new LinkedList<>();
        taskComponents.add(new TaskComponentForTask(3, false));
        taskComponents.add(new TaskComponentForTask(4, false));
        List<Integer> userGroupIds = new LinkedList<>();
        userGroupIds.add(1);
        try {
            workflowItemService.updateTask(8, "neuer Name", userGroupIds, taskComponents);
        } catch (WorkflowItemNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<FormGroupForTask> fgft = workflowItemService.getComponentsAndFormGroupsForTask(8);
        List<Integer> components = fgft.get(0).getTaskComponentIds();
        assertEquals(2, components.size());
        workflowItemService.removeComponentFromWorkflowItem(8, 3);
        workflowItemService.removeComponentFromWorkflowItem(8, 4);
    }

    @Test
    public void getTaskComponentType() {
        String type = workflowItemService.getTaskComponentType(2);
        assertEquals("TEXTLABEL", type);
    }

    @Test
    public void testWorkflowDecisionUpdateException() throws WorkflowItemNotFoundException {
        exception.expect(WorkflowItemNotFoundException.class);
        workflowItemService.updateWorkflowDecision(0, null, null, 0);
    }

    @Test
    public void testWorkflowDecisionUpdateNoException() throws WorkflowItemNotFoundException {
        WorkflowItem workflowItem = workflowItemService.getWorkflowItemById(14);
        assertThat(workflowItem, instanceOf(WorkflowDecision.class));
        WorkflowDecision workflowDecision = (WorkflowDecision) workflowItem;
        assertThat(workflowDecision.getNextWorkflowItems().size(), equalTo(2));
        assertThat(workflowDecision.getNextWorkflowItems().get(0), equalTo(15));
        assertThat(workflowDecision.getNextWorkflowItems().get(1), equalTo(16));

        List<TaskVariable> variables = new LinkedList<>();
        TaskVariable taskVariable = new TaskVariable();
        taskVariable.setName("Kreditbetrag");
        taskVariable.setTaskId(13);
        taskVariable.setTaskComponentId(10);
        variables.add(taskVariable);

        String condition = "Kreditbetrag < 1000.00";
        workflowItemService.updateWorkflowDecision(workflowDecision.getId(), variables, condition, 15);

    }
    
    @Test
    public void testWorkflowScriptUpdateNoException() throws WorkflowItemNotFoundException {
        WorkflowItem workflowItem = workflowItemService.getWorkflowItemById(21);
        assertThat(workflowItem, instanceOf(WorkflowScript.class));
        WorkflowScript workflowScript = (WorkflowScript) workflowItem;
        assertThat(workflowScript.getNextWorkflowItemId(), equalTo(22));

        List<TaskVariable> variables = new LinkedList<>();
        TaskVariable taskVariable = new TaskVariable();
        taskVariable.setName("Arbeitsverweigerer");
        taskVariable.setTaskId(20);
        taskVariable.setTaskComponentId(15);
        variables.add(taskVariable);

        String script = "Arbeitsverweigerer +  ' muss mehr Arbeiten!'";
        workflowItemService.updateWorkflowScript(workflowScript.getId(), variables, script);
    }

    @Test(expected = WorkflowItemConnectionException.class)
    public void testSetNextWorkflowItemToPreviousWorkflowItem() throws WorkflowNotEditableException, WorkflowItemNotFoundException, NextWorkflowItemException, WorkflowItemConnectionException {
    	WorkflowItem workflowItem1 = new Task();
        WorkflowItem workflowItem2 = new Task();
        workflowItem1.setName("Test1");
        workflowItem1.setType("TASK");
        workflowItem1.setWorkflowId(workflow.getId());
        workflowItemService.createWorkflowItem(workflowItem1);
        workflowItem2.setName("Test2");
        workflowItem2.setType("TASK");
        workflowItem2.setWorkflowId(workflow.getId());
        workflowItemService.createWorkflowItem(workflowItem2);

        workflowItemService.setNextWorkflowItems(workflowItem1.getId(), Arrays.asList(workflowItem2.getId()));
        workflowItemService.setNextWorkflowItems(workflowItem2.getId(), Arrays.asList(workflowItem1.getId()));

        workflowItemService.deleteWorkflowItem(workflowItem1.getId());
        workflowItemService.deleteWorkflowItem(workflowItem2.getId());
    }
}
