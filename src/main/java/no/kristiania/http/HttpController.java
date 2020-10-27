package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;

public interface HttpController {

    void handle(String requestLine, Socket clientSocket) throws IOException;
}
