package com.hubli.imperium.imaperiumdiary.Settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    private SPData spData;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText old = (EditText) findViewById(R.id.old_password);
        final EditText newPass = (EditText) findViewById(R.id.new_pass);
        final EditText rePass = (EditText) findViewById(R.id.reenter_pass);
        Button done = (Button) findViewById(R.id.pass_done);


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            old.setBackgroundResource(R.drawable.round_corner);
            newPass.setBackgroundResource(R.drawable.round_corner);
            rePass.setBackgroundResource(R.drawable.round_corner);
            done.setBackgroundResource(R.drawable.round_corner_btn);
        }

        spData = new SPData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getApplicationContext();

                if(newPass.getText().toString().length() > 6) {
                    if (newPass.getText().toString().contentEquals(rePass.getText().toString())) {
                        progressDialog.show();
                        new MyVolley(context, new IVolleyResponse() {
                            @Override
                            public void volleyResponse(String result) {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String oldPass = jsonObject.getString("password");
                                    if (old.getText().toString().trim().contentEquals(oldPass)) {
                                        changePassword(newPass.getText().toString());
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Incorrect old Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void volleyError() {
                                progressDialog.dismiss();
                            }
                        }).setUrl(URL.CHANGE_PASSWORD)
                        .setParams(SPData.USER_NUMBER, spData.getUserData(SPData.USER_NUMBER))
                        .connect();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Re-Entered Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Password length should me more than 6", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword(String password){
        new MyVolley(getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                progressDialog.dismiss();
                if(result.contentEquals("DONE")){
                    Toast.makeText(getApplicationContext(),
                            "Done",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void volleyError() {
                Toast.makeText(getApplicationContext(),
                        "Failed",Toast.LENGTH_SHORT).show();
            }
        }).setUrl(URL.CHANGE_PASSWORD)
        .setParams(SPData.USER_NUMBER, spData.getUserData(SPData.USER_NUMBER))
        .setParams("new_password",password)
        .connect();
    }
}
