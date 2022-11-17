package com.yaps.petstore.catalog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.yaps.common.dao.AbstractDAO;
import com.yaps.common.dao.exception.YapsDataException;
import com.yaps.petstore.catalog.domain.Category;

/**
 * This class does all the database access for the class Category.
 *
 * @see Category
 */
@Repository
public class CategoryDAO extends AbstractDAO<Category> {

    private static final String FIELDS[] = { "name", "description" };

    protected CategoryDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "category", FIELDS);
    }



    @Override
    protected Object[] getFieldValues(Category entity) {
        return new Object[] {
            entity.getName(),
            entity.getDescription()
        };
    }

    @Override
    protected Category extractEntity(ResultSet res) {
        try {
        String id = extractString(res, "id");
        String name = extractString(res, "name");
        String description = extractString(res, "description");
        return new Category(id, name, description);
        } catch (SQLException e) {
            throw new YapsDataException("error in extraction", e);
        }
    }

}
