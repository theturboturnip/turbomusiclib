/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common;

import com.turboturnip.turbomusiclib.common.library_filters.LibraryFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author samuel
 */
public abstract class Library {

    public abstract void addSongSource(SongSource instance);
    public abstract SongSource getSongSourceForId(String id);
    public abstract void clearSongSourceForId(String idToClear);
    
    public abstract SongCollectionSource getCollectionSource(int index);
    public abstract void addCollectionSource(SongCollectionSource source);
    public abstract void removeCollectionSource(int index);
    public abstract void updateCollectionSource(int index, SongCollectionSource newSource);
    public void updateKnownSongs(){
        updateKnownSongs(false);
    }
    public abstract void updateKnownSongs(boolean forceUpdateAll);
    public abstract int getCollectionSourceCount();
    
    public abstract Set<SongId> getAllSongIds();
    public abstract Set<Integer> getAllAlbumIds();
    public abstract Set<Integer> getAllArtistIds();

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
    
    public Set<SongId> getSongIdsInAlbum(Album album){
        return getSongIdsInAlbum(album.id);
    }
    public abstract Set<SongId> getSongIdsInAlbum(int albumId);
    
    public Set<SongId> getSongIdsByArtist(Artist artist){
        return getSongIdsByArtist(artist.id);
    }
    public abstract Set<SongId> getSongIdsByArtist(int artistId);
    
    public Set<Integer> getAlbumIdsByArtist(Artist artist){
        return getAlbumIdsByArtist(artist.id);
    }
    public abstract Set<Integer> getAlbumIdsByArtist(int artistId);
    
    public Set<Song> getSongsFromIds(Collection<SongId> ids){
        Set<Song> songs = new HashSet<>();
        for (SongId id : ids){
            songs.add(getSongFromId(id));
        }
        return songs;
    }
    public abstract Song getSongFromId(SongId id);
    public Set<Album> getAlbumsFromIds(Collection<Integer> ids){
        Set<Album> albums = new HashSet<>();
        for (Integer id : ids){
            albums.add(getAlbumFromId(id));
        }
        return albums;
    }
    public abstract Album getAlbumFromId(int id);

    public Set<Artist> getArtistsFromIds(Collection<Integer> ids){
        Set<Artist> artists = new HashSet<>();
        for (Integer id : ids){
            artists.add(getArtistFromId(id));
        }
        return artists;
    }
    public abstract Artist getArtistFromId(int id);
}
