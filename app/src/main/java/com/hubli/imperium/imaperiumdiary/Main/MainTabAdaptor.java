package com.hubli.imperium.imaperiumdiary.Main;


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

    public MainTabAdaptor() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_main_tab_adaptor, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adaptor = new ViewPagerAdapter(getChildFragmentManager());
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

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
}
