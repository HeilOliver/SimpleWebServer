package at.fhv.ohe.web.server;

public class WebHeaderFactory {

    private WebHeaderFactory() {
    }

    public static String getHeader(int statusCode, int contentLength) {
//        HTTP/1.1 404 Not Found
//        Content-Length: 1635
//        Content-Type: text/html
//        Connection: close

        return String.format("HTTP/1.1 %d %s\n" +
                "Content-Length: %d\n" +
                "Content-Type: text/html\n" +
                "Connection: close", statusCode, "OK", contentLength);
    }
}
