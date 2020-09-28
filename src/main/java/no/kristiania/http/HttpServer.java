package no.kristiania.http;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private File documentRoot;

    public HttpServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        new Thread(() ->{
            try {
                Socket socket = serverSocket.accept();
                handleRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }

    public static void main(String[] args) throws IOException {
        new HttpServer(8080);
    }

    private static void handleRequest(Socket socket) throws IOException {
        String requestLine = HttpClient.readLine(socket);
        System.out.println(requestLine);

        String requestTarget = requestLine.split(" ")[1];
        String responseCode = null;
        String body = null;

        int questionPos = requestTarget.indexOf('?');
        if (questionPos != -1){
            QueryString queryString = new QueryString(requestTarget.substring(questionPos + 1));
            responseCode = queryString.getParameter("status");
            body = queryString.getParameter("body");
        }

        if(body == null) body = "Hello World";
        if(responseCode == null) responseCode = "200";


        writeResponse(socket, responseCode, body);
    }

    private static void writeResponse(Socket clientSocket, String responseCode, String body) throws IOException {
        String response = "HTTP/1.1 " + responseCode + " OK\r\n" +
                "Content-Length: "+ body.length() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes());
    }

    public void setDocumentRoot(File documentRoot) {
        this.documentRoot = documentRoot;
    }
}
