package com.daryl.db;

import com.daryl.api.User;
import com.daryl.db.mappers.UserMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterRowMapper(UserMapper.class)
public interface UserDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS users (" +
            "id SERIAL PRIMARY KEY," +
            "email VARCHAR(256) UNIQUE NOT NULL," +
            "password VARCHAR(64) NOT NULL," +
            "name VARCHAR(256) NOT NULL,    " +
            "privileges INT DEFAULT 0)")
    void createTable();

    @SqlQuery("SELECT password FROM users WHERE email = :email")
    String getPasswordFromEmail(@Bind("email") String email);

    @SqlUpdate("INSERT INTO users (email, password, name) VALUES (:email, :password, :name)")
    @GetGeneratedKeys("id")
    int createUser(@Bind("email") String email, @Bind("password") String password, @Bind("name") String name);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    User getUserFromEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    User getUserFromId(@Bind("id") int id);

    @SqlUpdate("UPDATE users SET privileges = :privileges WHERE id = :id")
    void changeUserPrivileges(@Bind("privileges") int privileges, @Bind("id") int id);

    @SqlUpdate("UPDATE users SET password = :password WHERE id = :id")
    void updateUserPassword(@Bind("password") String password, @Bind("id") int id);

    @SqlUpdate("UPDATE users SET email = :email, SET name = :name")
    void updateUserDetails(@Bind("email") String email, @Bind("name") String name);

}
