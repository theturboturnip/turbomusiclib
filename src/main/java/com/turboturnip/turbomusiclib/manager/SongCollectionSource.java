/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

import com.turboturnip.turbomusiclib.common.*;
import java.util.List;

/**
 *
 * @author samuel
 */
public abstract class SongCollectionSource {
    public void addToLibrary(Library library){
        List<Artist> artists = getAllArtists();
        List<Album> albums = getAllAlbums();
        List<Song> songs = getAllSongs();
        
        for (Artist artist : artists){
            int initialArtistId = artist.id;
            
            int newArtistId = library.getUndupedArtistId(artist);
            if (newArtistId < 0){
                newArtistId = library.getNextArtistId();
                library.addArtist(new Artist(artist, newArtistId));
            }
            
            for (Album album : albums){
                if (album.artistId != initialArtistId) continue;
                int initialAlbumId = album.id;
                
                int newAlbumId = library.getUndupedAlbumId(album);
                if (newAlbumId < 0){
                    newAlbumId = library.getNextAlbumId();
                    library.addAlbum(new Album(album, newAlbumId, newArtistId));
                }
                
                for (Song song : songs){
                    if (song.albumId != initialAlbumId) continue;
                    
                    int newSongId = library.getNextSongId(); 
                    library.addSong(new Song(song, newSongId, newAlbumId));
                }
                
            }
        }
    }
    protected abstract List<Artist> getAllArtists();
    protected abstract List<Album> getAllAlbums();
    protected abstract List<Song> getAllSongs();
}

