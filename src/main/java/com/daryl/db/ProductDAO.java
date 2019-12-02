package com.daryl.db;

import com.daryl.api.Product;
import com.daryl.db.mappers.ProductMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(ProductMapper.class)
public interface ProductDAO {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS product(" +
            "id SERIAL PRIMARY KEY,"+
            "name VARCHAR(256) UNIQUE NOT NULL,"+
            "description VARCHAR(256) NOT NULL,"+
            "amount INT NOT NULL);")
    void createTable();

    @SqlQuery("SELECT * FROM product WHERE id = :id")
    Product getProduct(@Bind("id") int id);

    @SqlQuery("SELECT * FROM product")
    List<Product> getAllProducts();

    @SqlUpdate("INSERT INTO product(name, description, amount) VALUES (:name, :desc, :amount")
    @GetGeneratedKeys("id")
    void addProduct(@Bind("name") String name, @Bind("desc") String description, @Bind("amount") int amount);

    @SqlUpdate("UPDATE product SET name = :name, SET description = :desc, SET amount = :amount WHERE id = :id")
    void updateProduct(@Bind("name") String name, @Bind("desc") String description,
                       @Bind("amount") int amount, @Bind("id") int id);

    @SqlUpdate("DELETE FROM product WHERE id = :id")
    void deleteProduct(@Bind("id") int id);
}
