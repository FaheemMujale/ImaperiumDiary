package com.hubli.imperium.imaperiumdiary.Main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds.FeedDialog;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds.FragTabFeeds;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Notifications.FragTabNotifications;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions.FragQuestions;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Ranks.FragTabRanks;
import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainTabAdaptor extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View view;
    private int currentTab;
    private final int QA_TAB = 0;
    private final int FEED_TAB = 1;
    private final int RANK_TAB = 2;
    private final int NOTIFICATION_TAB = 3;
    private View b1,b2;
    private ViewPagerAdapter adaptor;
    public static final String NOTIFICATION_BC_FILTER = "NOTIFICATION_BC_FILTER";
    private ViewPager tabScroll;
    private FloatingActionButton fabClick;

    public MainTabAdaptor() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_tab_adaptor, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        fab = (FloatingActionButton) view.findViewById(R.id.post_new_fab);
        fab.hide();
        adaptor = new ViewPagerAdapter(getChildFragmentManager());
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        getActivity().registerReceiver(receiver, new IntentFilter(NOTIFICATION_BC_FILTER));
        setTabScroll(viewPager);
        setFabClick(fab);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        adaptor.addFragment(new FragQuestions(),"1");
        adaptor.addFragment(new FragTabFeeds(),"2");
        adaptor.addFragment(new FragTabRanks(),"3");
        adaptor.addFragment(new FragTabNotifications(), "4");
        viewPager.setAdapter(adaptor);
        viewPager.setOffscreenPageLimit(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(viewPager.getCurrentItem());
    }

    public void setTabScroll(final ViewPager viewPager) {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = viewPager.getCurrentItem();
                setTitle(position);
                fab.hide();
                if(position == FEED_TAB && new SPData().getIdentification() == SPData.TEACHER){
                    fab.show();
                }
            }
        });
    }
    private void setTitle(int position){
        if(position == FEED_TAB){
            getActivity().setTitle("New Feeds");
        }else if(position == QA_TAB){
            getActivity().setTitle("Q & A");
        }
        else if(position == RANK_TAB){
            getActivity().setTitle("Weekly Ranking");
        }
        else if(position == NOTIFICATION_TAB){
            getActivity().setTitle("Notifications");
        }
    }

    public void setFabClick(FloatingActionButton fabClick) {
        fabClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                FeedDialog dialog = new FeedDialog();
                dialog.show(fragmentManager,"frag");
            }
        });
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTabs();
        }
    };
    private void updateTabs(){
        currentTab = viewPager.getCurrentItem();
        tabLayout.removeAllTabs();
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        //Badges are used to show notification count on tabs
        b1 = new Badges(getActivity().getApplicationContext()).getBadgeIcon(R.drawable.ic_chat);
        b2 = new Badges(getActivity().getApplicationContext()).getBadgeIcon(R.drawable.ic_notifications);
        tabLayout.getTabAt(QA_TAB).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(FEED_TAB).setIcon(R.drawable.ic_question_answer);
        tabLayout.getTabAt(RANK_TAB).setIcon(R.drawable.ic_rank);
        tabLayout.getTabAt(NOTIFICATION_TAB).setCustomView(b2);
    }
    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
