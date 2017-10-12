package model;
import java.util.ArrayList;

public class IDlist
{
    /*
    * for check duplication ID when adding participants
    * */

    private ArrayList<String> addIDList;

    public IDlist()
    {
        this.addIDList = new ArrayList<>();
    }

    public ArrayList<String> getAddIDList()
    {
        return addIDList;
    }

    public void setAddIDList(String id)
    {
        addIDList.add(id);
    }
}
