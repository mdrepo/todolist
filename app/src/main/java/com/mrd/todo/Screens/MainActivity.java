package com.mrd.todo.Screens;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mrd.todo.Controller.PagerAdapter;
import com.mrd.todo.Controller.TaskController;
import com.mrd.todo.Models.Task;
import com.mrd.todo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    Handler mHandler;
    TaskController taskController;
    ProgressDialog mProgressDialog;
    private PageFragment mPendingFragment, mDoneFragment;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHandler = new Handler(this);
        taskController = TaskController.getInstance(getApplicationContext());
        mProgressDialog = new ProgressDialog(this);
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Done"));
        mAdapter = new PagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(PageFragment.newInstance(Task.PENDING), "Pending");
        mAdapter.addFragment(PageFragment.newInstance(Task.DONE), "Done");
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);


        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                PageFragment fragment = (PageFragment) mAdapter.getItem(position);
                fragment.showTasks();
            }
        });
    }

    public void fetchTasks() {
        mProgressDialog.setMessage("Fetching tasks..");
        mProgressDialog.show();
        taskController.handleMessage(TaskController.FETCH_TASKS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskController.addOutboxHandler(mHandler);
        fetchTasks();

    }

    @Override
    protected void onPause() {
        super.onPause();
        taskController.removeOutboxHandler(mHandler);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        AddTaskDialog dialog = new AddTaskDialog(this);
        dialog.show();
    }

    @Override
    public boolean handleMessage(Message message) {
        mProgressDialog.dismiss();

        switch (message.what) {
            case TaskController.FETCH_TASKS:
                PageFragment f = (PageFragment) mAdapter.getItem(viewPager.getCurrentItem());
                f.showTasks();
                f.stopRefreshing();
                break;
            case TaskController.FETCH_TASK_FAILURE:
                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main),
                        (String) message.obj,
                        Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchTasks();
                    }
                });
                snackbar.show();
                break;
            case TaskController.FAILURE:
                Toast.makeText(getApplicationContext(), (CharSequence) message.obj, Toast.LENGTH_SHORT).show();

                break;
        }
        return false;
    }

    public void addTask(Task task) {
        PageFragment f = (PageFragment) mAdapter.getItem(task.getState());
        f.addTask(task);

    }
}
