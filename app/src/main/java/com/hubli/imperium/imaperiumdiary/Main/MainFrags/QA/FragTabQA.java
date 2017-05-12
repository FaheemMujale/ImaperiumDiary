package com.hubli.imperium.imaperiumdiary.Main.MainFrags.QA;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabQA extends Fragment implements MyAdaptor.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{


    public FragTabQA() {
        // Required empty public constructor
    }

    private View view;
    private SwipeRefreshLayout swipeRefresh;
    private MyAdaptor adaptor;
    private SPData spData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_tab_q, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adaptor = new MyAdaptor(this,getActivity());
        adaptor.setLinearLayoutManager(linearLayoutManager);
        adaptor.setRecyclerView(recyclerView);
        recyclerView.setAdapter(adaptor);
        recyclerView.getItemAnimator().setChangeDuration(0);
        spData = new SPData(getActivity().getApplicationContext());
        swipeRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onAnswerClick(QaAnimData qaAnimData) {

    }

    @Override
    public void onRefresh() {

    }
}
