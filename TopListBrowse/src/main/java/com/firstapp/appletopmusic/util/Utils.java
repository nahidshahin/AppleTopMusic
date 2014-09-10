package com.firstapp.appletopmusic.util;

import com.firstapp.appletopmusic.model.Song;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nahid on 9/8/14.
 */
public class Utils {

    public static List<Song> parseJSONForSongs(String jsonStr) throws Exception {
        List<Song> res = new ArrayList<Song>();

        JSONObject root = new JSONObject(jsonStr);
        JSONObject jObject = root.getJSONObject("feed");

        JSONArray entries = jObject.getJSONArray("entry");

        for (int i=0; i < entries.length(); i++) {
            JSONObject entry = entries.getJSONObject(i);
            JSONObject nameObj = entry.getJSONObject("im:name");
            JSONObject artistObj = entry.getJSONObject("im:artist");
            JSONObject imageObj = entry.getJSONArray("im:image").length() > 0 ? entry.getJSONArray("im:image").getJSONObject(0) : null;
            String name = nameObj.getString("label");
            String artist = artistObj.getString("label");
            String imageURL = (imageObj != null ? imageObj.getString("label") : null);
            res.add(new Song(name, artist, null, imageURL));
        }

        return res;
    }

    public static byte[] fetch(String urlString) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        InputStream is = response.getEntity().getContent();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] tmp = buffer.toByteArray();
        buffer.close();
        is.close();
        return tmp;
    }

}
