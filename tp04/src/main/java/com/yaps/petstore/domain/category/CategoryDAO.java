package com.yaps.petstore.domain.category;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.yaps.utils.dao.AbstractDAO;


/**
 * This class does all the database access for the class Category.
 *
 * @see Category
 */
public class CategoryDAO extends AbstractDAO<Category> {
    public CategoryDAO(Connection connection) {
        super(connection, "category", Category.class);
    }

    protected Category extractSpecificData(List<Object> argList) {
        return new Category((String) argList.get(0), (String) argList.get(1), (String) argList.get(2));
    }

}
