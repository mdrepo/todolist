package com.mrd.todo;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.mrd.todo.Database.TaskDB;
import com.mrd.todo.Models.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mayurdube on 06/11/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DatabaseTest {
    TaskDB taskDB;

    @Before
    public void setUp(){
        taskDB = new TaskDB(InstrumentationRegistry.getTargetContext());
        taskDB.open();
    }

    @After
    public void finish() {
        taskDB.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(taskDB);
    }

    @Test
    public void testDelete() {
        Task task = new Task();
        task.setId(12);
        task.setName("New task");
        task.setState(Task.PENDING);
        taskDB.deleteAll();
        taskDB.insertTask(task);
        List<Task> taskList = taskDB.getAllTasks();

        assertThat(taskList.size(), is(+1));

        taskDB.deleteTask(task);
        taskList = taskDB.getAllTasks();

        assertThat(taskList.size(), is(0));
    }


}
