package de.teamrocket.relaxo.persistence.mapper;

import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * Interface fuer UserManagement-Statements an die Datenbank
 */
public interface UserManagementMapper {

    // User bezogene Methoden

    public void insertUser(User user);

    public User selectUserByUsername(String username);

    public User selectUserById(Integer id);

    public List<User> selectAllUser();

    public void insertTokenForUser(@Param("token") String token, @Param("user") User user);

    public void updateTokenForUser(@Param("token") String token, @Param("user") User user);

    public void deleteTokenForUser(User user);

    public User selectUserByToken(String token);

    public String selectTokenByUser(User user);

    public User selectUserByCredentials(@Param("username") String username, @Param("password") String password);

    public void deleteUser(User user);

    public boolean updateUser(User user);


    // UserGroup bezogene Methoden

    public void insertUserGroup(UserGroup userGroup) throws PersistenceException;

    public List<UserGroup> selectAllUserGroups();

    public UserGroup selectUserGroupByName(String usergroupname);

    public void deleteUserGroup(UserGroup userGroup);

    public void insertUserToUserGroup(@Param("user") User user, @Param("usergroup") UserGroup userGroup) throws PersistenceException;

    public List<UserGroup> selectUserGroupsByUser(User user);

    public List<User> selectUserByUserGroup(UserGroup userGroup);

    public UserGroup selectUserGroupById(Integer id);

    public void removeUserFromUserGroup(@Param("user_id") Integer userId, @Param("usergroup_id") Integer userGroupId);
}
