package com.daryl.db;

import com.daryl.api.Image;
import com.daryl.db.mappers.ImageMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.ArrayList;

@RegisterRowMapper(ImageMapper.class)
public interface ImageDAO {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS product_images (" +
            "id SERIAL PRIMARY KEY," +
            "product_id INT," +
            "type VARCHAR(64)," +
            "filename VARCHAR(128)," +
            "FOREIGN KEY(product_id) REFERENCES product(id) ON DELETE CASCADE)")
    void createTable();

    @SqlQuery("SELECT * FROM product_images WHERE id = :id")
    Image getImage(@Bind("id") int id);

    @SqlQuery("SELECT * FROM product_images WHERE product_id = :id")
    ArrayList<Image> getImagesForProduct(@Bind("id") int id);

    @SqlUpdate("INSERT INTO product_images(product_id, type, filename) VALUES(:id, :type, :filename)")
    @GetGeneratedKeys("id")
    int saveFile(@Bind("id") int productId, @Bind("type") String type, @Bind("filename") String filename);

    @SqlUpdate("DELETE FROM product_images WHERE id = :id")
    boolean deleteImage(@Bind("id") int imageId);
}
