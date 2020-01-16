package com.daryl.db;

import com.daryl.api.UserAddress;
import com.daryl.db.mappers.UserAddressMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterRowMapper(UserAddressMapper.class)
public interface UserAddressDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS user_address (" +
            "id SERIAL PRIMARY KEY," +
            "user_id INT," +
            "street_address VARCHAR(64) NOT NULL," +
            "zip_code VARCHAR(6) NOT NULL," +
            "district VARCHAR(64) NOT NULL," +
            "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);")
    void createTable();

    @SqlUpdate("INSERT INTO user_address(user_id, street_address, zip_code, district) " +
            "VALUES(:user_id, :street_address, :zip_code, :district)")
    boolean create(@Bind("user_id") int id, @Bind("street_address") String streetAddress,
                   @Bind("zip_code") String zipCode,
                   @Bind("district") String district);

    @SqlUpdate("UPDATE user_address SET street_address = :street_address," +
            "zip_code = :zip_code, district = :district WHERE user_id = :id")
    boolean update(@Bind("street_address") String streetAddress,
                   @Bind("zip_code") String zipCode,
                   @Bind("district") String district,
                   @Bind("id") int userId);

    @SqlQuery("SELECT * FROM user_address WHERE user_id = :id")
    UserAddress getUserAddress(@Bind("id") int userId);
}
