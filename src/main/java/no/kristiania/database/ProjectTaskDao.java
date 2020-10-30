package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class ProjectTaskDao extends AbstractDao<ProjectTask>{

    public ProjectTaskDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(ProjectTask task) throws SQLException {
        insert(task, "INSERT INTO project_tasks (task_name) values (?)");
    }

    public ProjectTask retrieve(long id) throws SQLException {
        return retrieve(id, "SELECT * FROM project_tasks WHERE id = ?");
    }

    public List<ProjectTask> list() throws SQLException {
        return list("SELECT * FROM project_tasks");
    }

    @Override
    protected ProjectTask mapRow(ResultSet rs) throws SQLException {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setName(rs.getString("task_name"));
        projectTask.setId(rs.getLong("id"));
        return projectTask;
    }
}
