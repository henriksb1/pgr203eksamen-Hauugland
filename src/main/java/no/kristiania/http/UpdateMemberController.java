package no.kristiania.http;

import no.kristiania.database.MemberDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class UpdateMemberController  implements HttpController{
    public UpdateMemberController(MemberDao memberDao) {
    }

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException, SQLException {

    }
}
