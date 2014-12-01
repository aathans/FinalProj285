package eecs285.proj4.finalGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Created by Alex on 11/29/14.
 * Plays a song in the background on a background thread.
 */
public class SongPlayer
{

  private static Thread thread;

  private static Clip backgroundSong;

  public static boolean isPlaying;

  SongPlayer(String songName)
  {
    playSound(songName);
  }

  private static synchronized void playSound(final String url)
  {
    thread = new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          backgroundSong = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              this.getClass().getResourceAsStream(url));
          backgroundSong.open(inputStream);
          backgroundSong.loop(Clip.LOOP_CONTINUOUSLY);
          isPlaying = true;
        }
        catch( Exception e )
        {
          System.out.println("Error with background song: " + e.getMessage());
        }
      }
    });

    thread.start();
  }

  public static void toggleSound()
  {
    if( isPlaying )
    {
      pauseSound();
    }
    else
    {
      playSound();
    }
  }

  private static void pauseSound()
  {
    backgroundSong.stop();
    isPlaying = false;
  }

  private static void playSound()
  {
    backgroundSong.loop(Clip.LOOP_CONTINUOUSLY);
    //backgroundSong.start();
    isPlaying = true;
  }
}
