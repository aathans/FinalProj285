package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * Created by Alex on 11/17/14.
 * Game contains all the properties of the racing game and handles the
 * player.
 */
public class Game
{

  private final int scoreLabelY = 15;

  private Player playerOne;

  private Multiplayer p;

  private boolean isMultiplayer;

  private int playerScore = 0;

  private boolean playerCrashed = false;

  private int opponentScore = 0;

  private boolean opponentCrashed = false;

  private Obstacle[] obstacleList;

  private PowerUp[] powerUpList;

  private BufferedImage background;

  private BufferedImage[] leftLines;

  private BufferedImage[] rightLines;

  private int lineHeight;
  private int lineWidth;
  private int lineTop;
  private PowerUp powerUpUsed;
  private int lineSpeed;
  private int carColour;

  public Game(final boolean inIsMultiplayer, final int inCarColour)
  {
    GameLogic.gameState = GameLogic.GameState.LOADING;
    isMultiplayer = inIsMultiplayer;
    carColour = inCarColour;

    Thread initializeGame = new Thread()
    {
      @Override
      public void run()
      {
        setup();
        loadGame();
        if( isMultiplayer )
        {
          p = new Multiplayer("35.2.254.140", 2000);
          p.createConnection();
        }
        GameLogic.gameState = GameLogic.GameState.PLAYING;
      }
    };
    initializeGame.start();
  }


  private void setup()
  {
    leftLines = new BufferedImage[2];
    rightLines = new BufferedImage[2];
    playerOne = new Player(carColour);

    obstacleList = new Obstacle[6];
    powerUpList = new PowerUp[6];
    for( int i = 0; i < 6; i++ )
    {
      powerUpList[i] = new MissilePowerUp();
    }
    for( int i = 0; i < 3; i++ )
    {
      obstacleList[i] = new CarObstacle();
      obstacleList[i + 3] = new WallObstacle();
    }

    lineSpeed = 0;
    lineTop = 0;
  }

  private void loadGame()
  {
    try
    {
      URL backgroundPath = this.getClass().getResource("/images/road.jpg");
      background = ImageIO.read(backgroundPath);

      URL linesImagePath = this.getClass().getResource("/images/lines.jpg");
      leftLines[0] = ImageIO.read(linesImagePath);
      leftLines[1] = ImageIO.read(linesImagePath);
      rightLines[0] = ImageIO.read(linesImagePath);
      rightLines[1] = ImageIO.read(linesImagePath);
      lineHeight = leftLines[0].getHeight();
      lineWidth = leftLines[0].getWidth();

    }
    catch( IOException ex )
    {
      Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void addObstacle(int obstacleType)
  {

    int start = obstacleType * 3;
    int end = start + 3;
    for( int i = start; i < end; i++ )
    {
      if( !obstacleList[i].inPlay )
      {
        obstacleList[i].inPlay = true;
        break;
      }
    }

  }

  public void addPowerUp()
  {
    for( int i = 0; i < 6; i++ )
    {
      if( !powerUpList[i].inPlay )
      {
        powerUpList[i].inPlay = true;
        break;
      }
    }
  }

  public void restart()
  {
    playerOne.reset();
    for( int i = 0; i < 6; i++ )
    {
      obstacleList[i].reset();
    }
    for( int i = 0; i < 6; i++ )
    {
      powerUpList[i].reset();
    }
  }

  public boolean updateGame()
  {
    playerOne.update();
    lineSpeed = playerOne.getSpeedY();

    //Check for collision with powerup
    int powerUpRetrieved = collidedWithPowerup();
    if( powerUpRetrieved >= 0 )
    {
      playerOne.addPowerUp(powerUpList[powerUpRetrieved]);
    }

    //Handle power up being used
    if( powerUpUsed != null )
    {
      int objectHitWithPowerUp = powerUpCollidedWithObject();
      if( objectHitWithPowerUp >= 0 )
      {
        playerOne.incrementScoreBy(500);
        powerUpUsed.reset();
        obstacleList[objectHitWithPowerUp].reset();
        powerUpUsed = null;
      }
    }

    playerScore = playerOne.getScore();

    playerCrashed = collidedWithObject();

    if( isMultiplayer )
    {
      String pScore = String.valueOf(playerScore);
      p.sendUpdate(pScore);
      opponentScore = p.getOpponentScore();
      opponentCrashed = p.isOpponentFinished();
      if( playerCrashed )
      {
        p.sendUpdate("-1");
        lineSpeed = 0;
      }
    }

    return playerCrashed;
  }

  private boolean collidedWithObject()
  {
    for( int i = 0; i < 6; i++ )
    {
      if( obstacleList[i].inPlay )
      {
        boolean collided = obstacleList[i]
            .checkForCollision(playerOne.getxPos(), playerOne.getyPos(),
                playerOne.getCarWidth(), playerOne.getCarHeight());
        if( collided )
        {
          return true;
        }
      }
    }
    return false;
  }

  private int collidedWithPowerup()
  {
    for( int i = 0; i < 6; i++ )
    {
      if( powerUpList[i].inPlay && !powerUpList[i].wasRetrieved )
      {
        boolean collided = powerUpList[i]
            .checkForCollision(playerOne.getxPos(), playerOne.getyPos(),
                playerOne.getCarWidth(), playerOne.getCarHeight());
        if( collided )
        {
          return i;
        }
      }
    }
    return -1;
  }

  private int powerUpCollidedWithObject()
  {
    for( int i = 0; i < 6; i++ )
    {
      if( obstacleList[i].inPlay )
      {
        boolean collided = obstacleList[i]
            .checkForCollision(powerUpUsed.getxPos(), powerUpUsed.getyPos(),
                powerUpUsed.getPowerUpWidth(), powerUpUsed.getPowerUpHeight());
        if( collided )
        {
          return i;
        }
      }
    }
    return -1;
  }

  public boolean opponentHasCrashed()
  {
    if( !opponentCrashed )
    {
      opponentScore = p.getOpponentScore();
      opponentCrashed = p.isOpponentFinished();
    }
    return opponentCrashed;
  }

  public void Draw(Graphics2D g2d)
  {
    lineTop += lineSpeed;
    if( lineTop > GameLogic.frameHeight + 14 )
    {
      lineTop = 0;
    }
    g2d.drawImage(background, 0, 0, GameLogic.frameWidth, GameLogic.frameHeight,
        null);
    g2d.drawImage(leftLines[0], GameLogic.frameWidth / 3 - lineWidth / 2,
        lineTop, lineWidth, lineHeight, null);
    g2d.drawImage(leftLines[1], GameLogic.frameWidth / 3 - lineWidth / 2,
        -1 * lineHeight + lineTop, lineWidth, lineHeight, null);
    g2d.drawImage(rightLines[0], 2 * GameLogic.frameWidth / 3 - lineWidth / 2,
        lineTop, lineWidth, lineHeight, null);
    g2d.drawImage(rightLines[1], 2 * GameLogic.frameWidth / 3 - lineWidth / 2,
        -1 * lineHeight + lineTop, lineWidth, lineHeight, null);
    if( obstacleList.length != 0 )
    {
      for( Obstacle o : obstacleList )
      {
        if( o != null && o.inPlay )
        {
          o.update(g2d, lineSpeed);
        }
      }
    }

    if( powerUpList.length != 0 )
    {
      for( PowerUp p : powerUpList )
      {
        if( p != null && p.inPlay && (!p.wasRetrieved || p.wasUsed) )
        {
          p.update(g2d, lineSpeed);
        }
      }
    }
    playerOne.Draw(g2d);
    g2d.setColor(Color.white);
    int labelY = scoreLabelY;
    g2d.drawString("Score: " + playerScore, 5, labelY);
    if( isMultiplayer )
    {
      g2d.setColor(Color.red);
      labelY += 20;
      g2d.drawString("Opponent: " + opponentScore, 5, labelY);
      g2d.setColor(Color.white);
    }
  }

  public void usePowerUp()
  {
    powerUpUsed = playerOne.usePowerUp();
  }

  public void DrawEnd(Graphics2D g2d)
  {
    Draw(g2d);
    g2d.setColor(Color.white);

    int labelY = scoreLabelY;
    g2d.drawString("Score: " + playerScore, 5, labelY);
    if( isMultiplayer )
    {
      labelY += 20;
      g2d.setColor(Color.red);
      g2d.drawString("Opponent: " + opponentScore, 5, labelY);
      g2d.setColor(Color.white);
      if( playerScore > opponentScore )
      {
        g2d.drawString("YOU WON!", GameLogic.frameWidth / 2 - 30,
            GameLogic.frameHeight / 2);
      }
      else if( playerScore < opponentScore )
      {
        g2d.drawString("YOU LOST!", GameLogic.frameWidth / 2 - 30,
            GameLogic.frameHeight / 2);
      }
      else
      {
        g2d.drawString("YOU TIED!", GameLogic.frameWidth / 2 - 30,
            GameLogic.frameHeight / 2);
      }
    }
    else
    {
      g2d.drawString("GAME OVER", GameLogic.frameWidth / 2 - 40,
          GameLogic.frameHeight / 2 + 50);
      g2d.drawString("Press any key to restart", GameLogic.frameWidth / 2 - 75,
          GameLogic.frameHeight / 2 + 30);
    }
  }

  public void DrawCrashed(Graphics2D g2d)
  {
    playerOne.crashed = true;
    Draw(g2d);
    playerOne.reset();
    lineSpeed = 0;

    if( !isMultiplayer )
    {
      g2d.drawString("CRASHED", GameLogic.frameWidth / 2 - 30,
          GameLogic.frameHeight / 2 + 50);
      g2d.drawString("Press any key to restart", GameLogic.frameWidth / 2 - 75,
          GameLogic.frameHeight / 2 + 30);
    }
    else
    {
      g2d.drawString("Waiting for opponent to finish",
          GameLogic.frameWidth / 2 - 75, GameLogic.frameHeight / 2);
    }

  }

  public int getPlayerScore()
  {
    return playerScore;
  }
}
