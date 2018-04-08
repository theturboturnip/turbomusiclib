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
public class LibraryFilterAND extends LibraryFilter{
    // Should evaluate to (a AND b AND c...)
    LibraryFilter[] subfilters;
    
    public LibraryFilterAND(LibraryFilter... subfilters){
        this.subfilters = subfilters;
    }
}
