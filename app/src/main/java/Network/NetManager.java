package Network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016-07-10.
 */
public class NetManager {

    public static void registerPattern(String jsonStr) {
        try {
            URL url = new URL("http://192.168.40.222:3000/api/bits");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(jsonStr);
            out.close();
            httpCon.getInputStream();
        }
        catch (IOException e)
        {
        }
    }

    private static boolean chkRegister(String jsonStr) {
        try {
            URL url = new URL("http://192.168.40.222:3000/api/bits");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpCon.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(jsonStr);
            out.close();
            httpCon.getInputStream();
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
}