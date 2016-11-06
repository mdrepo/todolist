package com.mrd.todo.Models;

/**
 * Created by mayurdube on 03/11/16.
 */

public class Task {

    public static final int DONE = 1;
    public static final int PENDING = 0;
    private int id = -1;

    private String name;

    private int state;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public int getState ()
    {
        return state;
    }

    public void setState (int state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", name = "+name+", state = "+state+"]";
    }
}
