package com.firstapp.appletopmusic.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.firstapp.appletopmusic.adapters.SongListAdapter;
import com.firstapp.appletopmusic.model.Song;
import com.firstapp.appletopmusic.toplistbrowse.TopListBrowseActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by nahid on 9/9/14.
 */
public class DataCollector extends AsyncTask<String, Void, String> {
    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    private final HttpClient httpclient = new DefaultHttpClient();
    final HttpParams params = httpclient.getParams();
    HttpResponse response;
    private String content = null;
    private boolean error = false;
    private ProgressDialog dialog;
    List<Song> songs;
    ListView songListView;
    TopListBrowseActivity tlist;
    SongListAdapter adapter;

    public DataCollector(TopListBrowseActivity tlist, List<Song> songs, ListView songListView, SongListAdapter adapter) {
        this.tlist = tlist;
        this.songs = songs;
        this.songListView = songListView;
        this.adapter = adapter;
        dialog = new ProgressDialog(tlist);
    }

    protected void onPreExecute() {
        dialog.setMessage("Loading your data... Please wait...");
        dialog.show();
    }

    protected String doInBackground(String... urls) {

        String URL = null;

        try {

            URL = urls[0];
            HttpGet request = new HttpGet(URL);


            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpResponse response = httpclient.execute(request);
            //InputStream is= response.getEntity().getContent();


            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                content = out.toString();
                songs.clear();
                for (Song song : Utils.parseJSONForSongs(content)) {
                    song.setCoverImage(Utils.fetch(song.getUrl()));
                    songs.add(song);
                }
                //adapter.notifyDataSetChanged();
                songListView.refreshDrawableState();
                songListView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            } else {
                //Closes the connection.
                Log.w("HTTP1:", statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.w("HTTP2:", e);
            content = e.getMessage();
            error = true;
            cancel(true);
        } catch (IOException e) {
            Log.w("HTTP3:", e);
            content = e.getMessage();
            error = true;
            cancel(true);
        } catch (Exception e) {
            Log.w("HTTP4:", e);
            content = e.getMessage();
            error = true;
            cancel(true);
        }

        return content;
    }

    protected void onPostExecute(String content) {
        dialog.dismiss();
        if (error) {
            Toast toast = Toast.makeText(tlist, "Error on URL fetch", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 75);
            toast.show();
        }
    }


}
