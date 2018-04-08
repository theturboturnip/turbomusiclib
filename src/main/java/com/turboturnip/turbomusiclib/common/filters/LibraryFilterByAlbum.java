/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common.filters;

/**
 *
 * @author samuel
 */
public class LibraryFilterByAlbum extends LibraryFilter{
    public int albumId;
    
    public LibraryFilterByAlbum(int albumId){
        this.albumId = albumId;
    }
}
