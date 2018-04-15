/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common;

import java.io.File;
import java.net.URL;
import javax.activation.MimeType;

/**
 *
 * @author samuel
 */
public abstract class SongSource {
    public abstract String getId();
    public abstract boolean getGeneratesFiles();
   
    public boolean getDoesDownload(){
        return false;
    }
    public static class DownloadData {
        public URL url;
        public MimeType filetype;
        
        public DownloadData(){}
        public DownloadData(URL url, MimeType filetype){
            this.url = url;
            this.filetype = filetype;
        }
    }
    public DownloadData getDownloadData(Song song){
        return null;
    }
    // Only called if the song source *doesn't* download.
    public File getFilterInputFile(Song song){
        return null;
    }
    
    public boolean getDoesFilter(){
        return true;
    }
}
