package model;

import java.util.Random;

public class Cyclist extends Person
{
    public Cyclist(String id, String name, int age, String state)
    {
        super(id, name, age, state);
    }

    public Cyclist(String id, String name, int point)
    {
        super(id, name, point);
    }



    /*
    * get competition result by random*/
    @Override
    public int compete()
    {
        //return 500 + (int)(Math.random() * 301);
    	Random randomGenerator = new Random();
		return randomGenerator.nextInt(301) + 500;
    }
}
