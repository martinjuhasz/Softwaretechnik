package de.teamrocket.relaxo.database;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.persistence.services.WorkflowService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.guice.XMLMyBatisModule;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Created by mmoel001 on 10.12.14.
 */
public class WorkflowTest {
    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script_for_tests.sql";
	
    static final Logger logger = Logger.getLogger("TestLogger");
    static WorkflowService workflowService;
    static Workflow wf1;
    static Workflow wf2;

    @BeforeClass
    public static void setup() {
    	SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.executeSqlScript(SQL_CREATE);
        sqlExecutor.executeSqlScript(SQL_VIEWS);
        sqlExecutor.executeSqlScript(SQL_TESTDATA);
    	
        Injector injector = Guice.createInjector(new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                setEnvironmentId("development");
                setClassPathResource("mybatis-config.xml");
                bind(WorkflowService.class);
            }
        });

        workflowService = injector.getInstance(WorkflowService.class);
        wf1 = new Workflow();
        wf1.setName("Kaffee kochen");
        wf1.setCreatorId(1);
        wf2 = new Workflow();
        wf2.setName("Update-Test");
        wf2.setCreatorId(2);
        workflowService.createWorkflow(wf2);
        wf2 = workflowService.getWorkflowById(wf2.getId());
    }

    @AfterClass
    public static void teardown() {
        workflowService.deleteWorkflow(wf1.getId());
        workflowService.deleteWorkflow(wf2.getId());
        workflowService = null;
    }

    @Test
    public void testCreateWorkflow() {
        workflowService.createWorkflow(wf1);
        wf1 = workflowService.getWorkflowById(wf1.getId());
        logger.log(Level.INFO, wf1.toString());
        assertNotNull(wf1.getId());
        assertNotNull(wf1.getCreationDate());
    }

    @Test
    public void testUpdatedWorkflow() {
        wf2.setCreatorId(1);
        wf2.setStartItemId(1);
        workflowService.updateWorkflow(wf2);
        wf2 = workflowService.getWorkflowById(wf2.getId());
        logger.log(Level.INFO, wf2.toString());
        assertNotNull(wf2.getStartItemId());
        assertNotNull(wf2.getCreatorId());
    }
}
