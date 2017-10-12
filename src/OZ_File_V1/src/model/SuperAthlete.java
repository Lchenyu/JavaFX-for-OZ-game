package model;

public class SuperAthlete extends Person
{
    private int gameType;

    public SuperAthlete(String id, String name, int age, String state)
    {
        super(id, name, age, state);
    }

    public SuperAthlete(String id, String name, int point)
    {
        super(id, name, point);
    }
    /*
    * get competition result by random
    * the superAthlete can play three types game
    * and different types have different range of result
    * thus it is needed to jude which types game that the superAthlete join before generate the result
    * the judgement dependents on gameID which is distinguished by different types of game
    * C or c for Cycling, R or r for Running, S or s for Swimming*/
    @Override
    public int compete()
    {
        if(gameType == 3 )
        {
            return 500 + (int)(Math.random() * 301);

        }

        if (gameType == 2)
        {
            return 10 + (int)(Math.random() * 11);
        }

        if (gameType == 1)
        {
            return 100 + (int)(Math.random() * 101);
        }
        else return 0;
    }


    public int getGameType()
    {
        return gameType;
    }

    public void setGameType(int gameType)
    {
        this.gameType = gameType;
    }
}
