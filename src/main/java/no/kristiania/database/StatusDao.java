package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatusDao extends AbstractDao<Status>{

    public StatusDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(Status status) throws SQLException {
        insert(status, "INSERT INTO statuses (name) values (?)");
    }

    public List<Status> list() throws SQLException {
        return list("SELECT * FROM statuses");
    }

    @Override
    protected void mapEntityToPreparedStatement(PreparedStatement statement, Status entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.executeUpdate();
    }

    @Override
    protected Status mapRow(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setName(rs.getString("name"));
        status.setId(rs.getInt("id"));
        return status;
    }
}
