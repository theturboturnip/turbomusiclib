/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common;

import com.turboturnip.turbomusiclib.common.filters.LibraryFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author samuel
 */
public abstract class Library {
    /*
 
    
    
    public abstract int getNextArtistId();
    public abstract void addArtist(Artist toAdd); // Dumb add, doesn't do any checks
    
    public abstract int getNextAlbumId();
    public abstract void addAlbum(Album toAdd); // Dumb add, doesn't do any checks
    
    public abstract int getNextSongId();
    public abstract void addSong(Song toAdd); // Dumb add, doesn't do any checks*/
    
    public abstract SongCollectionSource getSource(int index);
    public abstract void addSource(SongCollectionSource source);
    public abstract void removeSource(int index);
    public abstract void updateSource(int index, SongCollectionSource newSource);
    public void updateKnownSongs(){
        updateKnownSongs(false);
    }
    public abstract void updateKnownSongs(boolean forceUpdateAll);
    public abstract int getSourceCount();
    
    public abstract List<SongId> getAllSongIds();
    public abstract List<SongId> getFilteredSongIds(LibraryFilter baseFilter);
    public abstract List<Integer> getAllAlbumIds();
    public abstract List<Integer> getAllArtistIds();

    public abstract int getUndupedArtistId(Artist artist);
    public Artist undupeArtist(Artist artist){
        int newId = getUndupedArtistId(artist);
        if (newId < 0) return artist;
        return new Artist(artist, newId);
    }
    public abstract int getUndupedAlbumId(Album album);
    public Album undupeAlbum(Album album){
        int newId = getUndupedAlbumId(album);
        if (newId < 0) return album;
        return new Album(album, newId);
    }
    
    public List<SongId> getSongIdsInAlbum(Album album){
        return getSongIdsInAlbum(album.id);
    }
    public abstract List<SongId> getSongIdsInAlbum(int albumId);
    
    public List<SongId> getSongIdsByArtist(Artist artist){
        return getSongIdsByArtist(artist.id);
    }
    public abstract List<SongId> getSongIdsByArtist(int artistId);
    
    public List<Integer> getAlbumIdsByArtist(Artist artist){
        return getAlbumIdsByArtist(artist.id);
    }
    public abstract List<Integer> getAlbumIdsByArtist(int artistId);
    
    public List<Song> getSongsFromIds(List<SongId> ids){
        List<Song> songs = new ArrayList<>(ids.size());
        for (SongId id : ids){
            songs.add(getSongFromId(id));
        }
        return songs;
    }
    public abstract Song getSongFromId(SongId id);
    public List<Album> getAlbumsFromIds(List<Integer> ids){
        List<Album> albums = new ArrayList<>(ids.size());
        for (Integer id : ids){
            albums.add(getAlbumFromId(id));
        }
        return albums;
    }
    public abstract Album getAlbumFromId(int id);

    public List<Artist> getArtistsFromIds(List<Integer> ids){
        List<Artist> artists = new ArrayList<>(ids.size());
        for (Integer id : ids){
            artists.add(getArtistFromId(id));
        }
        return artists;
    }
    public abstract Artist getArtistFromId(int id);
}
