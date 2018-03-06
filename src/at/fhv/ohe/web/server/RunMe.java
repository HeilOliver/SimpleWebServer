package at.fhv.ohe.web.server;

public class RunMe {

    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Wrong argument count");

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Argument 0 cant be parsed");
        }

        WebServer webServer = new WebServer(port);
        webServer.addConsoleOut(System.out::println);
        webServer.Start();
    }
}
