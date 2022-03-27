package com.example.dialist;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {

    public int mCount;

    public PageAdapter(FragmentActivity fragment, int count) {
        super(fragment);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index%2==0) return new Page_1(index+1);
        else return new Page_2(index+1);
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    public int getRealPosition(int position) { return position % mCount; }
}
