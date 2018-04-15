/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common;

import java.io.File;
import java.net.URL;

/**
 *
 * @author samuel
 */
public class Song { 
    public static class SourceId{
        public final String idOfSource;
        public final String idWithinSource;
        public final String verboseIdWithinSource;
        
        public SourceId(String idOfSource, String idWithinSource, String verboseIdWithinSource){
            this.idOfSource = idOfSource;
            this.idWithinSource = idWithinSource;
            this.verboseIdWithinSource = verboseIdWithinSource;
        }
    }
    
    public final SongId id;
    public final SourceId sourceId;
    public final String name;
    public final int artistId;
    public final int albumId;
    public final int albumIndex;
    
    public Song(Song copyFrom, SongId newId){
        this(newId, copyFrom.sourceId, copyFrom.name, copyFrom.artistId, copyFrom.albumId, copyFrom.albumIndex);
    }
    public Song(Song copyFrom, SongId newId, int newAlbumId){
        this(newId, copyFrom.sourceId, copyFrom.name, copyFrom.artistId, newAlbumId, copyFrom.albumIndex);
    }
    public Song(Song copyFrom, SongId newId, int newArtistId, int newAlbumId){
        this(newId, copyFrom.sourceId, copyFrom.name, newArtistId, newAlbumId, copyFrom.albumIndex);
    }
    public Song(SongId id, SourceId sourceId, String name, Album album, int albumIndex){
        this(id, sourceId, name, album.artistId, album.id, albumIndex);
    }
    public Song(SongId id, SourceId sourceId, String name, Artist artist){
        this(id, sourceId, name, artist.id, -1, -1);
    }
    public Song(SongId id, SourceId sourceId, String name, int artistId, int albumId, int albumIndex){
        assert(id.songIndex >= 0);
        assert(sourceId != null);
        assert(name != null);
        assert(artistId >= 0);
        
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.artistId = artistId;
        this.albumId = albumId;
        this.albumIndex = albumIndex;
    }
    
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Song)) return false;
        return id.equals(((Song)o).id);
    }
    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
