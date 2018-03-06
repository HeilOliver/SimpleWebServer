package at.fhv.ohe.web.server;

import javax.print.attribute.standard.RequestingUserName;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer{

    private int port;
    private boolean enable;
    private Thread clientHandler;
    private ConsoleOut consoleOut;
    private ExecutorService threadPool;
    private ServerSocket serverSocket;

    WebServer(int port) {
        if (port > 65535 || port < 0)
            throw new IllegalArgumentException("Port Number must be in range 0 < port < 65535 ");
        this.port = port;
    }

    public void Start() {
        if (enable) return;
        enable = true;
        threadPool = Executors.newCachedThreadPool();
        clientHandler = new Thread(this::clientHandel);
        clientHandler.setName("WebServerThread @ " + port);
        clientHandler.setDaemon(true);
        //clientHandler.start();
        clientHandler.run();
    }

    public void Stop() {
        if (!enable) return;
        enable = false;
        threadPool.shutdown();
        serverSocket.notify();
        try {
            clientHandler.join();
        } catch (InterruptedException e) {
            printError(e.getMessage());
        }
        clientHandler = null;
    }

    private void clientHandel() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            printError(e.getMessage());
            enable = false;
            return;
        }
        printConsole("Server Started @" + port);
        while (enable) {
            printConsole("Awaiting Connection");
            try {
                Socket accept = serverSocket.accept();
                printConsole("New Connection @" + accept.getLocalAddress().getHostName());
                WebClientHandler webClientHandler = new WebClientHandler(accept);
                threadPool.execute(webClientHandler);
            } catch (IllegalBlockingModeException ignore) {
                // Ignore this because it is thrown when an
                // notify occurred and no connection is available
            } catch (IOException e) {
                printError(e.getMessage());
                enable = false;
                return;
            }
        }

        if (serverSocket.isClosed()) return;
        try {
            serverSocket.close();
        } catch (IOException e) {
            printError(e.getMessage());
        }
    }

    private void printConsole(String s) {
        if (consoleOut == null) return;

        consoleOut.webServerOut(s);
    }

    private void printError(String s) {
        if (consoleOut == null) return;
        consoleOut.webServerOut(s);
    }

    public void addConsoleOut(ConsoleOut consoleOut) {
        this.consoleOut = consoleOut;
    }

    public interface ConsoleOut {
        void webServerOut(String s);
    }

}
