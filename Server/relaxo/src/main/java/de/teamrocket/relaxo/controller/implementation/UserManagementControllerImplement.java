package de.teamrocket.relaxo.controller.implementation;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.UserManagementController;
import de.teamrocket.relaxo.controller.exceptions.*;
import de.teamrocket.relaxo.events.models.UserUserGroupEvent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.persistence.services.UserManagementService;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;
import org.apache.ibatis.exceptions.PersistenceException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;

/**
 * UserManagementControllerImplement ist eine konkrete Ausprägung des Interface UserManagementController
 */
public class UserManagementControllerImplement implements UserManagementController {

    // STATIC

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.CONTROLLER);

    // VARS

    private final UserManagementService userManagementService;

    /**
     *  Instanz des Eventsbusses.
     */
    private final EventBus eventBus;

    // CONSTRUCT

    @Inject
    public UserManagementControllerImplement(UserManagementService userManagementService, EventBus eventBus) {
        this.userManagementService = userManagementService;
        this.eventBus = eventBus;
    }

    // METHODS

    @Override
    public void createUser(User user) throws UserNameTakenException, FieldMissingException {
        if (userManagementService.selectUserByUsername(user.getUsername()) != null) {
            throw new UserNameTakenException("User mit diesem Username bereits vorhanden");
        } else if (user.getPassword() == null || user.getUsername() == null || user.getName() == null ||
                user.getPrename() == null || user.getPassword().isEmpty() || user.getUsername().isEmpty() || user.getName().isEmpty() ||
                user.getPrename().isEmpty()) {
            throw new FieldMissingException("Mindestens eine Angabe für User fehlt.");
        } else {
            userManagementService.insertUser(user);
            eventBus.post(new UserUserGroupEvent());
            LOGGER.info("Ein User mit dem Namen: "+user.getName()+" wurde erstellt.");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userManagementService.selectUserByUsername(username);
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        User user = userManagementService.selectUserById(id);
        if (user == null) {
            throw new UserNotFoundException("Es gibt keinen User zu dieser ID");
        } else {
            return user;
        }
    }

    @Override
    public List<User> getAllUser() {
        return userManagementService.selectAllUser();
    }

    @Override
    public String createTokenForUser(User user) {
        SecureRandom random = new SecureRandom();
        String token = new BigInteger(160, random).toString(32);
        if (userManagementService.selectTokenByUser(user) == null) {
            userManagementService.insertTokenForUser(token, user);
        } else {
            userManagementService.updateTokenForUser(token, user);
        }
        return token;
    }

    @Override
    public User getUserByToken(String token) {
        return userManagementService.selectUserByToken(token);
    }

    @Override
    public User getUserWithCredentials(String username, String password) throws UserNotFoundException{
        User user = userManagementService.selectUserByCredentials(username, password);
        if(user == null) {
            throw new UserNotFoundException("User mit den Credentials nicht gefunden!");
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        userManagementService.deleteUser(user);
    }

    @Override
    public void createUserGroup(UserGroup group) throws NotNullException, DuplicateException {
        try {
            userManagementService.insertUserGroup(group);
            eventBus.post(new UserUserGroupEvent());
            LOGGER.info("Die UserGroup: "+group.getName()+" wurde erstellt.");
        } catch (PersistenceException e) {
            if (((SQLException) e.getCause()).getSQLState().equals("23505")) {
                throw new DuplicateException(e.getMessage());
            } else if (((SQLException) e.getCause()).getSQLState().equals("23502")) {
                throw new NotNullException(e.getMessage());
            }
        }
    }

    @Override
    public List<UserGroup> getAllGroups() {
        return userManagementService.selectAllUserGroups();
    }

    @Override
    public UserGroup getUserGroupByName(String name) throws UserGroupNotFoundException {

        UserGroup group = userManagementService.selectUserGroupByName(name);
        if (group == null) {
            throw new UserGroupNotFoundException("Keine Usergroup unter diesem Namen gefunden");
        } else {
            return group;
        }
    }

    @Override
    public void deleteGroup(UserGroup group) {
        userManagementService.deleteUserGroup(group);
        eventBus.post(new UserUserGroupEvent());
        LOGGER.info("Die UserGroup: "+group.getName()+" mit der ID: "+group.getId()+" wurde entfernt.");
    }

    @Override
    public void addUserToGroup(int userId, int userGroupId) throws UserNotFoundException, UserGroupNotFoundException, PersistenceException {
        User user = userManagementService.selectUserById(userId);
        UserGroup group = userManagementService.selectUserGroupById(userGroupId);
        if (user == null) {
            throw new UserNotFoundException("Kein user für diese ID");
        }
        if (group == null) {
            throw new UserGroupNotFoundException("Keine Usergroup für diese ID");
        }
        userManagementService.insertUserToUserGroup(user, group);
        LOGGER.info("Der User: "+user.getName()+" mit der ID: "+user.getId()+" wurde der UserGroup: "+group.getName()+" mit der ID: "+group.getId()+" hinzugefügt.");
        eventBus.post(new UserUserGroupEvent());
    }

    @Override
    public List<UserGroup> getUserGroupsForUser(int id) throws UserNotFoundException {
        User user = userManagementService.selectUserById(id);
        if (user == null) {
            throw new UserNotFoundException("Kein User für die ID gefunden.");
        }
        return userManagementService.selectUserGroupsByUser(user);
    }

    @Override
    public UserGroup getGroupById(int id) throws UserGroupNotFoundException {
        UserGroup group = userManagementService.selectUserGroupById(id);
        if (group == null) {
            throw new UserGroupNotFoundException("Keine Usergroup zu dieser id gefunden.");
        } else {
            return group;
        }
    }

    @Override
    public String createSessionForUser(User user) {
        String token = this.createTokenForUser(user);
        LOGGER.info("User " + user.getUsername() + " hat eine Session initiiert." + " TOKEN: " + token);
        return token;
    }

    @Override
    public void removeSessionForUser(String token) {
        User user = userManagementService.selectUserByToken(token);
        userManagementService.deleteTokenForUser(user);
        LOGGER.info(user + " hat seine Session entfernt.");
    }

    @Override
    public boolean isUserAdmin(User user) {
        return user.isAdmin();
    }

    @Override
    public List<User> getUserForUserGroup(int id) throws UserGroupNotFoundException {
        UserGroup group = userManagementService.selectUserGroupById(id);
        if (group == null) {
            throw new UserGroupNotFoundException("Keine Nutzergruppe zu dieser ID gefunden");
        }
        return userManagementService.selectUserByUserGroup(group);
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException, FieldMissingException {
        if (userManagementService.selectUserById(user.getId()) == null) {
            throw new UserNotFoundException("User nicht vorhanden");
        } else if (user.getUsername().isEmpty() || user.getName().isEmpty() ||
                user.getPrename().isEmpty()) {
            throw new FieldMissingException("Mindestens eine Angabe für User fehlt.");
        } else {
            if (user.getPassword() == null) {
                user.setPassword(getUserById(user.getId()).getPassword());
            }
            userManagementService.updateUser(user);
            eventBus.post(new UserUserGroupEvent());
            LOGGER.info("Die Daten des Users: "+user.getName()+" mit der ID: "+user.getId()+" wurde aktualisiert.");
        }
    }

    @Override
    public void removeUserFromUserGroup(int userId, int userGroupId) {
        userManagementService.removeUserFromUserGroup(userId, userGroupId);
        eventBus.post(new UserUserGroupEvent());
    }
}
