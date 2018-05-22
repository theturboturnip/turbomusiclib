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
public class LibraryFilterOR extends LibraryFilter {
    // Should evaluate to (a OR b OR c...)
    LibraryFilter[] subfilters;
    
    public LibraryFilterOR(LibraryFilter... subfilters){
        assert(subfilters.length > 0);
        this.subfilters = subfilters;
    }
    
    @Override
    public boolean fitsSong(Song testAgainst){
        for (LibraryFilter subfilter : subfilters){
            if (subfilter.fitsSong(testAgainst)) return true;
        }
        return false;
    }
    @Override
    public Set<SongId> songsThatFit(Library library){
        Set<SongId> songSet = subfilters[0].songsThatFit(library);
        for (int i = 1; i < subfilters.length; ++i){
            songSet.addAll(subfilters[i].songsThatFit(library));
        }
        return songSet;
    }
}
