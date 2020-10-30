package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends IdEntity> {
    protected final DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected T retrieve(Long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    } else {
                        return null;
                    }

                }
            }
        }
    }

    public List<T> list(String sql) throws SQLException {
        List<T>  list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                }
            }
        }
        return list;
    }

    public void insert(T entity, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            )){
                mapEntityToPreparedStatement(statement, entity);

                fillGeneratedKeys(entity, statement);
            }
        }
    }


    protected abstract void mapEntityToPreparedStatement(PreparedStatement statement, T entity) throws SQLException;

    public static void fillGeneratedKeys(IdEntity entity, PreparedStatement statement) throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            generatedKeys.next();
            entity.setId(generatedKeys.getLong("id"));
        }
    }

    protected abstract T mapRow(ResultSet rs) throws SQLException;
}
