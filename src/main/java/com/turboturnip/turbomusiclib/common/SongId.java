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
public class SongId {
    public final int sourceIndex;
    public final int songIndex;
    
    public SongId(int songIndex){
        this(-1, songIndex);
    }
    public SongId(int sourceIndex, int songIndex){
        this.sourceIndex = sourceIndex;
        this.songIndex = songIndex;
    }
    
    @Override
    public int hashCode(){
        return 7 * sourceIndex ^ 13 * songIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SongId other = (SongId) obj;
        if (this.sourceIndex != other.sourceIndex) {
            return false;
        }
        if (this.songIndex != other.songIndex) {
            return false;
        }
        return true;
    }
}
