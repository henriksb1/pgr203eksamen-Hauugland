package no.kristiania.http;

import no.kristiania.database.ProjectTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectMemberOptionsController implements HttpController {
    public ProjectMemberOptionsController(ProjectTaskDao projectTaskDao) {
    }

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException, SQLException {
        HttpMessage requestMessage = new HttpMessage(requestLine);
        requestMessage.readHeaders(clientSocket);

        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 200 OK");
        String body = "<option>A</option><option>B</option>";
        responseMessage.setBody(body);
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.write(clientSocket);
    }
}
