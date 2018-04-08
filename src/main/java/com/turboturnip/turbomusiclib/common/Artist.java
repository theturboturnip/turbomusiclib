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
public class Artist {
    public final int id;
    public final String name;
    
    public Artist(Artist copyFrom, int newId){
        this(newId, copyFrom.name);
    }
    public Artist(int id, String name){
        assert(id >= 0);
        assert(name != null);
        
        this.id = id;
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Artist)) return false;
        return id == ((Artist)o).id;
    }
    @Override
    public int hashCode(){
        return id;
    }
}
