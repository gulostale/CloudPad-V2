package com.jatar_000.cloudpad.notes.util;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utils for Validation etc
 * Created by Jatar on 27.05.16.
 */
public class NotesClientUtil {

      public static boolean isHttp(String url) {
        return url != null && url.length() > 4 && url.startsWith("http") && url.charAt(4) != 's';
    }

    public static boolean isValidLogin(String url, String username, String password) {
        try {
            String targetURL = url + "index.php/apps/notes/api/v0.2/notes";
            HttpURLConnection con = (HttpURLConnection) new URL(targetURL)
                    .openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty(
                    "Authorization",
                    "Basic "
                            + new String(Base64.encode((username + ":"
                            + password).getBytes(), Base64.NO_WRAP)));
            con.setConnectTimeout(10 * 1000); // 10 seconds
            con.connect();
            if (con.getResponseCode() == 200) {
                return true;
            }
        } catch (MalformedURLException e1) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static boolean isValidURL(String url) {
        StringBuffer result = new StringBuffer();
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url + "status.php")
                    .openConnection();
            con.setRequestMethod(NotesClient.METHOD_GET);
            con.setConnectTimeout(10 * 1000); // 10 seconds
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject response = new JSONObject(result.toString());
            return response.getBoolean("installed");
        } catch (IOException | JSONException | NullPointerException e) {
            return false;
        }
    }

}