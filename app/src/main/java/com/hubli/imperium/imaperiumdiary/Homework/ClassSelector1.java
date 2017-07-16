package com.hubli.imperium.imaperiumdiary.Homework;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassSelector1 extends Fragment {


    public ClassSelector1() {
        // Required empty public constructor
    }

    private List<ClassDivisions> classes = new ArrayList<>();
    private boolean backPressed = false;

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_selector, container, false);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        setListClickListner(listView);
        getActivity().setTitle(R.string.select_class);
        MyClassAdaptor myAdaptor = new MyClassAdaptor();
        listView.setAdapter(myAdaptor);
        if(!backPressed) {
            getDateFromServer(myAdaptor, progressBar);
        }
        return rootView;
    }

    private void setListClickListner(final ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                backPressed = true;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                DivisionSelector divisionSelector = new DivisionSelector();
                Bundle bundle = new Bundle();
                bundle.putString("class",classes.get(position).getClassName());
                bundle.putStringArray("divisions",classes.get(position).getDivisions());
                divisionSelector.setArguments(bundle);
                transaction.replace(R.id.main_con,divisionSelector );
                transaction.addToBackStack("attendance");
                transaction.commit();
            }
        });
    }

    private void getDateFromServer(final MyClassAdaptor myAdaptor, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                try {
                    classes.clear();
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        classes.add(new ClassDivisions(jsonObject.getString("class"),jsonObject.getString("division")));
                    }
                    myAdaptor.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void volleyError() {
                progressBar.setVisibility(View.GONE);
            }
        }).setUrl(URL.CLASSES_DIVISIONS_SUBJECTS).connect();
    }


    private class MyClassAdaptor extends ArrayAdapter<ClassDivisions>{

        public MyClassAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.selector_item, classes);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getActivity().getLayoutInflater().inflate(R.layout.selector_item, parent, false);
            }

            TextView class_text=(TextView)ItemView.findViewById(R.id.selector_id) ;
            class_text.setText("Class ");

            String s1 = String.valueOf(classes.get(position).getClassName());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(s1.toUpperCase(),color,20);
            ((ImageView) ItemView.findViewById(R.id.selector_image)).setImageDrawable(drawable);
            return ItemView;
        }
    }
}
