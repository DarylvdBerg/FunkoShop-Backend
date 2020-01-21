package com.daryl.db.mappers;

import com.daryl.api.UserAddress;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAddressMapper implements RowMapper<UserAddress> {

    @Override
    public UserAddress map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new UserAddress(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("street_address"),
                rs.getString("zip_code"),
                rs.getString("district")
        );
    }
}
