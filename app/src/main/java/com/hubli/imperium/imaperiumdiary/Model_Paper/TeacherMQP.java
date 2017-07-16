package com.hubli.imperium.imaperiumdiary.Model_Paper;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.Main.MainActivity;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherMQP extends Fragment {

    private SPData userDataSP;
    private ListView clist;
    private MyAdaptor adaptor;
    private ProgressBar progressBar;
    private TextView noInternet;
    private TextView notAvailable;
    public static boolean backpressed = false;
    public static List<String> classes = new ArrayList<>();

    public TeacherMQP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_teacher_mqp, container, false);
        getActivity().setTitle("Model Question Paper");
        userDataSP=new SPData();
        clist=(ListView)rootView.findViewById(R.id.list);
        progressBar = (ProgressBar)rootView.findViewById(R.id.teachermodel_progress);
        notAvailable = (TextView)rootView.findViewById(R.id.modelnotavailableT);
        noInternet = (TextView)rootView.findViewById(R.id.noInternetT);
        adaptor = new MyAdaptor();
        classes.clear();
        clist.setAdapter(adaptor);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkInternetConnection();
    }

    public  void checkInternetConnection()
    {
        if(ServerConnect.checkInternetConenction(getActivity()) && !backpressed)
        {
            progressBar.setVisibility(View.VISIBLE);
            getClassData();
        }
        else
        {
            String data = userDataSP.getClassesMQP();
            if(data.length()>0){
                try {
                    getJsonData(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                noInternet.setVisibility(View.VISIBLE);
            }
        }

    }
    public void getClassData(){
        MyVolley volley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                try {
                    progressBar.setVisibility(View.GONE);
                    noInternet.setVisibility(View.GONE);
                    userDataSP.storeClasses(result);
                    getJsonData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    notAvailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void volleyError() {

            }
        });
        volley.setUrl(URL.CLASSES_DIVISIONS_SUBJECTS).connect();
        volley.connect();
    }

    private void getJsonData(String re) throws JSONException {
        JSONArray json = new JSONArray(re);
        classes.clear();

        for (int i = 0; i <= json.length() - 1; i++) {
            JSONObject jsonobj = json.getJSONObject(i);
            classes.add(jsonobj.getString(SPData.CLASS));
        }

        adaptor.notifyDataSetChanged();
        clist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                backpressed = true;
                ModelQuestionPapers blankFragment = new ModelQuestionPapers(classes.get(position));
                FragmentTransaction frag = getActivity().getSupportFragmentManager().beginTransaction();
                frag.replace(R.id.main_con,blankFragment);
                MainActivity.setTag(MainActivity.BACK_STACK_TMQP);
                frag.addToBackStack(MainActivity.BACK_STACK_TMQP);
                frag.commit();
            }
        });


    }


    class MyAdaptor extends ArrayAdapter<String> {

        public MyAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.class_div,classes);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.class_div, parent, false);
            }

            String className=classes.get(position);
            ImageView img = (ImageView) itemView.findViewById(R.id.class_image);
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(className.toUpperCase(),color,20);
            img.setImageDrawable(drawable);
            return itemView;

        }
    }
}
