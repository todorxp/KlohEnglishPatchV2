package com.mg.uros.klohenglishpatchv2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;



public class CustomDialog extends Dialog  implements  android.view.View.OnClickListener{

    public Activity c;
    public Dialog d;
    public Button ok;
    public  TextView text;
    public  TextView alertTitle ;
    public boolean killapp;

    public CustomDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog_one_button);
        ok = (Button) findViewById(R.id.alert_dialog_button_OK);
        ok.setOnClickListener(this);

    }

    @Override
    public void setTitle(CharSequence title) {
        alertTitle = (TextView) findViewById(R.id.alert_title);
        alertTitle.setText(title);
    }
    public void setMessage(CharSequence message){
        text = (TextView) findViewById(R.id.alert_message);
        text.setText(message);
    }

    @Override
    public void onClick(View v) {

        dismiss();

        if (killapp)
        {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}







