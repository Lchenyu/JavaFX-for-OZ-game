package model;
public class Sprinter extends Person
{
    public Sprinter(String id, String name, int age, String state)
    {
        super(id, name, age, state);
    }

    public Sprinter(String id, String name, int point)
    {
        super(id, name, point);
    }

    /*
    * get competition result by random*/
    @Override
    public int compete()
    {
        return 10 + (int)(Math.random() * 11);
    }
}
