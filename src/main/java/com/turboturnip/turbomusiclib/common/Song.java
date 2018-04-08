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
//enum AccessType {
        //    LocalFile,
        //    DownloadedFile,
        //    Stream
        //}
        /*public AccessType getAccessType();
        public String getNetworkAddress(); // The location of the file on the network (can be URL) or the identifier for the song on a streaming service.
        public String getDownloadsSubfolder(); // The subfolder of the downloads folder which the files will be downloaded to.
        public String getDownloadFilename(); // The name of the downloaded file. Should be consistent between runs, so that existing downloads can be reused.
        public String getSourceFile(); // The local source file. Used if the file isn't downloaded.
        public String getOutputFile();*/
//public String getDownloadSubfolder();
        //public String getDownloadFilename();
        //public String getLocalFilename(); // Either the location of the local file, or where the downloaded file will end up
    }
    public final int id;
    public final Source source;
    public final String name;
    public final int artistId;
    public final int albumId;
    
    public Song(Song copyFrom, int newId){
        this(newId, copyFrom.source, copyFrom.name, copyFrom.artistId, copyFrom.albumId);
    }
    public Song(Song copyFrom, int newId, int newAlbumId){
        this(newId, copyFrom.source, copyFrom.name, copyFrom.artistId, newAlbumId);
    }
    public Song(int id, Source source, String name, Album album){
        this(id, source, name, album.artistId, album.id);
    }
    public Song(int id, Source source, String name, Artist artist){
        this(id, source, name, artist.id, -1);
    }
    public Song(int id, Source source, String name, int artistId, int albumId){
        assert(id >= 0);
        assert(source != null);
        assert(name != null);
        assert(artistId >= 0);
        
        this.id = id;
        this.source = source;
        this.name = name;
        this.artistId = artistId;
        this.albumId = albumId;
    }
    
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Song)) return false;
        return id == ((Song)o).id;
    }
    @Override
    public int hashCode(){
        return id;
    }
}
