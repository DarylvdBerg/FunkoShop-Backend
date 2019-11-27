package com.daryl.db;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS users (" +
            "id SERIAL PRIMARY KEY," +
            "email VARCHAR(256) UNIQUE NOT NULL," +
            "password VARCHAR(64) NOT NULL," +
            "name VARCHAR(256) NOT NULL,    " +
            "privileges INT DEFAULT 0)")
    void createTable();


}
