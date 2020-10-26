package no.kristiania.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectTaskDaoTest {

    private ProjectTaskDao projectTaskDao;
    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        projectTaskDao = new ProjectTaskDao(dataSource);
    }

    @Test
    void shouldListAllProjectTasks() throws SQLException {
        ProjectTask projectTask1 = exampleTask();
        ProjectTask projectTask2 = exampleTask();
        projectTaskDao.insert(projectTask1);
        projectTaskDao.insert(projectTask2);
        assertThat(projectTaskDao.list())
                .extracting(ProjectTask::getName)
                .contains(projectTask1.getName(), projectTask2.getName());
    }

    private ProjectTask exampleTask() {
        ProjectTask task = new ProjectTask();
        task.setName(exampleTaskName());
        return task;
    }

    private String exampleTaskName() {
        String[] names = {"Vaske Tak", "Opprydding", "Maling av tak", "Salg"};
        return names[random.nextInt(names.length)];
    }

    @Test
    void shouldRetrieveAllProjectTaskProperties() throws SQLException {
        projectTaskDao.insert(exampleTask());
        projectTaskDao.insert(exampleTask());
        ProjectTask task = exampleTask();
        projectTaskDao.insert(task);
        assertThat(task).hasNoNullFieldsOrProperties();
        assertThat(projectTaskDao.retrieve(task.getId()))
                .usingRecursiveComparison()
                .isEqualTo(task);

    }


}
