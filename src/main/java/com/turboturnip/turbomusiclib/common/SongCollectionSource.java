/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common;

import java.util.List;

/**
 *
 * @author samuel
 */
public abstract class SongCollectionSource {
    public boolean isDirty = false;
    
    public abstract List<Artist> getAllArtists();
    public abstract List<Album> getAllAlbums();
    public abstract List<Song> getAllSongs();
}
