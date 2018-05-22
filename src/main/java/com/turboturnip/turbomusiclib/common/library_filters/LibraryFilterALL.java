/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common.library_filters;

import com.turboturnip.turbomusiclib.common.Library;
import com.turboturnip.turbomusiclib.common.Song;
import com.turboturnip.turbomusiclib.common.SongId;
import java.util.Set;

/**
 *
 * @author samuel
 */
public class LibraryFilterALL extends LibraryFilter {
    @Override
    public boolean fitsSong(Song testAgainst){
        return true;
    }
    @Override
    public Set<SongId> songsThatFit(Library library){
        return library.getAllSongIds();
    }
}
