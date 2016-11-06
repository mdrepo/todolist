package com.mrd.todo.Screens;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrd.todo.Controller.TaskAdapter;
import com.mrd.todo.Database.TaskDB;
import com.mrd.todo.Models.Task;
import com.mrd.todo.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.data;

/**
 * Created by mayurdube on 04/11/16.
 */

public class PageFragment extends Fragment implements OnItemInteractionListener {
    public static final String ARG_PAGE = "tasklist";

    @BindView(R.id.rwTaskList)
    RecyclerView rwTaskList;
    @BindView(R.id.swifeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    TaskAdapter mAdapter;
    @BindView(R.id.vw_close_tip)
    View vwCloseTip;
    private int mStatus;
    private ArrayList<Task> mTasks;


    public static PageFragment newInstance(int page) {
        Log.d("Fragment", "newInstance");
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mStatus = args.getInt(ARG_PAGE);
    }

    @OnClick(R.id.vw_close_tip)
    public void closeTip() {
        vwCloseTip.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);
        if (mStatus == Task.DONE) {
            closeTip();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTasks();
            }
        });
        return view;
    }

    public void stopRefreshing() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void fetchTasks() {
        MainActivity ac = (MainActivity) getActivity();
        ac.fetchTasks();
    }

    public void showTasks() {
        if (rwTaskList != null) {
            mTasks = new TaskDB(getContext()).getTask(mStatus);
            if (mTasks != null) {
                rwTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
                mAdapter = new TaskAdapter(mTasks, this);
                rwTaskList.setAdapter(mAdapter);
            }
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchTasks();
                }
            });
        }
    }

    public void addTask(Task task) {
        if (mAdapter != null)
            mAdapter.addItem(task);
    }

    @Override
    public void onLongClick(final int position, Object show) {

        final Task task = mTasks.get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Delete task").
                setMessage("Do you want to delete this task?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                mAdapter.removeItem(position);
                TaskDB db = new TaskDB(getContext());
                db.deleteTask(task);
                dialog.dismiss();
                Snackbar snackbar = Snackbar.make(getView(),
                        "UNDO delete",
                        Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTask(task);

                    }
                }).show();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public void onClick(final int position, Object show) {
        if (mStatus == Task.PENDING) {
            final String basetext = "Updating task to done in ";
            final Task task = mTasks.get(position);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Update task").
                    setMessage(basetext + "5 seconds");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    TaskDB db = new TaskDB(getContext());
                    db.updateStatus(task.getId(), Task.DONE);
                    mAdapter.removeItem(position);
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();
            //final ProgressDialog pDialog = ProgressDialog.show(getContext(), null, basetext + "5 seconds", true);
            new Thread() {
                public void run() {
                    try {
                        int sec = 5;
                        while (sec != 0) {
                            final int finalSec = sec;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alert.setMessage(basetext + finalSec + " seconds");
                                }
                            });
                            Thread.sleep(1000);
                            sec = sec - 1;
                        }
                        TaskDB db = new TaskDB(getContext());
                        db.updateStatus(task.getId(), Task.DONE);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.dismiss();
                                mAdapter.removeItem(position);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }
}
