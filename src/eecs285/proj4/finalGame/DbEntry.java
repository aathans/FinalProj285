package eecs285.proj4.finalGame;

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
