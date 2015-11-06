package de.teamrocket.relaxo.database;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.persistence.services.FormGroupService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.guice.XMLMyBatisModule;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mmoel001 on 15.12.14.
 */
public class FormGroupTest {
    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script_for_tests.sql";
	
    static final Logger logger = Logger.getLogger("TestLogger");
    static FormGroupService formGroupService;
    static FormGroup fg1;
    static FormGroup fg2;
    static List<FormGroup> fgList1;
    static List<TaskComponent> tcList1;


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
                bind(FormGroupService.class);
            }
        });

        formGroupService = injector.getInstance(FormGroupService.class);
        fg1 = new FormGroup();
        fg1.setName("Test FromGroup 1");
        fg1.setWorkflowId(1);
    }

    @AfterClass
    public static void teardown() {
        formGroupService.deleteFormGroup(fg1.getId());
        formGroupService = null;
    }

    @Test
    public void testCreateFormGroup() {
        formGroupService.createFormGroup(fg1);
        fg1 = formGroupService.getFormGroupById(fg1.getId());
        logger.log(Level.INFO, fg1.toString());
        assertNotNull(fg1.getId());
    }

    @Test
    public void testGetAllFormGroups() {
        fgList1 = formGroupService.getAllFormGroups(1);
        logger.log(Level.INFO, fgList1.get(0).toString());
        assertNotNull(fgList1.size());
    }

    @Test
    public void testGetFormGroupById() {
        fg2 = formGroupService.getFormGroupById(1);
        System.out.println("..");
        logger.log(Level.INFO, fg2.getComponents().get(0).getName());
        assertNotNull(fg2);
    }

    @Test
    public void testGetFormGroupByTask() {
        fgList1 = formGroupService.getFormGroupsByTask(10);
        assertEquals(2, fgList1.size());
    }

    @Test
    public void testGetTaskComponentsForTaskAndFormGroup() {
        tcList1 = formGroupService.getTaskComponentsForTaskAndFormGroup(10, 1);
        assertEquals(3, tcList1.size());
    }
}
