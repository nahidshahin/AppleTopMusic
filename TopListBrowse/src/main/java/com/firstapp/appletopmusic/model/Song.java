package com.firstapp.appletopmusic.model;

/**
 * Created by nahid on 9/5/14.
 */
public class Song implements Comparable<Song> {
    private String name;
    private String artist;
    private String url;
    private byte[] coverImage;

    public Song(String name) {
        this.name = name;
    }

    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public Song(String name, String artist, byte[] coverImage) {
        this.name = name;
        this.artist = artist;
        this.coverImage = coverImage;
    }

    public Song(String name, String artist, byte[] coverImage, String url) {
        this.name = name;
        this.artist = artist;
        this.coverImage = coverImage;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    @Override
    public int compareTo(Song other){
        return this.name != null ? this.name.compareTo(other.getName()) : 0;
    }
}
