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
    public static abstract class Source {
        public abstract boolean isStream();
        public String streamingService(){
            return null;
        }
        public String streamingId(){
            return null;
        }
        
        public abstract boolean isDownloaded();
        public URL downloadSource(){
            return null;
        }
        public String downloadSubFolder(){
            return null;
        }
        public String downloadFilename(){
            return null;
        }
        
        public abstract File resultantInputFile();
        
        public abstract boolean isFiltered();
        public String filterOutputSubFolder(){
            return null;
        }
        public String filterOutputFilename(){
            return null;
        }
        
        public abstract boolean isFile();
        public File getFinalOutputFile(File filterOutputRootFolder){
            if (!isFile()) return null;
            if (isFiltered()) {
                return new File(new File(filterOutputRootFolder, filterOutputSubFolder()), filterOutputFilename());
            }else{
                return resultantInputFile();
            }
        }
    }
    public final SongId id;
    public final Source source;
    public final String name;
    public final int artistId;
    public final int albumId;
    public final int albumIndex;
    
    public Song(Song copyFrom, SongId newId){
        this(newId, copyFrom.source, copyFrom.name, copyFrom.artistId, copyFrom.albumId, copyFrom.albumIndex);
    }
    public Song(Song copyFrom, SongId newId, int newAlbumId){
        this(newId, copyFrom.source, copyFrom.name, copyFrom.artistId, newAlbumId, copyFrom.albumIndex);
    }
    public Song(Song copyFrom, SongId newId, int newArtistId, int newAlbumId){
        this(newId, copyFrom.source, copyFrom.name, newArtistId, newAlbumId, copyFrom.albumIndex);
    }
    public Song(SongId id, Source source, String name, Album album, int albumIndex){
        this(id, source, name, album.artistId, album.id, albumIndex);
    }
    public Song(SongId id, Source source, String name, Artist artist){
        this(id, source, name, artist.id, -1, -1);
    }
    public Song(SongId id, Source source, String name, int artistId, int albumId, int albumIndex){
        assert(id.songIndex >= 0);
        assert(source != null);
        assert(name != null);
        assert(artistId >= 0);
        
        this.id = id;
        this.source = source;
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
