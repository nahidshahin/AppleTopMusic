package com.firstapp.appletopmusic.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstapp.appletopmusic.model.Song;
import com.firstapp.appletopmusic.toplistbrowse.R;
import com.firstapp.appletopmusic.toplistbrowse.TopListBrowseActivity;

import java.util.List;

/**
 * Created by nahid on 9/9/14.
 */
public class SongListAdapter extends ArrayAdapter<Song> {
    TopListBrowseActivity tlist;
    List<Song> songs;
    byte[] noPrevImg;

    public SongListAdapter(TopListBrowseActivity tlist, List<Song> songs, byte[] noPrevImg) {
        super(tlist, R.layout.songlistliew_item, songs);
        this.tlist = tlist;
        this.songs = songs;
        this.noPrevImg = noPrevImg;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = tlist.getLayoutInflater().inflate(R.layout.songlistliew_item, parent, false);

        Song song = songs.get(position);

        TextView name = (TextView) view.findViewById(R.id.songName);
        name.setText(song.getName());
        TextView url = (TextView) view.findViewById(R.id.sUrl);
        url.setText(song.getArtist());
        ImageView ivContactImage = (ImageView) view.findViewById(R.id.idSongCoverImage);
        byte[] coverImg = song.getCoverImage() == null || song.getCoverImage().length <= 0 ? noPrevImg : song.getCoverImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(coverImg, 0, coverImg.length);
        ivContactImage.setImageBitmap(bmp);
        return view;
    }


}
