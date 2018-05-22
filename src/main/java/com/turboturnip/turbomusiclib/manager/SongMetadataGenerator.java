/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

import java.io.File;

/**
 *
 * @author samuel
 */
public interface SongMetadataGenerator {
    SongMetadata FromFile(File file);
}
