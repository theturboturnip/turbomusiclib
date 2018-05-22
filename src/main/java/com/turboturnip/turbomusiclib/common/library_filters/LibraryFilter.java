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
public abstract class LibraryFilter {
    public abstract boolean fitsSong(Song testAgainst);
    public abstract Set<SongId> songsThatFit(Library library);
}
