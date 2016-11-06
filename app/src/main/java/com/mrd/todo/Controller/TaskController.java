package com.mrd.todo.Controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mrd.todo.Database.TaskDB;
import com.mrd.todo.Models.TaskResponse;
import com.mrd.todo.Utils.Constants;
import com.mrd.todo.Utils.NetworkRequestQueue;

import org.json.JSONObject;

/**
 * Created by mayurdube on 03/11/16.
 */

public class TaskController extends Controller {

    public static final int FETCH_TASKS = 1002;
    public static final int FETCH_TASK_FAILURE = 1003;
    private String TAG = TaskController.class.getSimpleName();
    Context mContext;
    private static TaskController sLoginController;

    public TaskController(Context con) {
        mContext = con;
    }

    public static TaskController getInstance(Context con) {
        if (sLoginController == null) {
            sLoginController = new TaskController(con);
        }
        return sLoginController;
    }


    @Override
    public boolean handleMessage(int what, Object data) {
        switch (what) {
            case FETCH_TASKS:
                fetchTasks();
                break;
        }
        return false;
    }

    private void fetchTasks() {
        Constants.log(TAG, "Fetching tasks : " + Constants.API);
        JsonObjectRequest getTaskRequest = new JsonObjectRequest(Constants.API,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Constants.log(TAG, "Response : " + response);
                        boolean isSuccessful = true;
                        try {
                            TaskResponse taskResponse = new Gson().fromJson(response.toString(), TaskResponse.class);
                            if (taskResponse != null && taskResponse.getTasklist() != null) {
                                TaskDB db = new TaskDB(mContext);
                                db.insertMultipleTasks(taskResponse.getTasklist());
                                notifyOutboxHandlers(FETCH_TASKS, 0, 0, taskResponse.getTasklist());
                            } else {
                                isSuccessful = false;
                            }
                        } catch (Exception ex) {
                            isSuccessful = true;
                            ex.printStackTrace();
                        } finally {
                            if (!isSuccessful) {
                                notifyOutboxHandlers(FAILURE, 0, 0, null);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                String errorMessage = "Oops! This didn't go as expected!";
                if (error instanceof NetworkError) {
                    errorMessage = "Is your device connected to a working internet connection?";
                } else if (error instanceof AuthFailureError) {

                } else if (error instanceof ParseError) {


                } else if (error instanceof NoConnectionError || error instanceof TimeoutError ||
                        error instanceof ServerError) {
                    errorMessage = "Could reach our server!";

                } else if (error instanceof TimeoutError) {
                    errorMessage = "Couldn't connect to dropbox link!";
                }
                notifyOutboxHandlers(FETCH_TASK_FAILURE,0,0,errorMessage);
            }
        });
        NetworkRequestQueue.getInstance(mContext).getRequestQueue().add(getTaskRequest);
    }
}
