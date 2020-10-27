package no.kristiania.http;

import no.kristiania.database.Member;

import java.io.IOException;
import java.net.Socket;

public class ProjectTaskPostController implements HttpController {
    @Override
    public void handle(String requestLine, Socket clientSocket) throws IOException {
        HttpMessage requestMessage = new HttpMessage(requestLine);
        requestMessage.readHeaders(clientSocket);

        int contentLength = Integer.parseInt(requestMessage.getHeader("Content-Length"));
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            body.append((char) clientSocket.getInputStream().read());
        }

        QueryString requestForm = new QueryString(body.toString());

        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 302 Redirect");
        responseMessage.setHeader("Location", "http://localhost:8080/index.html");
        responseMessage.setHeader("Connection", "close");
        responseMessage.setHeader("Content-Length", "2");
        responseMessage.write(clientSocket);

    }
}
