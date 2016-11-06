package com.mrd.todo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mayurdube on 03/11/16.
 */

public class TaskResponse {

    @SerializedName("data")
    private ArrayList<Task> mTasklist;

    public ArrayList<Task> getTasklist() {
        return mTasklist;
    }

    public void setTasklist(ArrayList<Task> tasklist) {
        this.mTasklist = tasklist;
    }
}
