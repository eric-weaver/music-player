package com.weaver.eric.orion.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weaver.eric.orion.view.SlidingTabLayout;

public class TabsPagerAdapter extends FragmentPagerAdapter 
{	 	
	private List<TabPagerItem> mTabs = new ArrayList<TabPagerItem>();
	
    public TabsPagerAdapter(FragmentManager fm) 
    {
        super(fm);
    }
    
    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {@link SlidingTabLayout}.
     * <p>
     * Here we construct one using the position value, but for real application the title should
     * refer to the item's contents.
     */
    @Override
    public CharSequence getPageTitle(int index) 
    {
    	return mTabs.get(index).getTitle();
    }
 
    @Override
    public Fragment getItem(int index) 
    {
    	return mTabs.get(index).getFragment();
    }
 
    @Override
    public int getCount() 
    {
        return mTabs.size();
    }
    
    public void addTab(TabPagerItem tab)
    {
    	mTabs.add(tab);
    }
}
