package model;

import java.util.Random;

public class Swimmer extends Person
{
    public Swimmer(String id, String name, int age, String state)
    {
        super(id, name, age, state);
    }

    public Swimmer(String id, String name, int point)
    {
        super(id, name, point);
    }

    /*
    * get competition result by random*/
    @Override
    public int compete()
    {
        //return 100 + (int)(Math.random() * 101);
        Random randomGenerator = new Random();
		return randomGenerator.nextInt(101) + 100;
    }
}
