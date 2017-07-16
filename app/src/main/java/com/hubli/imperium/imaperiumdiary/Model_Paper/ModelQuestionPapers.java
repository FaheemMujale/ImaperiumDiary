package com.hubli.imperium.imaperiumdiary.Model_Paper;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ModelQuestionPapers extends Fragment implements IVolleyResponse {

    private FloatingActionButton button;
    private ListView list_model;
    private View rootView;
    private SPData userdatasp;
    private MyAdaptor adaptor;
    private ProgressBar progressBar;
    private TextView noInternet;
    private TextView notAvailable;
    private MyVolley myVolley;
    private String classNumber;
    public static final String INTENTFILTER_M ="intent_filter_m";
    public static final String PAPER_NAME = "paper_name";
    public static final String PAPER_LINK = "paper_link";
    public static final String USER_UPLOAD = "user_upload";
    private ImageView delete_button;
    List<Model_paper> items = new ArrayList<>();


    public ModelQuestionPapers() {}

    public ModelQuestionPapers(String classNumber) {
        this.classNumber = classNumber;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_pdf, container, false);
        userdatasp=new SPData();
        progressBar = (ProgressBar) rootView.findViewById(R.id.pdf_progress);
        notAvailable = (TextView) rootView.findViewById(R.id.not_available);
        noInternet = (TextView)rootView.findViewById(R.id.noInternet);
        getActivity().setTitle("Model Question Paper");
        list_model = (ListView) rootView.findViewById(R.id.list);
        adaptor = new MyAdaptor();
        list_model.setAdapter(adaptor);
        myVolley = new MyVolley(getActivity().getApplicationContext(),this);
        checkInternetConnection();
        button = (FloatingActionButton) rootView.findViewById(R.id.add);
        if(ServerConnect.checkInternetConenction(getActivity())) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ServerConnect.checkInternetConenction(getActivity())) {
                        android.app.FragmentManager manager = getActivity().getFragmentManager();
                        Dialog_model dialog_model = new Dialog_model();
                        dialog_model.setClassNumber(classNumber);
                        dialog_model.show(manager, "Dialog_model");
                    }
                }
            });
        }

        return rootView;
    }
    public void checkInternetConnection()
    {
        if(ServerConnect.checkInternetConenction(getActivity()))
        {
            getDataFromServer();
        }
        else
        {
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(receiver, new IntentFilter(INTENTFILTER_M));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            items.add(0, new Model_paper(intent.getStringExtra(PAPER_NAME),
                    intent.getStringExtra(PAPER_LINK),
                    intent.getStringExtra(USER_UPLOAD)));
            adaptor.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDataFromServer(){
        if(userdatasp.isStudent()){
            classNumber = userdatasp.getUserData(SPData.CLASS);
        }
          progressBar.setVisibility(View.VISIBLE);
          myVolley.setUrl(URL.MODEL_PAPER);
          myVolley.setParams(SPData.CLASS,classNumber);
          myVolley.connect();
    }
    @Override
    public void volleyResponse(String result) {
        try {
            notAvailable.setVisibility(View.GONE);
            getJsonData(result);
        } catch (JSONException e) {
            e.printStackTrace();
            notAvailable.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void volleyError() {

    }

    private void getJsonData(String re) throws JSONException {
        JSONArray json = new JSONArray(re);
        for (int i = json.length() - 1; i >= 0; i--) {
            JSONObject jsonobj = json.getJSONObject(i);
            items.add(new Model_paper(jsonobj.getString("paper_name"), jsonobj.getString("paper_link"), jsonobj.getString(SPData.USER_NUMBER)));
        }
        adaptor.notifyDataSetChanged();

        list_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link=URL.SERVER_URL + items.get(position).getLink();
                String pdfLink = link.replace(" ","%20");
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(pdfLink),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    getActivity().startActivity(intent);
                }catch (Exception e){
                    intent = new Intent(Intent.ACTION_VIEW,Uri.parse(URL.GOOGLE_DRIVE_VIEWER + pdfLink));
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    class MyAdaptor extends ArrayAdapter<Model_paper> {
        public MyAdaptor() {super(getActivity().getApplicationContext(), R.layout.pdf_names, items);}
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getActivity().getLayoutInflater().inflate(R.layout.pdf_names, parent, false);
            }

            final Model_paper currentItem = items.get(position);
            delete_button = (ImageView) ItemView.findViewById(R.id.paper_delete);
            TextView name = (TextView) ItemView.findViewById(R.id.pdf_name);
            name.setText(currentItem.getName());
            ImageView imageName = (ImageView) ItemView.findViewById(R.id.image_text);

            String firstletter = String.valueOf(currentItem.getName().charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRound(firstletter.toUpperCase(),color);
            imageName.setImageDrawable(drawable);

            if(currentItem.getUserUpload()== Integer.parseInt(userdatasp.getUserData(SPData.USER_NUMBER)))
            {
                delete_button.setVisibility(View.VISIBLE);
                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());
                        alert.setTitle("Alert");
                        alert.setMessage("Are you sure to delete record");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myVolley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                                    @Override
                                    public void volleyResponse(String result) {
                                        if(result.contains("DONE"))
                                        {
                                            items.remove(position);
                                            adaptor.notifyDataSetChanged();
                                            Toast.makeText(getActivity().getApplicationContext(), "Deleted",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void volleyError() {

                                    }
                                });
                                myVolley.setUrl(URL.MODELPAPER_DELETE);
                                myVolley.setParams(SPData.USER_NUMBER, userdatasp.getUserData(SPData.USER_NUMBER) );
                                myVolley.setParams("paper_link",currentItem.getLink() );
                                myVolley.connect();
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                });
            }
            else{
                delete_button.setVisibility(View.GONE);
            }
            return ItemView;
        }
    }
}




