package com.daryl.db.mappers;

import com.daryl.api.Image;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageMapper implements RowMapper<Image> {

    @Override
    public Image map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Image(rs.getInt("id"),
                rs.getInt("product_id"),
                rs.getString("filename"),
                rs.getString("type"));
    }
}
