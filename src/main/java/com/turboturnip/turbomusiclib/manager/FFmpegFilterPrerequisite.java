/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turboturnip.turbomusiclib.manager;

/**
 *
 * @author samuel
 */
public class FFmpegFilterPrerequisite {
    // The contents of this array will be inserted between "ffmpeg -i <path>" and "-f null -".
    // The output of the resulting command will be given to the FFMpegFilter using this prerequisite.
    public final String[] command;
    
    public FFmpegFilterPrerequisite(String... command){
        assert(command.length > 0);
        this.command = command;
    }
}
