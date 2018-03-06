package at.fhv.ohe.web.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 class WebResourceHandle {

    static String getResource(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.length() == 0) path = "index.html";
        return get("resources/assets/" + path);
    }

    private static String get(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();

        } catch (IOException ignore) {
            return null;
        }
    }

    static String getError(int errorCode) {
        return get("resources/error/" + errorCode + ".html");
    }
}
