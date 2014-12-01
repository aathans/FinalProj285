package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alex on 12/1/14.
 * The score screen when navigated to from the main menu.  Receives
 * the most up to date scores and displays them.
 */
public class Scores
{
  private BufferedImage scoresImage;
  private DbEntry[] highScores;
  private final int NUM_SCORES = 8;

  Scores()
  {
    GameLogic.gameState = GameLogic.GameState.SCORE1;
    LoadScores();
    highScores = new DbEntry[NUM_SCORES];
  }


  public void LoadScores()
  {
    try
    {
      URL scoresPage = getClass().getResource("/images/scores.png");
      scoresImage = ImageIO.read(scoresPage);

    }
    catch( IOException ex )
    {
      Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void updateHighScores(DbEntry[] entries)
  {
    int i = 0;
    for( DbEntry e : entries )
    {
      highScores[i] = e;
      i++;
      if( i >= NUM_SCORES )
      {
        break;
      }
    }
  }

  public void Draw(Graphics2D g2d){
    g2d.drawImage(scoresImage,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);
    int yValue = 150;
    for(DbEntry e : highScores){
      String scoreString = String.valueOf(e.getScore());
      create(GameLogic.frameWidth/2 + 50, yValue,scoreString,g2d);
      create1(GameLogic.frameWidth/7,yValue,e.getUser(),g2d);
      yValue += 50;
    }

  }

  public void create(int x,int y,String str,Graphics2D g2d){
    Color textColor = Color.GREEN;
    Color bgColor = Color.CYAN;

    FontMetrics fm = g2d.getFontMetrics();

    g2d.setColor(textColor);

    Font font = new Font("Serif", Font.PLAIN,30 );
    g2d.setFont(font);

    g2d.drawString(str, x, y);
  }

  public void create1(int x,int y,String str,Graphics2D g2d){
    Color textColor = Color.BLACK;
    Color bgColor = Color.CYAN;

    FontMetrics fm = g2d.getFontMetrics();

    g2d.setColor(textColor);

    Font font = new Font("Serif", Font.PLAIN,30 );
    g2d.setFont(font);

    g2d.drawString(str, x, y);
  }
}
