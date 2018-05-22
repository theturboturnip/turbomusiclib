/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

import java.util.Objects;

/**
 *
 * @author samuel
 */
public class SongMetadata {
    public static final SongMetadata Empty = new SongMetadata("","","");
    
    public String name;
    public String artist;
    public String album;

    public SongMetadata(String name, String artist, String album){
        if (name == null)
            this.name = "";
        else
            this.name = name;
        if (artist == null)
            this.artist = "";
        else
            this.artist = artist;
        if (album == null)
            this.album = "";
        else
            this.album = album;
    }
    
    @Override
    public int hashCode(){
        return 17 * (7 * name.hashCode() ^ 13 * artist.hashCode()) ^ 23 * album.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SongMetadata)) {
            return false;
        }
        final SongMetadata other = (SongMetadata) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.artist, other.artist)) {
            return false;
        }
        if (!Objects.equals(this.album, other.album)) {
            return false;
        }
        return true;
    }
}