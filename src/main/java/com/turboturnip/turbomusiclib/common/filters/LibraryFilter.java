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
public abstract class LibraryFilter {
    public static interface Translator<OutputType> {
        public OutputType translateFilter(LibraryFilter toTranslate);
    }
}