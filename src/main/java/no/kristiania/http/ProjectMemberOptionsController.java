package no.kristiania.http;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import no.kristiania.database.ProjectTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ProjectMemberOptionsController implements HttpController {
    private MemberDao memberDao;

    public ProjectMemberOptionsController(MemberDao memberDao) {
        this.memberDao = memberDao;
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

    public String getBody() throws SQLException {
        String body = "";
        for (Member member : memberDao.list()) {
            body += "<option value=" + member.getId() + ">" + member.getName() + "</option>";
        }

        return memberDao.list()
                .stream().map(m -> "<option value=" + m.getId() + ">" + m.getName() + "</option>")
                .collect(Collectors.joining());
    }
}
