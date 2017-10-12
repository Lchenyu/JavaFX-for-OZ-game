package model;
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
        return 100 + (int)(Math.random() * 101);
    }
}
