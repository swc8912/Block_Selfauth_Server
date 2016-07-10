package Network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016-07-10.
 */
public class NetManager {

    public static void registerPattern(final String jsonStr) {
        new Thread(){
            @Override
            public void run() {
                Log.i("test","test111=");
                try {
                    StringBuilder result = new StringBuilder();
                    //JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,"myURL", null, new Response.Listener<JSONObject>();
                    URL url = new URL("http://192.168.40.222:3000/api/bits/hello");

                    HttpURLConnection conn;
                    //------------------
                    conn = (HttpURLConnection)url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //-----------------
                    //httpCon.setDoOutput(true);
                    //httpCon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    //httpCon.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    /*
                    OutputStreamWriter out = new OutputStreamWriter(
                            conn.getOutputStream());
                    out.write(jsonStr);
                    out.flush();*/


                    Log.i("test","test="+jsonStr);
                }
                catch (IOException e)
                {
                    e.fillInStackTrace();
                }
            }
        }.start();
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