package no.kristiania.http;

import no.kristiania.database.Member;

import java.io.IOException;
import java.net.Socket;

public class ProjectTaskGetController implements HttpController {

    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException {
        String body = "<ul>";

        body += "</ul>";


            HttpMessage responseMessage = new HttpMessage("HTTP/1.1 200 OK");
            responseMessage.setHeader("Connection", "close");
            responseMessage.setHeader("Content-Type", "text/html");
            responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
            responseMessage.setBody(body);
            responseMessage.write(clientSocket);

        body += "</ul>";


    }
}
