package com.example.imius.model;

public class LibraryPlaylist {

    private int idLibraryPlaylist;
    private String nameLibraryPlaylist;
    private String imageLibraryPlaylist;
    private String username;

    public LibraryPlaylist(int idLibraryPlaylist, String nameLibraryPlaylist, String imageLibraryPlaylist, String username) {
        this.idLibraryPlaylist = idLibraryPlaylist;
        this.nameLibraryPlaylist = nameLibraryPlaylist;
        this.imageLibraryPlaylist = imageLibraryPlaylist;
        this.username = username;
    }

    public LibraryPlaylist(int idLibraryPlaylist, String nameLibraryPlaylist, String imageLibraryPlaylist) {
        this.idLibraryPlaylist = idLibraryPlaylist;
        this.nameLibraryPlaylist = nameLibraryPlaylist;
        this.imageLibraryPlaylist = imageLibraryPlaylist;
    }

    public int getIdLibraryPlaylist() {
        return idLibraryPlaylist;
    }

    public void setIdLibraryPlaylist(int idLibraryPlaylist) {
        this.idLibraryPlaylist = idLibraryPlaylist;
    }

    public String getNameLibraryPlaylist() {
        return nameLibraryPlaylist;
    }

    public void setNameLibraryPlaylist(String nameLibraryPlaylist) {
        this.nameLibraryPlaylist = nameLibraryPlaylist;
    }

    public String getImageLibraryPlaylist() {
        return imageLibraryPlaylist;
    }

    public void setImageLibraryPlaylist(String imageLibraryPlaylist) {
        this.imageLibraryPlaylist = imageLibraryPlaylist;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
