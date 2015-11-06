package de.teamrocket.relaxo.persistence.services;

import com.google.inject.Inject;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.persistence.mapper.UserManagementMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.guice.transactional.Transactional;

import java.util.List;

/**
 * Service-Klasse zum Persistieren von UserManagement Daten
 */
public class UserManagementService {

    /**
     * UserManagementMapper-Interface
     */
    @Inject
    UserManagementMapper userManagementMapper;

    /**
     * Einfuegen eines Users in die DB
     *
     * @param user der einzufuegende User
     */
    @Transactional
    public void insertUser(User user) {
        userManagementMapper.insertUser(user);
    }

    /**
     * Gibt den User an Hand seines Usernamens zurueck
     *
     * @param username Username des Users
     * @return der User mit dem passenden Usernamen
     */
    @Transactional
    public User selectUserByUsername(String username) {
        return userManagementMapper.selectUserByUsername(username);
    }

    /**
     * Gibt den User an Hand seiner ID zurueck
     *
     * @param id ID des Users
     * @return der User mit der passenden ID
     */
    @Transactional
    public User selectUserById(Integer id) {
        return userManagementMapper.selectUserById(id);
    }

    /**
     * Gibt eine Liste von Usern zurueck
     *
     * @return Liste von Usern
     */
    @Transactional
    public List<User> selectAllUser() {
        return userManagementMapper.selectAllUser();
    }

    /**
     * Einfuegen eines Token zu einem User
     *
     * @param token der einzufuegende Token
     * @param user  der User dem der Token hinzugefuegt wird
     */
    @Transactional
    public void insertTokenForUser(String token, User user) {
        userManagementMapper.insertTokenForUser(token, user);
    }

    /**
     * Aendert das Token fuer den uebergebenen User
     *
     * @param token der neue Token
     * @param user  der User dessen Token geaendert wird
     */
    @Transactional
    public void updateTokenForUser(String token, User user) {
        userManagementMapper.updateTokenForUser(token, user);
    }

    /**
     * Loescht das Token fuer den uebergebenen User
     *
     * @param user der User dessen Token geloescht wird
     */
    @Transactional
    public void deleteTokenForUser(User user) {
        userManagementMapper.deleteTokenForUser(user);
    }

    /**
     * Gibt einen User an Hand eines Token zurueck
     *
     * @param token Token des Users
     * @return der User mit dem passenden Token
     */
    @Transactional
    public User selectUserByToken(String token) {
        return userManagementMapper.selectUserByToken(token);
    }

    /**
     * Gibt das Token des uebergebenen User zurueck
     *
     * @param user der User dessen Token gesucht wird
     * @return das Token des User
     */
    @Transactional
    public String selectTokenByUser(User user) {
        return userManagementMapper.selectTokenByUser(user);
    }

    /**
     * Gibt einen User an Hand seiner Credentials zurueck
     *
     * @param username Username des Users
     * @param password Password des Users
     * @return der User mit den passenden Credentials
     */
    @Transactional
    public User selectUserByCredentials(String username, String password) {
        return userManagementMapper.selectUserByCredentials(username, password);
    }

    /**
     * Loescht einen User aus der DB
     *
     * @param user der zu loeschende User
     */
    @Transactional
    public void deleteUser(User user) {
        userManagementMapper.deleteUser(user);
    }


    /**
     * Einguegen einer UserGroup in die DB
     *
     * @param userGroup die einzufuegende UserGroup
     */
    @Transactional
    public void insertUserGroup(UserGroup userGroup) throws PersistenceException {
        userManagementMapper.insertUserGroup(userGroup);
    }

    /**
     * Gibt eine Liste aller UserGroups zurueck
     *
     * @return Liste aller UserGroups
     */
    @Transactional
    public List<UserGroup> selectAllUserGroups() {
        return userManagementMapper.selectAllUserGroups();
    }

    /**
     * Gibt eine UserGroup an Hand ihres Namens zurueck
     *
     * @param usergroupname Name der UserGroup
     * @return die UserGroup mit dem passenden Namen
     */
    @Transactional
    public UserGroup selectUserGroupByName(String usergroupname) {
        return userManagementMapper.selectUserGroupByName(usergroupname);
    }

    /**
     * Loescht eine UserGroup aus der DB
     *
     * @param userGroup die zu loeschende UserGroup
     */
    @Transactional
    public void deleteUserGroup(UserGroup userGroup) {
        userManagementMapper.deleteUserGroup(userGroup);
    }

    /**
     * Einfuegen eines User zu einer UserGroup
     *
     * @param user      der einzufuegende User
     * @param userGroup die UserGroup in die der User eingefuegt wird
     */
    @Transactional
    public void insertUserToUserGroup(User user, UserGroup userGroup) throws PersistenceException {
        userManagementMapper.insertUserToUserGroup(user, userGroup);
    }

    /**
     * Gibt eine Liste der UserGroups zurueck in denen der uebergeben User ist
     *
     * @param user der User dessen Gruppenzugehoerigkeit gesucht wird
     * @return Liste der UserGroups des User
     */
    @Transactional
    public List<UserGroup> selectUserGroupsByUser(User user) {
        return userManagementMapper.selectUserGroupsByUser(user);
    }

    /**
     * Gibt eine Liste von Usern selektiert nach einer UserGroup zurueck
     * @param userGroup die uebergebene UserGroup
     * @return eine Liste von Usern selektiert nach UserGroup
     */
    @Transactional
    public List<User> selectUserByUserGroup(UserGroup userGroup) {
        return userManagementMapper.selectUserByUserGroup(userGroup);
    }

    /**
     * Gibt eine UserGroup an Hand ihrer ID zurueck
     *
     * @param id ID der UserGroup
     * @return die UserGroup mit der passenden ID
     */
    @Transactional
    public UserGroup selectUserGroupById(Integer id) {
        return userManagementMapper.selectUserGroupById(id);
    }

    /**
     * Update eines Users in der Datenbank
     * @param user der uebergebene User
     * @return true bei erfolgreichem Update, sonst false
     */
    @Transactional
    public boolean updateUser(User user) {
        return userManagementMapper.updateUser(user);
    }

    /**
     * Entfernen eines Users aus einer UserGroup
     * @param userId die uebergebene ID das Users
     * @param userGroupId die uebergebene ID der UserGroup
     */
    @Transactional
    public void removeUserFromUserGroup(int userId, int userGroupId) {
        userManagementMapper.removeUserFromUserGroup(userId, userGroupId);
    }
}
