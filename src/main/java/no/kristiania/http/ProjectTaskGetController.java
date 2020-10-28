package no.kristiania.http;

import no.kristiania.database.ProjectTask;
import no.kristiania.database.ProjectTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectTaskGetController implements HttpController {

    private ProjectTaskDao projectTaskDao;

    public ProjectTaskGetController(ProjectTaskDao projectTaskDao) {

        this.projectTaskDao = projectTaskDao;
    }

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException, SQLException {
        StringBuilder body = new StringBuilder("<ul>");
        for (ProjectTask task : projectTaskDao.list()) {
            body.append("<li>").append(task.getName()).append("</li>");
        }

        body.append("</ul>");


            HttpMessage responseMessage = new HttpMessage("HTTP/1.1 200 OK");
            responseMessage.setHeader("Connection", "close");
            responseMessage.setHeader("Content-Type", "text/html");
            responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
            responseMessage.setBody(body.toString());
            responseMessage.write(clientSocket);


    }
}
