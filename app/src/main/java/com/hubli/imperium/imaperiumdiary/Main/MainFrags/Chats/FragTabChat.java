package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Chats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hubli.imperium.imaperiumdiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabChat extends Fragment {


    public FragTabChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_tab_chat, container, false);
    }

}
