package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {
    @Test
    void shouldReturnSuccessfulErrorCode() throws IOException {
        HttpServer server = new HttpServer(10001);
        HttpClient client = new HttpClient("localhost", 10001, "/echo");
        assertEquals(200, client.getResponseCode());
    }

    @Test
    void shouldReturnUnsuccessfulErrorCode() throws IOException {
        HttpServer server = new HttpServer(10002);
        HttpClient client = new HttpClient("localhost", 10002, "/echo?status=404");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReturnHttpHeaders() throws IOException {
        HttpServer server = new HttpServer(10003);
        HttpClient client = new HttpClient("localhost", 10003, "/echo?body=HelloWorld");
        assertEquals("10", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnFileContent() throws IOException {
        HttpServer server = new HttpServer(10005);
        File documentRoot = new File("target");
        server.setDocumentRoot(documentRoot);
        String fileContent = "Hello " + new Date();
        Files.writeString(new File(documentRoot, "index.html").toPath(), fileContent);
        HttpClient client = new HttpClient("localhost", 10005, "/index.html");
        assertEquals(fileContent, client.getResponseBody());
    }

    @Test
    void ShouldReturn404onMissingFile() throws IOException {
        HttpServer server = new HttpServer(10006);
        server.setDocumentRoot(new File("target"));
        HttpClient client = new HttpClient("localhost", 10006, "/missingFile");
        assertEquals(404, client.getResponseCode());
    }
    @Test
    void ShouldReturnCorrectContentType() throws IOException {
        HttpServer server = new HttpServer(10007);
        File documentRoot = new File("target");
        server.setDocumentRoot(documentRoot);
        Files.writeString(new File(documentRoot, "plain.txt").toPath(), "Plain text");
        HttpClient client = new HttpClient("localhost", 10007, "/plain.txt");
        assertEquals("text/plain", client.getResponseHeader("Content-Type"));
    }

    @Test
    void ShouldPostMember() throws IOException {
        HttpServer server = new HttpServer(10008);
        QueryString member = new QueryString("");
        member.addParameter("full_name", "Marius");
        member.addParameter("email_address", "austheim.marius@gmail.com");
        new HttpClient("localhost", 10008, "/members", "POST", member);
        assertEquals(List.of("Marius"), server.getMemberNames());
    }

    @Test
    void ShouldDisplayExistingMember() throws IOException {
        HttpServer server = new HttpServer(10009);
        server.getMemberNames().add("Petter");
        HttpClient client = new HttpClient("localhost", 10009, "/projectMembers");
        assertEquals("<ul><li>Petter</li></ul>", client.getResponseBody());
    }
}