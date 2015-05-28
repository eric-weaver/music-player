package com.weaver.eric.orion.adapters;

import android.support.v4.app.Fragment;

import com.weaver.eric.orion.view.SlidingTabLayout;

public class TabPagerItem 
{
	private final Fragment mFragment;
    private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;

    public TabPagerItem(Fragment fragment, CharSequence title, int indicatorColor, int dividerColor) 
    {
        mFragment = fragment;
    	mTitle = title;
        mIndicatorColor = indicatorColor;
        mDividerColor = dividerColor;
    }
    
    Fragment getFragment() 
    {
    	return mFragment;
    }
    
    /**
     * @return the title which represents this tab. In this sample this is used directly by
     * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
     */
    CharSequence getTitle() 
    {
        return mTitle;
    }

    /**
     * @return the color to be used for indicator on the {@link SlidingTabLayout}
     */
    int getIndicatorColor() 
    {
        return mIndicatorColor;
    }

    /**
     * @return the color to be used for right divider on the {@link SlidingTabLayout}
     */
    int getDividerColor() 
    {
        return mDividerColor;
    }
}
