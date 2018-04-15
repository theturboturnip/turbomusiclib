/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.common.library_filters;

/**
 *
 * @author samuel
 */
public class LibraryFilterOR extends LibraryFilter {
    // Should evaluate to (a OR b OR c...)
    LibraryFilter[] subfilters;
    
    public LibraryFilterOR(LibraryFilter... subfilters){
        this.subfilters = subfilters;
    }
}
