package com.mrd.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mrd.todo.Models.Task;

import java.util.ArrayList;

/**
 * Created by mayurdube on 04/11/16.
 */

public class TaskDB {
    public static final String TABLE_NAME = "tasks";
    SQLiteDatabase mDatabase;
    TaskDBHelper mDbHelper;

    // Columns
    private static String ID = "_id";
    private static String TASKNAME = "taskname";
    private static String STATUS = "status";

    private String[] getColumns() {
        return new String[]{
                ID, TASKNAME, STATUS
        };
    }

    public TaskDB(Context context) {
        mDbHelper = new TaskDBHelper(context);
    }

    public static final String CREATE_TASKS_DB = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " " + TaskDBHelper.INT_TYPE + " PRIMARY KEY" + TaskDBHelper.COMMA_SEP +
            TASKNAME + " " + TaskDBHelper.TEXT_TYPE + TaskDBHelper.COMMA_SEP +
            STATUS + " " + TaskDBHelper.BOOL + ")";

    public void open() {
        mDatabase = mDbHelper.getReadableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long insertTask(Task task) {
        boolean saveStatus = false;
        long id = -1;
        try {
            ContentValues content;
            content = new ContentValues();
            content.put(ID, task.getId() == -1 ? null : task.getId());
            content.put(TASKNAME, task.getName());
            content.put(STATUS, task.getState() == Task.PENDING ? false : true);
            open();
            id = mDatabase.insert(TABLE_NAME, null, content);
            close();
            if (id != -1) {
                saveStatus = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertMultipleTasks(ArrayList<Task> taskList) {
        try {
            ContentValues content;
            for (Task task : taskList) {
                content = new ContentValues();
                content.put(ID, task.getId() == -1 ? null : task.getId());
                content.put(TASKNAME, task.getName());
                content.put(STATUS, task.getState() == Task.PENDING ? false : true);
                open();
                mDatabase.insert(TABLE_NAME, null, content);
                close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getAllTasks() {
        open();
        ArrayList<Task> list = new ArrayList<>();
        try {
            Cursor cursor = mDatabase.query(TABLE_NAME, getColumns(), null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    task.setName(cursor.getString(cursor.getColumnIndex(TASKNAME)));
                    task.setState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    list.add(task);
                } while (cursor.moveToNext());
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Task> getTask(int status) {
        open();
        String selectionArgs[] = {"" + status};

        ArrayList<Task> list = new ArrayList<>();
        try {
            Cursor cursor = mDatabase.query(TABLE_NAME, getColumns(), STATUS+"=?", selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    task.setName(cursor.getString(cursor.getColumnIndex(TASKNAME)));
                    task.setState(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    list.add(task);
                } while (cursor.moveToNext());
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean updateStatus(int id, int status) {
        boolean saveStatus = false;
        ContentValues content;
        content = new ContentValues();
        content.put(STATUS, status == Task.PENDING ? false : true);
        open();
        id = mDatabase.update(TABLE_NAME, content, "_id=" + id, null);
        close();
        if (id != -1) {
            saveStatus = true;
        }
        return saveStatus;
    }

    public void deleteTask(Task task) {
        open();
        mDatabase.delete(TABLE_NAME, ID + "=" + task.getId(), null);
        close();
    }

    public void deleteAll() {
        open();
        mDatabase.delete(TABLE_NAME, null, null);
        close();
    }
}
