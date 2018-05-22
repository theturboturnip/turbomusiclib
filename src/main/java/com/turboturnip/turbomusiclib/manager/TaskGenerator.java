/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

import com.turboturnip.turbomusiclib.common.library_filters.LibraryFilterALL;
import com.turboturnip.turbomusiclib.common.library_filters.LibraryFilter;
import com.turboturnip.turbomusiclib.common.library_filters.LibraryFilterOR;
import com.turboturnip.turbomusiclib.common.Library;
import com.turboturnip.turbomusiclib.common.Song;
import com.turboturnip.turbomusiclib.common.SongSource;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author samuel
 */
public class TaskGenerator {
    private static class FileMetadataPair {
        public File file;
        public SongMetadata metadata;
        
        public FileMetadataPair(File file, SongMetadata metadata){
            assert(file != null);
            assert(metadata != null);
            
            this.file = file;
            this.metadata = metadata;
        }
    }
    
    public static class DownloadTask {
        public final OriginalSongMetadata target;
        public final SongSource source;
        public final File output;
        
        public DownloadTask(OriginalSongMetadata target, SongSource source, File output){
            this.target = target;
            this.source = source;
            this.output = output;
        }
    }
    public static class FilterTask {
        public final File input;
        public final File output;
        public final List<FFmpegFilter> filterChains;
        
        public FilterTask(File input, File output, List<FFmpegFilter> filterChains){
            this.input = input;           
            this.output = output;
            this.filterChains = filterChains;
        }
    }
    public static class FinalTransferTask {
        public final File input;
        public final File output;
        public final OriginalSongMetadata target;
        
        public FinalTransferTask(File input, File output, OriginalSongMetadata target){
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
    public static Tasks generateTasks(Library library, List<FFmpegFilter> filters, SongMetadataGenerator metadataGenerator, PathOptions paths, TaskGenerationOptions options){
        // The songs to be filtered and moved into the output folder
        List<Song> expectedOutputSongs = library.getSongsFromIds(library.getFilteredSongIds(options.songsToFilter));
        expectedOutputSongs.removeIf(s -> !library.getSongSourceForId(s.sourceId.idOfSource).getDoesFilter());
        Set<OriginalSongMetadata> expectedOutputSongMetadata = new HashSet<>();
        expectedOutputSongs.forEach((song) -> {
            expectedOutputSongMetadata.add(new OriginalSongMetadata(song, library));
        });
        
        // Find the songs we currently have in the output folder
        Set<FileMetadataPair> currentOutputSongMetadataPairs = new HashSet<>();
        populateCurrentSongMetadataSet(currentOutputSongMetadataPairs, metadataGenerator, paths.finalOutputDirectory);
        
        // Find the songs we don't have in the output folder
        Set<OriginalSongMetadata> missingSongs = new HashSet<>();
        missingSongs.addAll(expectedOutputSongMetadata);
        missingSongs.removeIf(songMetadata -> currentOutputSongMetadataPairs.stream().anyMatch(otherMetadataPair -> otherMetadataPair.metadata.equals(songMetadata)));
        
        // Define the set of songs we need to filter
        Set<OriginalSongMetadata> toFilter;
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
            
            toFilter.forEach((OriginalSongMetadata songMetadata) -> {
                SongSource source = library.getSongSourceForId(songMetadata.baseSong.sourceId.idOfSource);
                if (!source.getGeneratesFiles()) return;
                
                File lastKnownLocation;
                
                File downloadFile = new File(new File(paths.downloadsDirectory, source.getId()), songMetadata.baseSong.sourceId.idWithinSource);
                boolean downloadOutOfDate = options.forceRedownload || !downloadFile.exists();
                
                if (source.getDoesDownload()){
                    if (downloadOutOfDate){
                        tasks.downloadTasks.add(new DownloadTask(songMetadata, source, downloadFile));
                    }
                    
                    lastKnownLocation = downloadFile;
                }else{
                    lastKnownLocation = source.getFilterInputFile(songMetadata.baseSong);
                }
                
                File filterOutput = new File(new File(paths.filterTempDirectory, source.getId()), songMetadata.baseSong.sourceId.idWithinSource + ".mp3");
                boolean filterOutOfDate = options.forceRefilter || downloadOutOfDate || filterOutput.exists();
                
                if (source.getDoesFilter()){
                    if (filterOutOfDate)
                        tasks.filterTasks.add(new FilterTask(lastKnownLocation, filterOutput, filters));
                    lastKnownLocation = filterOutput;
                }
                
                File targetOutputLocation = new File(new File(paths.finalOutputDirectory, source.getId()), songMetadata.baseSong.name + ".mp3");
                boolean finalTransferOutOfDate = filterOutOfDate || !targetOutputLocation.exists();
                if (finalTransferOutOfDate)
                    tasks.finalTransferTasks.add(new FinalTransferTask(lastKnownLocation, targetOutputLocation, songMetadata));
            });
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
    private static void populateCurrentSongMetadataSet(Set<FileMetadataPair> toPopulate, SongMetadataGenerator metadataGenerator, File searchFolder){
        if (searchFolder.listFiles() == null) return;
        for (File file : searchFolder.listFiles()){
            if (file.isDirectory()) populateCurrentSongMetadataSet(toPopulate, metadataGenerator, file);
            else toPopulate.add(new FileMetadataPair(file, metadataGenerator.FromFile(file)));
        }
    }
}
