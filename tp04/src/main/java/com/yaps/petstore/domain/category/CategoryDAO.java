package com.yaps.petstore.domain.category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.yaps.utils.dao.AbstractDAO;


/**
 * This class does all the database access for the class Category.
 *
 * @see Category
 */
public class CategoryDAO extends AbstractDAO<Category> {
    private static final String COLUMNS[] = { "name", "description" };

    public CategoryDAO(Connection connection) {
        super(connection, "category", COLUMNS);
    }

    @Override
    protected void fillPreparedStatement(PreparedStatement pst, Category category, int[] fieldsOrder)
            throws SQLException {
        pst.setObject(fieldsOrder[0], category.getId());
        pst.setObject(fieldsOrder[1], category.getName());
        pst.setObject(fieldsOrder[2], category.getDescription());
    }

    protected Category extractData(ResultSet res) throws SQLException {
        String id = res.getString("id");
        String name = res.getString("name");
        String description = res.getString("description");
        return new Category(id, name, description);
    }

}
