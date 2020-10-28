package no.kristiania.http;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    private JdbcDataSource dataSource;

    @BeforeEach
    void setUp(){
        dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
    }

    @Test
    void shouldReturnSuccessfulErrorCode() throws IOException {
        HttpServer server = new HttpServer(10001, dataSource);
        HttpClient client = new HttpClient("localhost", 10001, "/echo");
        assertEquals(200, client.getResponseCode());
    }

    @Test
    void shouldReturnUnsuccessfulErrorCode() throws IOException {
        HttpServer server = new HttpServer(10002, dataSource);
        HttpClient client = new HttpClient("localhost", 10002, "/echo?status=404");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReturnHttpHeaders() throws IOException {
        HttpServer server = new HttpServer(10003, dataSource);
        HttpClient client = new HttpClient("localhost", 10003, "/echo?body=HelloWorld");
        assertEquals("10", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnFileContent() throws IOException {
        HttpServer server = new HttpServer(10005, dataSource);
        File documentRoot = new File("target/test-classes/public");
        documentRoot.mkdirs();
        String fileContent = "Hello " + new Date();
        Files.writeString(new File(documentRoot, "index.html").toPath(), fileContent);
        HttpClient client = new HttpClient("localhost", 10005, "/index.html");
        assertEquals(fileContent, client.getResponseBody());
    }

    @Test
    void shouldReturn404onMissingFile() throws IOException {
        HttpServer server = new HttpServer(10006, dataSource);
        File documentRoot = new File("target/test-classes");
        HttpClient client = new HttpClient("localhost", 10006, "/missingFile");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReturn404onFileOutsideDocumentRoot() throws IOException {
        HttpServer server = new HttpServer(10007, dataSource);
        File documentRoot = new File("target/test-classes");
        Files.writeString(new File(documentRoot, "secret.txt").toPath(), "Super secret file");
        HttpClient client = new HttpClient("localhost", 10007, "/../secret.txt");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReturnCorrectContentType() throws IOException {
        HttpServer server = new HttpServer(10008, dataSource);
        File documentRoot = new File("target/test-classes/public");
        documentRoot.mkdirs();
        Files.writeString(new File(documentRoot, "plain.txt").toPath(), "Plain text");
        HttpClient client = new HttpClient("localhost", 10008, "/plain.txt");
        assertEquals("text/plain", client.getResponseHeader("Content-Type"));
    }

    @Test
    void shouldPostMember() throws IOException {
        HttpServer server = new HttpServer(10009, dataSource);
        QueryString member = new QueryString("");
        member.addParameter("full_name", "Marius");
        member.addParameter("email_address", "austheim.marius@gmail.com");
        new HttpClient("localhost", 10009, "/members", "POST", member);
        assertEquals(List.of("Marius"), server.getMemberNames());
    }

    @Test
    void shouldDisplayExistingMember() throws IOException, SQLException {
        HttpServer server = new HttpServer(10011, dataSource);
        MemberDao memberDao = new MemberDao(dataSource);
        Member member = new Member();
        member.setName("Petter");
        memberDao.insert(member);
        HttpClient client = new HttpClient("localhost", 10011, "/projectMembers");
        assertThat(client.getResponseBody().contains("<li>Petter</li>"));
    }

    @Test
    void shouldPostNewTask() throws IOException {
        HttpServer server = new HttpServer(10012, dataSource);
        QueryString task = new QueryString("");
        task.addParameter("task_name", "Male");
        task.addParameter("color", "black");
        HttpClient postClient = new HttpClient("localhost", 10012, "/newProjectTasks", "POST", task);
        assertEquals(302, postClient.getResponseCode());

        HttpClient getClient = new HttpClient("localhost", 10012, "/projectTasks");
        assertThat(getClient.getResponseBody().contains("<li>Male</li>"));


    }

}