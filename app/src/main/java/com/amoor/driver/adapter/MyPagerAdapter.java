package com.amoor.driver.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.amoor.driver.ui.fragment.homeCycle.BusFragment;
import com.amoor.driver.ui.fragment.homeCycle.HomeFragment;
import com.amoor.driver.ui.fragment.homeCycle.MapFragment;
import com.amoor.driver.ui.fragment.homeCycle.NotesFragment;

public class MyPagerAdapter extends FragmentPagerAdapter
{
    Fragment [] frags ={new MapFragment(),new BusFragment(),new NotesFragment(),new HomeFragment()};
    String [] titles = {"الخريطة","الاتوبيس","الملاحظات","الاخبار"};
    public MyPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        Log.i("adapter", "getItem: tab "+i);
        return frags[i];
    }

    @Override
    public int getCount()
    {
        return frags.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return titles[position];
    }
}
