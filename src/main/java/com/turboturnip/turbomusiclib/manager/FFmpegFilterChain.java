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
    
    // These will be inserted as extra audio sources like "ffmpeg -i [input_file] -f lavfi -i [extra_audio_source_1] -i [extra_audio_source_2]..."
    public String[] extraAudioSources(){
        return null;
    }
    // This has to be synchronized so that filter chain can use static things like Patterns?
    // This will be inserted as an lavfi filter between other filters in the chain. Do not specify the output, that will be done for you.
    protected abstract String filterCommand_internal(int extraAudioSourcesStartIndex, String prerequisiteOutput);
    public synchronized final String filterCommand(int extraAudioSourcesStartIndex, String prerequisiteOutput){
        return filterCommand_internal(extraAudioSourcesStartIndex, prerequisiteOutput);
    }
}
