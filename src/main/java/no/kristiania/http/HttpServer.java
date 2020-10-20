package no.kristiania.http;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {
    private File documentRoot;
    private List<String> memberNames = new ArrayList<>();
    private final MemberDao memberDao;

    public HttpServer(int port, DataSource dataSource) throws IOException {

        memberDao = new MemberDao(dataSource);

        ServerSocket serverSocket = new ServerSocket(port);

        new Thread(() ->{
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    handleRequest(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public static void main(String[] args) throws IOException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/teammembers");
        dataSource.setUser("memberadmin");
        dataSource.setPassword("V0E5!M@7eaM!");

        HttpServer server = new HttpServer(8080, dataSource);
        server.setDocumentRoot(new File("src/main/resources"));
    }

    private void handleRequest(Socket clientSocket) throws IOException, SQLException {
        String requestLine = HttpMessage.readLine(clientSocket);
        System.out.println(requestLine);

        String requestMethod = requestLine.split(" ")[0];
        String requestTarget = requestLine.split(" ")[1];

        if(requestMethod.equals("POST")){
            HttpMessage requestMessage = new HttpMessage(requestLine);
            requestMessage.readHeaders(clientSocket);

            int contentLength = Integer.parseInt(requestMessage.getHeader("Content-Length"));
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < contentLength; i++) {
                body.append((char)clientSocket.getInputStream().read());
            }
            QueryString requestForm = new QueryString(body.toString());
            Member member = new Member();
            member.setName(requestForm.getParameter("full_name"));
            memberDao.insert(member);

            HttpMessage responseMessage = new HttpMessage("HTTP/1.1 302 Redirect");
            responseMessage.setHeader("Location", "http://localhost:8080/index.html");
            responseMessage.setHeader("Connection", "close");
            responseMessage.setHeader("Content-Length", "2");
            responseMessage.write(clientSocket);
            clientSocket.getOutputStream().write("OK".getBytes());
            return;
        } else if(requestMethod.equals("GET")){
            if(requestTarget.equals("/")){
                HttpMessage responseMessage = new HttpMessage("HTTP/1.1 302 Redirect");
                responseMessage.setHeader("Location", "http://localhost:8080/index.html");
                responseMessage.setHeader("Connection", "close");
                responseMessage.setHeader("Content-Length", "2");
                responseMessage.write(clientSocket);
                clientSocket.getOutputStream().write("OK".getBytes());
            }
        }

        String responseCode = null;
        String body = null;

        int questionPos = requestTarget.indexOf('?');

        if(requestTarget.equals("/projectMembers")){
            body = "<ul>";
            for(Member member : memberDao.list()){
                body += "<li>" + member.getName() + "</li>";
            }
            body += "</ul>";

        } else if (questionPos != -1){
            QueryString queryString = new QueryString(requestTarget.substring(questionPos + 1));
            responseCode = queryString.getParameter("status");
            body = queryString.getParameter("body");
        }else if(!requestTarget.equals("/echo")) {
            File targetFile = new File(documentRoot, requestTarget);

            if(!targetFile.exists()){
                writeResponse(clientSocket, "404", requestTarget + " not found");
                return;
            }

            HttpMessage responseMessage = new HttpMessage("HTTP/1.1 200 OK");
            responseMessage.setHeader("Content-Length", String.valueOf(targetFile.length()));
            responseMessage.setHeader("Content-Type", "text/html");
            responseMessage.setHeader("Connection", "close");

            if (targetFile.getName().endsWith(".txt")){
                responseMessage.setHeader("Content-Type", "text/plain");
            }

            if(targetFile.getName().endsWith(".css")){
                responseMessage.setHeader("Content-Type", "text/css");
            }

            responseMessage.write(clientSocket);

            try (FileInputStream inputStream = new FileInputStream(targetFile)){
                inputStream.transferTo(clientSocket.getOutputStream());
            }

        }

        if(body == null) body = "Hello World";
        if(responseCode == null) responseCode = "200";


        writeResponse(clientSocket, responseCode, body);
    }

    private static void writeResponse(Socket clientSocket, String responseCode, String body) throws IOException {
        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 " + responseCode + " OK");
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.setHeader("Content-Type", "text/plain");
        responseMessage.setHeader("Connection", "close");
        responseMessage.write(clientSocket);
        clientSocket.getOutputStream().write(body.getBytes());

    }

    public void setDocumentRoot(File documentRoot) {
        this.documentRoot = documentRoot;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }
}
