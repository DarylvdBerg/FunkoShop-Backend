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
            "price NUMERIC(11,2) NOT NULL,"+
            "amount INT NOT NULL);")
    void createTable();

    @SqlQuery("SELECT * FROM product WHERE id = :id")
    Product getProduct(@Bind("id") int id);

    @SqlQuery("SELECT * FROM product")
    List<Product> getAllProducts();

    @SqlUpdate("INSERT INTO product(name, description, price, amount) VALUES (:name, :desc, :price, :amount);")
    @GetGeneratedKeys("id")
    int addProduct(@Bind("name") String name,
                       @Bind("desc") String description,
                       @Bind("price") double price,
                       @Bind("amount") int amount);

    @SqlUpdate("UPDATE product SET name = :name, SET description = :desc" +
            "SET price = :price SET amount = :amount WHERE id = :id")
    boolean updateProduct(@Bind("name") String name, @Bind("desc") String description,
                          @Bind("amount") int amount,
                          @Bind("price") double price,
                          @Bind("id") int id);

    @SqlUpdate("DELETE FROM product WHERE id = :id")
    boolean deleteProduct(@Bind("id") int id);
}
