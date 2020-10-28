package no.kristiania.http;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import no.kristiania.database.ProjectTaskDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class HttpServer {
    private final List<String> memberNames = new ArrayList<>();
    private final MemberDao memberDao;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final Map<String, HttpController> controllers;
    private final ServerSocket serverSocket;

    public HttpServer(int port, DataSource dataSource) throws IOException {
        memberDao = new MemberDao(dataSource);
        ProjectTaskDao projectTaskDao = new ProjectTaskDao(dataSource);
        controllers = Map.of(
                "/newProjectTasks", new ProjectTaskPostController(projectTaskDao),
                "/projectTasks", new ProjectTaskGetController(projectTaskDao)
        );

        serverSocket = new ServerSocket(port);

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

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("pgr203.properties")) {
            properties.load(fileReader);
        }
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        HttpServer server = new HttpServer(8080, dataSource);

        logger.info("Started on {}", "http://localhost:8080/");

    }

    private void handleRequest(Socket clientSocket) throws IOException, SQLException {
        String requestLine = HttpMessage.readLine(clientSocket);
        System.out.println(requestLine);

        String requestMethod = requestLine.split(" ")[0];
        String requestTarget = requestLine.split(" ")[1];
        int questionPos = requestTarget.indexOf('?');
        String requestPath = questionPos != -1 ? requestTarget.substring(0, questionPos) : requestTarget;

        if(requestMethod.equals("POST")){
            if(requestPath.equals("/members")){
                handlePostMember(clientSocket, requestLine);
            }else {
                getController(requestPath).handle(requestLine, clientSocket);
            }
        } else {
            if(requestPath.equals("/echo")) {
                handleEchoRequest(clientSocket, requestTarget, questionPos);

            }else if(requestPath.equals("/projectMembers") || requestPath.equals("/")){
                handleGetMembers(clientSocket, requestTarget);
            }else{
                HttpController controller = controllers.get(requestPath);

                if(controller != null ){
                    controller.handle(requestLine, clientSocket);
                }

                handleFileRequest(clientSocket, requestPath);
            }
        }
    }


    private void handleEchoRequest(Socket clientSocket, String requestTarget, int questionPos) throws IOException {
        String responseCode = "200";
        String body = "<h1>Hello World!<h1>";
        if (questionPos != -1) {
            QueryString queryString = new QueryString(requestTarget.substring(questionPos + 1));
            if (queryString.getParameter("status") != null) {
                responseCode = queryString.getParameter("status");
            }
            if (queryString.getParameter("body") != null) {
                body = queryString.getParameter("body");
            }
        }

            HttpMessage responseMessage = new HttpMessage("HTTP/1.1 " + responseCode + " OK");
            responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
            responseMessage.setHeader("Content-Type", "text/plain");
            responseMessage.setBody(body);
            responseMessage.write(clientSocket);

    }

    private void handleGetMembers(Socket clientSocket, String requestTarget) throws SQLException, IOException {
        StringBuilder body = new StringBuilder("<ul>");
        for(Member member : memberDao.list()){
            body.append("<li>").append(member.getName()).append(" (Email: ").append(member.getEmail()).append(") </li>");
        }
        body.append("</ul>");

        HttpMessage responseMessage;
        if(requestTarget.equals("/")){
            responseMessage = new HttpMessage("HTTP/1.1 302 Redirect");
            responseMessage.setHeader("Location", "http://localhost:8080/index.html");
        }else{
            responseMessage = new HttpMessage("HTTP/1.1 200 OK");
            responseMessage.setHeader("Content-Type", "text/html");
        }
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.setBody(body.toString());
        responseMessage.write(clientSocket);


    }

    private HttpController getController(String requestTarget) {
        return controllers.get(requestTarget);
    }

    private void handlePostMember(Socket clientSocket, String requestLine) throws IOException, SQLException {
        HttpMessage requestMessage = new HttpMessage(requestLine);
        requestMessage.readHeaders(clientSocket);

        String body = HttpMessage.readBody(clientSocket, requestMessage.getHeader("Content-Length"));

        QueryString requestForm = new QueryString(body);
        memberNames.add(requestForm.getParameter("full_name"));

        Member member = new Member();
        member.setName(requestForm.getParameter("full_name"));
        member.setEmail(requestForm.getParameter("email_address"));
        memberDao.insert(member);


        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 302 Redirect");
        responseMessage.setHeader("Location", "http://localhost:8080/projectMembers.html");
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.write(clientSocket);
    }

    private void handleFileRequest(Socket clientSocket, String requestTarget) throws IOException {
        URL requestedResource = getClass().getResource("/public" + requestTarget);
        String rootPath = getClass().getResource("/public").getPath();
        if(requestedResource == null || !requestedResource.getPath().startsWith(rootPath)){
                writeResponse(clientSocket, "404", requestTarget + " not found");
                return;
        }
        try(InputStream inputStream = getClass().getResourceAsStream("/public" + requestTarget)){
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            inputStream.transferTo(buffer);


            HttpMessage responseMessage = new HttpMessage("HTTP/1.1 200 OK");

            if (requestTarget.endsWith(".txt")){
                responseMessage.setHeader("Content-Type", "text/plain");
            }

            if(requestTarget.endsWith(".css")){
                responseMessage.setHeader("Content-Type", "text/css");
            }

            if(requestTarget.endsWith(".html")){
                responseMessage.setHeader("Content-Type", "text/html");
            }

            responseMessage.setHeader("Content-Length", String.valueOf(buffer.toByteArray().length));

            responseMessage.write(clientSocket);
            clientSocket.getOutputStream().write(buffer.toByteArray());
        }
    }

    private static void writeResponse(Socket clientSocket, String responseCode, String body) throws IOException {
        HttpMessage responseMessage = new HttpMessage("HTTP/1.1 " + responseCode + " OK");
        responseMessage.setHeader("Content-Length", String.valueOf(body.length()));
        responseMessage.setHeader("Content-Type", "text/plain");
        responseMessage.write(clientSocket);
        clientSocket.getOutputStream().write(body.getBytes());

    }

    public List<String> getMemberNames() {
        return memberNames;
    }
}
