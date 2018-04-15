/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.turboturnip.turbomusiclib.common.Library;
import com.turboturnip.turbomusiclib.common.Song;
import com.turboturnip.turbomusiclib.common.SongSource;
import com.turboturnip.turbomusiclib.common.filters.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author samuel
 */
public class TaskGenerator {
    public static class SongMetadata {
        public Song source; // OPTIONAL
        public String name;
        public String artist;
        public String album;
        
        public SongMetadata(Song source, Library library){
            this.source = source;
            name = source.name;
            artist = library.getArtistFromId(source.artistId).name;
            if (source.albumId < 0) album = "";
            else album = library.getAlbumFromId(source.albumId).name;
        }
        public SongMetadata(String name, String artist, String album){
            this.source = null;
            this.name = name;
            this.artist = artist;
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
            if (getClass() != obj.getClass()) {
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
    public static class DownloadTask {
        public final SongMetadata target;
        
        public DownloadTask(SongMetadata target){
            this.target = target;
        }
    }
    public static class FilterTask {
        public final File input;
        public final File output;
        public final String filter;
        
        public FilterTask(){
            input = null;
            output = null;
            filter = null;
        }
    }
    public static class FinalTransferTask {
        public final File input;
        public final File output;
        public final SongMetadata target;
        
        public FinalTransferTask(File input, File output, SongMetadata target){
            this.input = input;
            this.output = output;
            this.target = target;
        }
    }
    public static class DeletionTask {
        public final File toDelete;
        
        public DeletionTask(File toDelete){
            this.toDelete = toDelete;
        }
    }
    
    public static class Tasks{
        public Set<DownloadTask> downloadTasks;
        public Set<FilterTask> filterTasks;
        public Set<FinalTransferTask> finalTransferTasks;
        public Set<DeletionTask> deleteTasks;
    }
    public static class PathOptions {
        public File downloadsDirectory;
        public File filterTempDirectory;
        public File finalOutputDirectory;
    }
    public static class TaskGenerationOptions {
        public LibraryFilter songsToDownload;
        public boolean forceRedownload;
        public LibraryFilter songsToFilter;
        public boolean forceRefilter;
        
        public TaskGenerationOptions(LibraryFilter songsToDownload, boolean forceRedownload, LibraryFilter songsToFilter, boolean forceRefilter){
            this.songsToDownload = songsToDownload;
            this.forceRedownload = forceRedownload;
            this.songsToFilter = new LibraryFilterOR(songsToDownload, songsToFilter);
            this.forceRefilter = forceRefilter;
        }
        public TaskGenerationOptions(){
            this(new LibraryFilterALL(), false, new LibraryFilterALL(), false);
        }
    }
    public static Tasks generateTasks(Library library, PathOptions paths, TaskGenerationOptions options){
        // The songs to be filtered and moved into the output folder
        List<Song> expectedOutputSongs = library.getSongsFromIds(library.getFilteredSongIds(options.songsToFilter));
        expectedOutputSongs.removeIf(s -> !library.getSongSourceForId(s.sourceId.idOfSource).getDoesFilter());
        Set<SongMetadata> expectedOutputSongMetadata = new HashSet<>();
        expectedOutputSongs.forEach((song) -> {
            expectedOutputSongMetadata.add(new SongMetadata(song, library));
        });
        
        // Find the songs we currently have in the output folder
        Set<FileMetadataPair> currentOutputSongMetadataPairs = new HashSet<>();
        populateCurrentSongMetadataSet(currentOutputSongMetadataPairs, paths.finalOutputDirectory);
        
        // Find the songs we don't have in the output folder
        Set<SongMetadata> missingSongs = new HashSet<>();
        missingSongs.addAll(expectedOutputSongMetadata);
        missingSongs.removeIf(songMetadata -> currentOutputSongMetadataPairs.stream().anyMatch(otherMetadataPair -> otherMetadataPair.metadata.equals(songMetadata)));
        
        // Define the set of songs we need to filter
        Set<SongMetadata> toFilter;
        if (options.forceRefilter)
            toFilter = expectedOutputSongMetadata;
        else
            toFilter = missingSongs;
        
        // Define the tasks we need to perform
        Tasks tasks = new Tasks();
        {
            tasks.downloadTasks = new HashSet<>();
            tasks.filterTasks = new HashSet<>();
            tasks.finalTransferTasks = new HashSet<>();
            
            toFilter.forEach((songMetadata) -> {
                SongSource source = library.getSongSourceForId(songMetadata.source.sourceId.idOfSource);
                if (!source.getGeneratesFiles()) return;
                
                File lastKnownLocation;
                if (source.getDoesDownload()){
                    File outputFile = new File(new File(paths.downloadsDirectory, source.getId()), songMetadata.source.sourceId.idWithinSource);
                    if (options.forceRedownload || !outputFile.exists())
                        tasks.downloadTasks.add(new DownloadTask(songMetadata));
                    
                    lastKnownLocation = outputFile;
                }else{
                    lastKnownLocation = source.getFilterInputFile(songMetadata.source);
                }
                
                if (source.getDoesFilter()){
                    // TODO: Filter!
                }
                
                File targetOutputLocation = new File(new File(paths.finalOutputDirectory, source.getId()), songMetadata.source.name + ".mp3");
                tasks.finalTransferTasks.add(new FinalTransferTask(lastKnownLocation, targetOutputLocation, songMetadata));
            });
            
            /*Set<SongMetadata> toDownload = new HashSet<>(toFilter);
            toDownload.removeIf(songMetadata -> !songMetadata.source.source.isDownloaded());
            if (!options.forceRedownload){
                toDownload.removeIf(songMetadata -> songMetadata.source.source.resultantInputFile().exists());
            }
            
            // Add the relevant tasks
            toDownload.forEach((songMetadata) -> );*/
        }
        {
            // The set of songs we need to filter is already known, just populate the tasks.
            
            /*toFilter.forEach((SongMetadata songMetadata) -> {
                File lastKnownLocation = songMetadata.source.source.resultantInputFile();
                File intermediateTempFile = new File(paths.filterTempDirectory, songMetadata.source.id + ".mp3");
                // TODO: Filter!
                // for (Filter in filters) { stuff; lastKnownLocation = intermediateTempFile; }
                tasks.finalTransferTasks.add(new FinalTransferTask(lastKnownLocation, songMetadata.source.source.getFinalOutputFile(paths.finalOutputDirectory), songMetadata));
            });*/
        }
        {
            // Define the set of songs we need to delete
            Set<FileMetadataPair> extraSongFiles = new HashSet<>();
            extraSongFiles.addAll(currentOutputSongMetadataPairs);
            extraSongFiles.removeIf(filePair -> expectedOutputSongMetadata.contains(filePair.metadata));
            
            // Add the relevant tasks
            tasks.deleteTasks = new HashSet<>();
            extraSongFiles.forEach((filePair) -> tasks.deleteTasks.add(new DeletionTask(filePair.file)));
        }
        
        return tasks;
    }
    private static void populateCurrentSongMetadataSet(Set<FileMetadataPair> toPopulate, File searchFolder){
        if (searchFolder.listFiles() == null) return;
        for (File file : searchFolder.listFiles()){
            if (file.isDirectory()) populateCurrentSongMetadataSet(toPopulate, file);
            else if (file.getName().endsWith(".mp3")) toPopulate.add(new FileMetadataPair(file, getSongMetadata(file)));
        }
    }
    private static class FileMetadataPair {
        public File file;
        public SongMetadata metadata;
        
        public FileMetadataPair(File file, SongMetadata metadata){
            this.file = file;
            this.metadata = metadata;
        }
    }
    private static SongMetadata getSongMetadata(File sourceMP3){
        try{
            Mp3File mp3File = new Mp3File(sourceMP3);
            if (mp3File.hasId3v1Tag()){
                ID3v1 tag = mp3File.getId3v1Tag();
                return new SongMetadata(tag.getTitle(), tag.getArtist(), tag.getAlbum());
            }else if (mp3File.hasId3v2Tag()){
                ID3v2 tag = mp3File.getId3v2Tag();
                return new SongMetadata(tag.getTitle(), tag.getArtist(), tag.getAlbum());   
            }
        }catch(IOException | UnsupportedTagException | InvalidDataException e){
            e.printStackTrace();
        }
        
        return new SongMetadata(sourceMP3.getName().split("\\.(?:[a-zA-Z0-9]+$)")[0], "", "");
    }
}
