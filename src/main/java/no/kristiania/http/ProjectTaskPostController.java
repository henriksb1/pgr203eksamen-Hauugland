package no.kristiania.http;

import no.kristiania.database.ProjectTask;
import no.kristiania.database.ProjectTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectTaskPostController implements HttpController {
    private ProjectTaskDao projectTaskDao;

    public ProjectTaskPostController(ProjectTaskDao projectTaskDao) {

        this.projectTaskDao = projectTaskDao;
    }

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException, SQLException {
        HttpMessage requestMessage = new HttpMessage(requestLine);
        requestMessage.readHeaders(clientSocket);

        String body = HttpMessage.readBody(clientSocket, requestMessage.getHeader("Content-Length"));

        QueryString requestForm = new QueryString(body);
        ProjectTask projectTask = new ProjectTask();
        projectTask.setName(requestForm.getParameter("task_name"));
        projectTaskDao.insert(projectTask);


        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 302 Redirect");
        responseMessage.setHeader("Location", "http://localhost:8080/index.html");
        responseMessage.setHeader("Connection", "close");
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.write(clientSocket);

    }
}
