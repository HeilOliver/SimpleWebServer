package at.fhv.ohe.web.server;

import java.io.*;
import java.net.Socket;

class WebClientHandler implements Runnable{

    private Socket socket;

    WebClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s = bufferedReader.readLine();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String[] split = s.split(" ");
            if (split[0].equals("GET")) {

                String resource = WebResourceHandle.getResource(split[1]);
                String header;
                if (resource == null) {
                    resource = WebResourceHandle.getError(404);
                    if (resource == null) resource = "";
                    header = WebHeaderFactory.getHeader(404, resource.length());
                } else {
                    header = WebHeaderFactory.getHeader(200, resource.length());
                }

                out.println(header);
                out.println("");
                out.println(resource);
            } else {
                String error = WebResourceHandle.getError(400);
                String header = WebHeaderFactory.getHeader(400, error.length());
                out.println(header);
                out.println("");
                out.println(error);
            }
        } catch (IOException ignored) {

        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
