package de.teamrocket.relaxo.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.RelaxoModule;
import de.teamrocket.relaxo.controller.exceptions.*;
import de.teamrocket.relaxo.messaging.Broker;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserManagementControllerTest {
    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script_for_tests.sql";
	
    static final RelaxoLogger logger = new RelaxoLogger(RelaxoLoggerType.TEST);
    static UserManagementController userManagementController;
    static Broker messageBroker;
    static String token;
    static User user1,  user2, checkUser;
    static UserGroup group1, group2, checkUserGroup;

    @BeforeClass
    public static void setup() {
    	SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.executeSqlScript(SQL_CREATE);
        sqlExecutor.executeSqlScript(SQL_VIEWS);
        sqlExecutor.executeSqlScript(SQL_TESTDATA);
    	
        Injector injector = Guice.createInjector(new ServiceModule(), new RelaxoModule());

        userManagementController = injector.getInstance(UserManagementController.class);
        messageBroker = injector.getInstance(Broker.class);
        messageBroker.start();

        // User 1
        user1 = new User();
        user1.setUsername("mmust001");
        user1.setPassword("testpw");
        user1.setPrename("Max");
        user1.setName("Muster");
        user1.setAdmin(true);
        user1.setActive(true);

        // User 2
        user2 = new User();
        user2.setUsername("mmust001");
        user2.setPassword("testpw");
        user2.setPrename("");
        user2.setName("");

        // UserGroup 1
        group1 = new UserGroup();
        group1.setName("Marketing");

        // UserGroup2
        group2 = new UserGroup();
    }

    @Test
    public void testCreateUser() {
        try {
            userManagementController.createUser(user1);
        } catch (UserNameTakenException e) {
            logger.warning(e.getMessage());
        } catch (FieldMissingException e) {
            logger.warning(e.getMessage());
        }
        assertNotSame(user1.getId(),0);
    }

    @Test
    public void testCreateSessionForUser() {
        token = userManagementController.createSessionForUser(user1);
        assertNotNull(userManagementController.getUserByToken(token));
    }

    @Test
    public void testGetUserWithCredentials() {
        try {
            checkUser = userManagementController.getUserWithCredentials("mmust001","testpw");
        } catch (UserNotFoundException e) {
            logger.warning(e.getMessage());
        }
        assertNotNull(checkUser);
        checkUser = null;
    }

    @Test
    public void testGetUserById() {
        try {
            checkUser = userManagementController.getUserById(user1.getId());
        } catch (UserNotFoundException e) {
            logger.warning(e.getMessage());
        }
        assertNotNull(checkUser);
        checkUser = null;
    }

    @Test
    public void testUpdateUser() {
        user1.setActive(false);
        try {
            userManagementController.updateUser(user1);
            checkUser = userManagementController.getUserById(user1.getId());
        } catch (UserNotFoundException e) {
            logger.warning(e.getMessage());
        } catch (FieldMissingException e) {
            logger.warning(e.getMessage());
        }
        assertFalse(checkUser.isActive());
        checkUser = null;
    }

    @Test
    public void testCreateUserGroup() {
        try {
            userManagementController.createUserGroup(group1);
        } catch (NotNullException e) {
            logger.warning(e.getMessage());
        } catch (DuplicateException e) {
            logger.warning(e.getMessage());
        }
        assertNotSame(group1.getId(),0);
    }

    @Test
    public void testGetUserGroupByName() {
        try {
            checkUserGroup = userManagementController.getUserGroupByName("Marketing");
        } catch (UserGroupNotFoundException e) {
            logger.warning(e.getMessage());
        }
        assertNotNull(checkUserGroup);
        checkUserGroup = null;
    }

    @Test
    public void testGetUserGroupById() {
        try {
            checkUserGroup = userManagementController.getGroupById(group1.getId());
        } catch (UserGroupNotFoundException e) {
            logger.warning(e.getMessage());
        }
        assertNotNull(checkUserGroup);
        checkUserGroup = null;
    }

    @Test
    public void testAddUserToUserGroup() {
        List<UserGroup> groups = null;
        try {
            userManagementController.addUserToGroup(user1.getId(),group1.getId());
            groups = userManagementController.getUserGroupsForUser(user1.getId());
        } catch (UserNotFoundException e) {
            logger.warning(e.getMessage());
        } catch (UserGroupNotFoundException e) {
            logger.warning(e.getMessage());
        }
        for(UserGroup ug : groups) {
            assertEquals("Marketing",ug.getName());
        }
    }

    @Test
    public void testGetUserForUserGroup() {
        List<User> users = null;
        try {
            users = userManagementController.getUserForUserGroup(group1.getId());
        } catch (UserGroupNotFoundException e) {
            logger.warning(e.getMessage());
        }
        for(User user : users) {
            assertEquals("Max", user.getPrename());
        }
    }

    @Test(expected = UserNameTakenException.class)
    public void testUserNameTakenException() throws UserNameTakenException, FieldMissingException{
        userManagementController.createUser(user2);
    }

    @Test(expected = FieldMissingException.class)
    public void testFieldMissingException() throws UserNameTakenException, FieldMissingException{
        user2.setUsername("mmust002");
        userManagementController.createUser(user2);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUserNotFoundException() throws UserNotFoundException{
        checkUser = userManagementController.getUserWithCredentials("Test123","Test456");
    }

    @Test(expected = UserGroupNotFoundException.class)
    public void testUserGroupNotFoundException() throws UserGroupNotFoundException{
        checkUserGroup = userManagementController.getGroupById(9999);
    }

    @Test(expected = NotNullException.class)
    public void testNotNullException() throws DuplicateException, NotNullException{
        userManagementController.createUserGroup(group2);
    }

    @Test(expected = DuplicateException.class)
    public void testDuplicateException() throws DuplicateException, NotNullException{
        group2.setName("Marketing");
        userManagementController.createUserGroup(group2);
    }

    @AfterClass
    public static void teardown() {
        userManagementController.removeSessionForUser(token);
        userManagementController.removeUserFromUserGroup(user1.getId(), group1.getId());
        userManagementController.deleteGroup(group1);
        userManagementController.deleteUser(user1);
        user1 = null;
        user2 = null;
        checkUser = null;
        group1 = null;
        group2 = null;
        checkUserGroup = null;
        messageBroker.stop();
    }

}