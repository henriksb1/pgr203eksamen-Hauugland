package no.kristiania.http;


import java.io.IOException;
import java.net.Socket;

public class HttpClient {

    private String responseBody;
    private HttpMessage responseMessage;

    public HttpClient(String hostname, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(hostname, port);

        HttpMessage requestMessage = new HttpMessage("GET " + requestTarget + " HTTP/1.1");
        requestMessage.setHeader("Host", hostname);
        requestMessage.write(socket);

        responseMessage = HttpMessage.read(socket);

        responseBody = HttpMessage.readBody(socket, getResponseHeader("Content-Length"));
    }

    public static void main(String[] args) throws IOException {
        String hostname = "urlecho.appspot.com";
        int port = 80;
        String requestTarget = "/echo?status=200&body=Hello%20world!";
        new HttpClient(hostname, port, requestTarget);
    }


    public int getResponseCode() {
        String[] responseLineParts = responseMessage.getStartLine().split(" ");
        int responseCode = Integer.parseInt(responseLineParts[1]);
        return responseCode;
    }

    public String getResponseHeader(String headerName) {
        return responseMessage.getHeader(headerName);
    }

    public String getResponseBody() {
        return responseBody;
    }
}
