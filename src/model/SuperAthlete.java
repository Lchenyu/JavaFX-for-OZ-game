package model;

import java.util.Random;

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
        	Random randomGenerator = new Random();
    		return randomGenerator.nextInt(301) + 500;

        }

        if (gameType == 2)
        {
        	Random randomGenerator = new Random();
    		return randomGenerator.nextInt(11) + 10;
        }

        if (gameType == 1)
        {
        	Random randomGenerator = new Random();
    		return randomGenerator.nextInt(101) + 100;
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
