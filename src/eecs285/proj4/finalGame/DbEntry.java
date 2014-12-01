package eecs285.proj4.finalGame;


/**
 * The database entry for user's scores in Road Racer.
 */
public class DbEntry
{
  private String user;
  private int score;

  public DbEntry()
  {
    user = "";
    score = 0;
  }

  public DbEntry(String u, int s)
  {
    user = u;
    score = s;
  }

  public String getUser()
  {
    return user;
  }

  public int getScore()
  {
    return score;
  }
}
