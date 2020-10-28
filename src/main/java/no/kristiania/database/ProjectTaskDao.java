package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectTaskDao {
    private final DataSource dataSource;

    public ProjectTaskDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void insert(ProjectTask task) throws SQLException {
        try (Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO project_tasks (task_name) values (?)",
                    Statement.RETURN_GENERATED_KEYS
            )){
                statement.setString(1, task.getName());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    task.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public ProjectTask retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project_tasks WHERE id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToTask(rs);
                    }else{
                        return null;
                    }

                }
            }
        }
    }

    private ProjectTask mapRowToTask(ResultSet rs) throws SQLException {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setName(rs.getString("task_name"));
        projectTask.setId(rs.getLong("id"));
        return projectTask;
    }

    public List<ProjectTask> list() throws SQLException {
        List<ProjectTask>  tasks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project_tasks")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        //ProjectTask projectTask = new ProjectTask();
                        //projectTask.setName(rs.getString(""));
                        tasks.add(mapRowToTask(rs));
                    }
                }
            }
        }
        return tasks;
    }
}
