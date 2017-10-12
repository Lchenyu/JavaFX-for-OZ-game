package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class Person
{
    private String id;
    private String name;
    private int age;
    private String state;
    private int points;

    private BooleanProperty select;


    public Person(){};

    public Person(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Person(String id, String name, int points)
    {
        this.id = id;
        this.name = name;
        this.points = points;

    }


    public Person(String id, String name, int age, String state)
    {
        this.id = id;
        this.name = name;
        this.age = age;
        this.state = state;
        points = 0;
        this.select = new SimpleBooleanProperty(false);
    }


    public abstract int compete();



    public boolean getSelected(){
    	return select.get();
    }

    public void setSelected(Boolean selected) {
        this.select.setValue(selected);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int p)
    {
        points = points + p;
    }
}
