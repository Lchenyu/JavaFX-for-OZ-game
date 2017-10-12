package model;
import java.util.Comparator;

public class ResultComparator implements Comparator<Result>
{
    @Override
    public int compare(Result o1, Result o2)
    {
        return o1.getRe() - o2.getRe();
    }
}

