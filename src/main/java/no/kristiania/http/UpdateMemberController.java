package no.kristiania.http;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import no.kristiania.database.ProjectTask;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class UpdateMemberController  implements HttpController{
    private MemberDao memberDao;

    public UpdateMemberController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException, SQLException {
        HttpMessage response = handle(requestLine);
        response.write(clientSocket);

    }

    public HttpMessage handle(String requestLine) throws SQLException {
        QueryString requestParameter = new QueryString(requestLine);

        Integer memberId = Integer.valueOf(requestParameter.getParameter("memberId"));
        Integer taskId = Integer.valueOf(requestParameter.getParameter("taskId"));
        Member member = memberDao.retrieve(memberId);
        member.setTaskId(taskId);

        memberDao.update(member);

        HttpMessage redirect = new HttpMessage("HTTP/1.1 302 Redirect");
        redirect.setHeader("Location", "http://localhost:8080/index.html");
        return redirect;
    }

}
