package no.kristiania.controllers;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public interface HttpController {

    void handle(String requestLine, Socket clientSocket) throws IOException, SQLException;
}
