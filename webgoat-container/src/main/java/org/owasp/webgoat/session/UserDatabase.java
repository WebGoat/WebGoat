package org.owasp.webgoat.session;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;

class UserDatabase {
    private Connection userDB;
    private final String USER_DB_URI = "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "UserDatabase";

    private final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL UNIQUE);";
    private final String CREATE_ROLES_TABLE = "CREATE TABLE IF NOT EXISTS roles (id INTEGER PRIMARY KEY AUTO_INCREMENT, rolename VARCHAR(255) NOT NULL UNIQUE);";
    private final String CREATE_USER_ROLES_TABLE = "CREATE TABLE IF NOT EXISTS user_roles (id INTEGER PRIMARY KEY AUTO_INCREMENT, user_id INTEGER NOT NULL, role_id INTEGER NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id), FOREIGN KEY (role_id) REFERENCES roles(id));";
    private final String ADD_DEFAULT_USERS = "INSERT INTO users (username) VALUES ('webgoat'),('basic'),('guest');";
    private final String ADD_DEFAULT_ROLES = "INSERT INTO roles (rolename) VALUES ('webgoat_basic'),('webgoat_admin'),('webgoat_user');";
    private final String ADD_ROLE_TO_USER = "INSERT INTO user_roles (user_id, role_id) SELECT users.id, roles.id FROM users, roles WHERE users.username = ? AND roles.rolename = ?;";

    private final String QUERY_ALL_USERS = "SELECT username FROM users;";
    private final String QUERY_ALL_ROLES_FOR_USERNAME = "SELECT rolename FROM roles, user_roles, users WHERE roles.id = user_roles.role_id AND user_roles.user_id = users.id AND users.username = ?;";
    private final String QUERY_TABLE_COUNT = "SELECT count(id) AS count FROM table;";

    private final String DELETE_ALL_ROLES_FOR_USER = "DELETE FROM user_roles WHERE user_id IN (SELECT id FROM users WHERE username = ?);";
    private final String DELETE_USER = "DELETE FROM users WHERE username = ?;";

    /**
     * <p>Constructor for UserDatabase.</p>
     */
    public UserDatabase() {
        createDefaultTables();
        if (getTableCount("users") <= 0) {
            createDefaultUsers();
        }
        if (getTableCount("roles") <= 0) {
            createDefaultRoles();
        }
        if (getTableCount("user_roles") <= 0) {
            addDefaultRolesToDefaultUsers();
        }
    }

    /**
     * <p>open.</p>
     *
     * @return a boolean.
     */
    public boolean open() {
        try {
            if (userDB == null || userDB.isClosed()) {
                Class.forName("org.h2.Driver");
                userDB = DriverManager.getConnection(USER_DB_URI, "webgoat_admin", "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * <p>close.</p>
     *
     * @return a boolean.
     */
    public boolean close() {
        try {
            if (userDB != null && !userDB.isClosed())
                userDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * <p>getTableCount.</p>
     *
     * @param tableName a {@link java.lang.String} object.
     * @return a int.
     */
    public int getTableCount(String tableName) {
        int count = 0;
        try {
            open();
            Statement statement = userDB.createStatement();
            ResultSet countResult = statement.executeQuery(QUERY_TABLE_COUNT.replace("table", tableName));
            if (countResult.next()) {
                count = countResult.getInt("count");
            }
            countResult.close();
            statement.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            count = -1;
        }
        return count;
    }

    /**
     * <p>getUsers.</p>
     *
     * @return a {@link java.util.Iterator} object.
     */
    public Iterator<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        User currentUser;
        ResultSet userResults, roleResults;

        try {
            open();
            Statement statement = userDB.createStatement();
            PreparedStatement rolesForUsers = userDB.prepareStatement(QUERY_ALL_ROLES_FOR_USERNAME);

            userResults = statement.executeQuery(QUERY_ALL_USERS);
            while (userResults.next()) {
                currentUser = new User(userResults.getString("username"));
                rolesForUsers.setString(1, currentUser.getUsername());
                roleResults = rolesForUsers.executeQuery();
                while (roleResults.next()) {
                    currentUser.addRole(roleResults.getString("rolename"));
                }
                roleResults.close();
            }
            rolesForUsers.close();
            userResults.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            users = new ArrayList<User>();
        }

        return users.iterator();
    }

    /**
     * <p>addRoleToUser.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param rolename a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean addRoleToUser(String username, String rolename) {
        try {
            open();
            PreparedStatement statement = userDB.prepareStatement(ADD_ROLE_TO_USER);
            statement.setString(1, username);
            statement.setString(2, rolename);
            statement.execute();
            statement.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * <p>removeUser.</p>
     *
     * @param user a {@link org.owasp.webgoat.session.User} object.
     * @return a boolean.
     */
    public boolean removeUser(User user) {
        return removeUser(user.getUsername());
    }

    /**
     * <p>removeUser.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean removeUser(String username) {
        try {
            open();

            PreparedStatement deleteUserRoles = userDB.prepareStatement(DELETE_ALL_ROLES_FOR_USER);
            PreparedStatement deleteUser = userDB.prepareStatement(DELETE_USER);

            deleteUserRoles.setString(1, username);
            deleteUser.setString(1, username);

            deleteUserRoles.execute();
            deleteUser.execute();

            deleteUserRoles.close();
            deleteUser.close();

            close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

	/*
	 * Methods to initialise the default state of the database.
	 */

    private boolean createDefaultTables() {
        try {
            open();
            Statement statement = userDB.createStatement();
            statement.execute(CREATE_USERS_TABLE);
            statement.execute(CREATE_ROLES_TABLE);
            statement.execute(CREATE_USER_ROLES_TABLE);
            statement.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createDefaultUsers() {
        try {
            open();
            Statement statement = userDB.createStatement();
            statement.execute(ADD_DEFAULT_USERS);
            statement.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createDefaultRoles() {
        try {
            open();
            Statement statement = userDB.createStatement();
            statement.execute(ADD_DEFAULT_ROLES);
            statement.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void addDefaultRolesToDefaultUsers() {
        addRoleToUser("webgoat", "webgoat_admin");
        addRoleToUser("basic", "webgoat_user");
        addRoleToUser("basic", "webgoat_basic");
        addRoleToUser("guest", "webgoat_user");
    }
}
