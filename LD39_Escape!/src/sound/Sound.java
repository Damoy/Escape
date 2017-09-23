package sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	
	public static final Sound menuOption = new Sound("/sounds/menuoption.wav");
	public static final Sound menuSelect = new Sound("/sounds/menuselect.wav");
	public static final Sound playerShoot = new Sound("/sounds/playerhit.wav");
	public static final Sound loseHit = new Sound("/sounds/lose_hit.wav");
	public static final Sound powerup = new Sound("/sounds/powerup.wav");
	public static final Sound doorOpen = new Sound("/sounds/door_open.wav");
	public static final Sound goingThroughDoor = new Sound("/sounds/going_through_door.wav");
	
	private AudioClip clip;

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}