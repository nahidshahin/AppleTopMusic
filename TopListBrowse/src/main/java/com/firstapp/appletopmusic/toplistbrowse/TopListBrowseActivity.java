package com.firstapp.appletopmusic.toplistbrowse;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.firstapp.appletopmusic.adapters.FavSongListAdapter;
import com.firstapp.appletopmusic.adapters.SongListAdapter;
import com.firstapp.appletopmusic.dao.SongDao;
import com.firstapp.appletopmusic.model.Song;
import com.firstapp.appletopmusic.util.DataCollector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class TopListBrowseActivity extends ActionBarActivity {
    static final String TAG = "TopListBrowseActivity";
    static final int songsLimit = 20;

    List<Song> songs = new ArrayList<Song>();
    List<Song> favSongs = new ArrayList<Song>();
    ListView songListView;
    ListView favSongListView;

    SQLiteDatabase db;
    SongDao sdao;
    byte[] noPrevImg = null;

    SongListAdapter adapter = null;
    FavSongListAdapter favsadapter = null;

    private ProgressDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SongListAdapter(TopListBrowseActivity.this, songs, noPrevImg);
        favsadapter = new FavSongListAdapter(TopListBrowseActivity.this, favSongs, noPrevImg);
        sdao = new SongDao(this);

        InputStream imgIns = getResources().openRawResource(
                getResources().getIdentifier("nopreview", "raw", getPackageName())
        );
        try {
            ByteArrayOutputStream nopreviewImgBuffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = imgIns.read(data, 0, data.length)) != -1) {
                nopreviewImgBuffer.write(data, 0, nRead);
            }
            nopreviewImgBuffer.flush();
            noPrevImg = nopreviewImgBuffer.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadFavList();
        setContentView(R.layout.activity_top_list_browse);

        songListView = (ListView) findViewById(R.id.listView);
        favSongListView = (ListView) findViewById(R.id.favListView);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("topList");
        tabSpec.setContent(R.id.listView);
        tabSpec.setIndicator("Top Chart");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("favList");
        tabSpec.setContent(R.id.favListView);
        tabSpec.setIndicator("Favorite Songs");
        tabHost.addTab(tabSpec);


        songListView.setAdapter(adapter);
        favSongListView.setAdapter(favsadapter);

        songListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        Song song = (Song) parent.getAdapter().getItem(position);
                        System.out.println("song name [" + song.getName());
                        boolean exists = false;
                        for (Song s : favSongs) {
                            if (s.getName().equals(song.getName())) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            try {
                                db = sdao.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.clear();
                                values.put(SongDao.C_NAME, song.getName());
                                values.put(SongDao.C_ARTIST, song.getArtist());
                                values.put(SongDao.C_COVERIMAGE, song.getCoverImage());
                                db.insertOrThrow(SongDao.TABLE, null, values);
                                Log.d(TAG, String.format("%s: %s added", song.getName(), song.getArtist()));
                                favSongs.add(song);
                                db.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            favsadapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Added to your Favourite List!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        favSongListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        Song song = (Song) parent.getAdapter().getItem(position);
                        boolean exists = false;
                        for (Song s : favSongs) {
                            if (s.getName().equals(song.getName())) {
                                exists = true;
                                break;
                            }
                        }
                        if (exists) {
                            try {
                                db = sdao.getWritableDatabase();
                                String delSql = "DELETE FROM " + SongDao.TABLE + " WHERE " + SongDao.C_NAME + " = ?  ";
                                SQLiteStatement delStmt = db.compileStatement(delSql);
                                delStmt.clearBindings();
                                delStmt.bindString(1, song.getName());
                                int numOfSong = delStmt.executeUpdateDelete();
                                Log.d(TAG, String.format("%d: songs having name %s deleted", numOfSong, song.getName()));
                                favSongs.remove(song);
                                db.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            favsadapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), song.getName() + " by " + song.getArtist() + " has been deleted from your Favourite List!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), song.getName() + " by " + song.getArtist() + " doesn't exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        (new DataCollector(TopListBrowseActivity.this, songs, songListView, adapter)).execute("https://itunes.apple.com/us/rss/topaudiobooks/limit=" + songsLimit + "/json");
    }

    private void loadFavList() {
        try {
            favSongs.clear();
            db = sdao.getWritableDatabase();
            String sql = "SELECT * FROM " + SongDao.TABLE;
            Cursor cursor = db.rawQuery(sql, new String[]{});
            while (cursor.moveToNext()) {
                favSongs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getBlob(2)));
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_list_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
