package org.owasp.webgoat.session;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
