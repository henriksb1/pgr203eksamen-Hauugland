package no.kristiania.http;

import no.kristiania.database.MemberDao;
import no.kristiania.database.ProjectTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectMemberOptionsController implements HttpController {
    public ProjectMemberOptionsController(MemberDao memberDao) {
    }

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException, SQLException {
        HttpMessage requestMessage = new HttpMessage(requestLine);
        requestMessage.readHeaders(clientSocket);

        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 200 OK");
        String body = getBody();
        responseMessage.setBody(body);
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.write(clientSocket);
    }

    public String getBody() {
        return "<option>A</option><option>B</option>";
    }
}
