package de.teamrocket.relaxo.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.RelaxoModule;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Created by mobbitz on 18.01.15.
 */
public class TaskControllerTest {
    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script_for_tests.sql";
    
    static final Logger logger = Logger.getLogger("TestLogger");
    static WorkflowItemService workflowItemService;
    static WorkflowItem workflowItem;
    static TaskController taskController;

    @BeforeClass
    public static void setup() {
    	SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.executeSqlScript(SQL_CREATE);
        sqlExecutor.executeSqlScript(SQL_VIEWS);
        sqlExecutor.executeSqlScript(SQL_TESTDATA);
    	
        Injector injector = Guice.createInjector(new ServiceModule(), new RelaxoModule());
        workflowItemService = injector.getInstance(WorkflowItemService.class);
        taskController = injector.getInstance(TaskController.class);
        workflowItem = workflowItemService.getWorkflowItemById(3);
    }

    @Test
    public void testGetFormGroupsForTask() {
        List<FormGroup> formGroups = new LinkedList<>();
        formGroups = taskController.getFormGroupsForTask((Task) workflowItem);
        assertNotNull(formGroups.get(0));
        assertNotNull(formGroups.get(0).getComponents().get(0));
    }

}
