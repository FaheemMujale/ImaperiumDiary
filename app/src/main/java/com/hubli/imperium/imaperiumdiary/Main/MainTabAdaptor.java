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

import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Chats.FragTabChat;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Notifications.FragTabNotifications;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.Posts.FragTabPosts;
import com.hubli.imperium.imaperiumdiary.Main.MainFrags.QA.FragTabQA;
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
    private final int RANK_TAB = 0;
    private final int HOME_TAB = 1;
    private final int QA_TAB = 2;
    private final int CHAT_TAB = 3;
    private final int NOTIFICATION_TAB = 4;
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
        adaptor.addFragment(new FragTabRanks(),"ONE");
        adaptor.addFragment(new FragTabPosts(),"TWO");
        adaptor.addFragment(new FragTabQA(),"THREE");
        adaptor.addFragment(new FragTabChat(), "FOUR");
        adaptor.addFragment(new FragTabNotifications(), "FIVE");
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
        tabLayout.getTabAt(RANK_TAB).setIcon(R.drawable.ic_rank);
        tabLayout.getTabAt(HOME_TAB).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(QA_TAB).setIcon(R.drawable.ic_question_answer);
        tabLayout.getTabAt(CHAT_TAB).setCustomView(b1);
        tabLayout.getTabAt(NOTIFICATION_TAB).setCustomView(b2);
    }
}
