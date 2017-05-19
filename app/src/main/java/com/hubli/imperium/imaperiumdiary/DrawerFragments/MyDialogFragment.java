package com.hubli.imperium.imaperiumdiary.DrawerFragments;

/**
 * Created by Rafiq Ahmad on 5/17/2017.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.R;

public class MyDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText subject;
    private EditText teacher;
    private Button submit;


    public interface UserNameListener {
        void onFinishUserDialog(String subject, String teacher);
    }

    // Empty constructor required for DialogFragment
    public MyDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_subject, container);
        subject = (EditText) view.findViewById(R.id.subject);
        teacher = (EditText) view.findViewById(R.id.teachername);
        submit = (Button) view.findViewById(R.id.finish);

        // set this instance as callback for editor action
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNameListener activity = (UserNameListener) getActivity();
                activity.onFinishUserDialog(subject.getText().toString(),teacher.getText().toString());

            }
        });
        subject.setOnEditorActionListener(this);
        subject.requestFocus();
        teacher.setOnEditorActionListener(this);
        teacher.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Please enter username");

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Return input text to activity
        UserNameListener activity = (UserNameListener) getActivity();
        activity.onFinishUserDialog(subject.getText().toString(),teacher.getText().toString());
        this.dismiss();
        return true;
    }
}