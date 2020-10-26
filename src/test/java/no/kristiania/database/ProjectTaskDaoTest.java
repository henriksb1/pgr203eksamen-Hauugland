package no.kristiania.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectTaskDaoTest {

    private ProjectTaskDao projectTaskDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        projectTaskDao = new ProjectTaskDao(dataSource);
    }

    @Test
    void shouldListAllProjectTasks(){
        ProjectTask projectTask1 = exampleTask();
        ProjectTask projectTask2 = exampleTask();
        assertThat(projectTaskDao.list())
                .extracting(ProjectTask::getName)
                .contains(projectTask1.getName(), projectTask2.getName());
    }

    private ProjectTask exampleTask() {
        return null;
    }

    @Test
    void shouldRetrieveAllProjectTaskProperties(){

    }


}
