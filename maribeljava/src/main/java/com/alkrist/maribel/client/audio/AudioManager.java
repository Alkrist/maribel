package com.alkrist.maribel.client.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.WaveData;

public class AudioManager {

	private static List<Integer> buffers = new ArrayList<Integer>();
	
	public static void init() {
		long device = ALC10.alcOpenDevice((ByteBuffer)null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		long context = ALC10.alcCreateContext(device, (IntBuffer)null);
		
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
	}
	
	public static int loadWAVSound(String path) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveData = WaveData.create(FileUtil.getSoundsPath()+path+".wav");
		if(waveData == null) {
			System.out.println("FUCK!");
		}
		AL10.alBufferData(buffer, waveData.format, waveData.data, waveData.samplerate);
		waveData.dispose();
		return buffer;
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static void cleanUp() {
		for(int buffer: buffers) {
			AL10.alDeleteBuffers(buffer);
		}
	}
}
