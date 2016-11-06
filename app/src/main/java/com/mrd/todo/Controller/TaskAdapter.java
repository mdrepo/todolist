package com.mrd.todo.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrd.todo.Models.Task;
import com.mrd.todo.R;
import com.mrd.todo.Screens.OnItemInteractionListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mayurdube on 03/11/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    ArrayList<Task> mTasks = null;
    OnItemInteractionListener mListener;
    public TaskAdapter(ArrayList<Task> tasks, OnItemInteractionListener listener) {
        mTasks = tasks;
        mListener = listener;
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task,parent,false);
        TaskViewHolder holder = new TaskViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.bind(mTasks.get(position));
        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return mTasks==null?0:mTasks.size();
    }

    public void addItem(Task task) {
        if(mTasks == null)
            mTasks = new ArrayList<>();

        mTasks.add(task);
        notifyItemInserted(mTasks.size());
    }

    public void removeItem(int position) {
        mTasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTasks.size());
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txtTaskName)
        TextView txtTaskName;
        @BindView(R.id.txtTaskStatus)
        TextView txtStatus;
        @BindView(R.id.txtTaskId)
        TextView txtTaskId;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Task task) {
            if(task != null) {
                txtTaskName.setText(task.getName());
                txtTaskId.setText("" + task.getId());
                if(task.getState() == Task.DONE) {
                    txtStatus.setText("Completed");
                } else {
                    txtStatus.setText("Pending");
                }
            }
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition(),view);
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.onLongClick(getAdapterPosition(),view);
            return false;
        }
    }
}
