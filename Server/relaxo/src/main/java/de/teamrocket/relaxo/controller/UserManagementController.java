package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.controller.exceptions.*;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * Controller Interface fuer UserManagement stellt Methoden bereit, um spezifisch auf User und UserGroups zuzugreifen.
 */
public interface UserManagementController {

    /**
     * Erstellt einen neuen User.
     *
     * @param user Instanz des User, der in die DB geschrieben werden soll.
     * @throws UserNameTakenException wird geworfen, sofern die Username bereits exisitert
     * @throws FieldMissingException wird geworfen, sofern ein Feld nicht angegeben wurde, welches required ist
     */
    public void createUser(User user) throws UserNameTakenException, FieldMissingException;

    /**
     * Gibt einen User an Hand seines Username zurueck
     *
     * @param username der Username des User
     * @return der User mit dem passenden Username
     */
    public User getUserByUsername(String username);

    /**
     * Gibt einen User an Hand seiner ID zurueck
     *
     * @param id die Id des User
     * @return der User mit der passenden ID
     */
    public User getUserById(int id) throws UserNotFoundException;

    /**
     * Liefert eine Liste aller User
     *
     * @return die Liste aller User
     */
    public List<User> getAllUser();

    /**
     * Erstellt ein Token fuer einen User
     *
     * @param user der User fuer den ein Token erstellt wird
     * @return der Token des Users
     */
    public String createTokenForUser(User user);

    /**
     * Gibt einen User an Hand eines Token zurueck
     *
     * @param token der Token des User
     * @return der User mit dem passenden Token
     */
    public User getUserByToken(String token);

    /**
     * Gibt einen User an Hand seiner Credentials zurueck
     *
     * @param username der Userrname
     * @param password das Password
     * @return der User mit den passenden Credentials
     */
    public User getUserWithCredentials(String username, String password) throws UserNotFoundException;

    /**
     * Loeschen eines User
     *
     * @param user der zu loeschende User
     */
    public void deleteUser(User user);


    // USERGROUP BEZOGEN METHODEN

    /**
     * Erstellt eine neue UserGroup
     */
    public void createUserGroup(UserGroup group) throws NotNullException, DuplicateException;

    /**
     * Liefert eine Liste aller UserGroups
     *
     * @return die Liste aller UserGroups
     */
    public List<UserGroup> getAllGroups();

    /**
     * Gibt eine UserGroup an Hand ihres Namens zurueck
     *
     * @param name der Name der UserGroup
     * @return die UserGroup mit dem passenden Namen
     */
    public UserGroup getUserGroupByName(String name) throws UserGroupNotFoundException;

    /**
     * Loeschen einer UserGroup
     *
     * @param group die zu loeschende UserGroup
     */
    public void deleteGroup(UserGroup group);

    /**
     * Fügt einen User einer UserGroup hinzu
     *
     * @param userId      der User, der hinzugefuegt werden soll
     * @param userGroupId die Gruppe, zu der hinzugefuegt werden soll
     */
    public void addUserToGroup(int userId, int userGroupId) throws UserNotFoundException, UserGroupNotFoundException, PersistenceException;

    /**
     * Gibt alle UserGroups eines User zurueck
     *
     * @param id Id des Users dessen UserGroups gesucht sind
     * @return die UserGroups des User
     */
    public List<UserGroup> getUserGroupsForUser(int id) throws UserNotFoundException;

    /**
     * Gibt eine UserGroup an Hand ihrer ID zurueck
     *
     * @param id die ID der UserGroup
     * @return die UserGroup mit der passenden ID
     */
    public UserGroup getGroupById(int id) throws UserGroupNotFoundException;

    /**
     * Erstellt eine userSession und gibt den Token-String zurück zurück.
     *
     * @param user der User fuer den ein Token erstellt wird
     * @return das erstellte Token
     */
    public String createSessionForUser(User user);

    /**
     * Entfernt die Session des übergebenden Tokens.
     *
     * @param token das Token
     */
    public void removeSessionForUser(String token);

    /**
     * Prüft, ob der übergebende User ein Administrator ist.
     *
     * @param user der zu prüfende User
     * @return true, bei Erfolg
     */
    public boolean isUserAdmin(User user);

    /**
     * Gibt eine Liste von User zurück, welche zu dieser UserGroup gehören.
     *
     * @param id Id welche die Nutzergruppe identifiziert
     */
    public List<User> getUserForUserGroup(int id) throws UserGroupNotFoundException;

    /**
     * Aktuallisiert die Daten eines Users in die DB.
     * @param user Die Daten, des Users.
     * @throws UserNotFoundException
     * @throws FieldMissingException
     */
    public void updateUser(User user) throws UserNotFoundException, FieldMissingException;

    /**
     * Entfernt einen User von einer UserGroup.
     * @param userId die ID des Users.
     * @param userGroupId die ID der UserGroup.
     */
    public void removeUserFromUserGroup(int userId, int userGroupId);
}
