package com.mrd.todo.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mrd.todo.R;


/**
 * Created by mayur dube on 29/08/16.
 */
public class TixdoProgressDialog extends Dialog {

    public Activity context;
    public String dialogText;
    private TextView tvDialog;
    private ViewFlipper flipper;
    private final static int DIALOG_HEIGHT = 600;


    public TixdoProgressDialog(Activity context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_tixdo_progress);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, DIALOG_HEIGHT);
        initDialog();
    }

    private void initDialog() {
        tvDialog = (TextView) findViewById(R.id.txt_dialog);
        flipper.setAutoStart(true);
        if(!TextUtils.isEmpty(dialogText))
            tvDialog.setText(dialogText);
    }

    public void setMessage(String msg){
        dialogText = msg;
    }



}
