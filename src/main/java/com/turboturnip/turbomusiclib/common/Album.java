/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common;

/**
 *
 * @author samuel
 */
public class Album {
    public final int id;
    public final String name;
    public final int artistId;
    
    public Album(Album copyFrom, int newId){
        this(newId, copyFrom.name, copyFrom.artistId);
    }
    public Album(Album copyFrom, int newId, int newArtistId){
        this(newId, copyFrom.name, copyFrom.artistId);
    }
    public Album(int id, String name, Artist artist){
        this(id, name, artist.id);
    }
    public Album(int id, String name, int artistId){
        assert(id >= 0);
        assert(name != null);
        assert(artistId >= 0);
        
        this.id = id;
        this.name = name;
        this.artistId = artistId;
    }
        
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Album)) return false;
        return id == ((Album)o).id;
    }
    @Override
    public int hashCode(){
        return id;
    }
}
