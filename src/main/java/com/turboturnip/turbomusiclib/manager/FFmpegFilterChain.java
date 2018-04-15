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
public abstract class FFmpegFilterChain {
    
    public FFmpegFilterChainPrerequisite prerequisiteCommand(){
        return null;
    }
    
    // The contents of this array will be inserted just after "ffmpeg -i <path>"
    public String[] extraInputs(){
        return null;
    }
    // This will be inserted as an lavfi filter between other filters in the chain. Do not specify the output, that will be done for you.
    protected abstract String filterCommand_internal(int extraInputStartIndex, String prerequisiteOutput);
    public final String filterCommand(int extraInputStartIndex, String prerequisiteOutput){
        synchronized(this){
            return filterCommand_internal(extraInputStartIndex, prerequisiteOutput);
        }
    }
}
