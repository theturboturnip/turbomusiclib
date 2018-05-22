/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

import com.turboturnip.turbomusiclib.common.Library;
import com.turboturnip.turbomusiclib.common.Song;

/**
 *
 * @author samuel
 */
public class OriginalSongMetadata extends SongMetadata{
    public Song baseSong;

    public OriginalSongMetadata(Song baseSong, Library library){
        super(baseSong.name, library.getArtistFromId(baseSong.artistId).name, baseSong.albumId < 0 ? "" : library.getAlbumFromId(baseSong.albumId).name);
        this.baseSong = baseSong;
    }
}