package com.mrd.todo.Screens;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.mrd.todo.Database.TaskDB;
import com.mrd.todo.Models.Task;
import com.mrd.todo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddTaskDialog extends Dialog {


    public MainActivity context;
    @BindView(R.id.swState)
    public Switch mStateSwitch;
    @BindView(R.id.edtaskName)
    public EditText mEdTaskName;

    public AddTaskDialog(MainActivity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_add_task);
        ButterKnife.bind(this);
        mStateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mStateSwitch.setText("DONE");
                } else {
                    mStateSwitch.setText("PENDING");
                }
            }
        });
    }

    @OnClick({R.id.create_action,R.id.cancel_action})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_action:
                String taskName = mEdTaskName.getText().toString();
                if(!TextUtils.isEmpty(taskName)) {
                    int state = mStateSwitch.isChecked()?Task.DONE:Task.PENDING;
                    Task task = new Task();
                    task.setName(taskName);
                    task.setState(state);
                    TaskDB db = new TaskDB(getContext());
                    int id = (int) db.insertTask(task);
                    task.setId(id);
                    context.addTask(task);
                    dismiss();

                } else {
                    mEdTaskName.setError("Please enter some task name!");
                }
                break;
            case R.id.cancel_action:
                dismiss();
                break;
        }
    }


}
