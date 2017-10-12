package model;
public class Result
{
    private String id;
    private int re;

    public Result(String id, int re)
    {
        this.id = id;
        this.re = re;
    }


    public String getId()
    {
        return id;
    }

    public int getRe()
    {
        return re;
    }
}
