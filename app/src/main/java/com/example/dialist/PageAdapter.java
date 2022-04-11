package com.example.dialist;

import android.content.Context;
import android.security.ConfirmationAlreadyPresentingException;
import android.security.identity.EphemeralPublicKeyNotFoundException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {

    public int mCount;
    public Page_2 pg2;
    public static Context context_padapter;

    public PageAdapter(FragmentActivity fragment, int count, int edit) {
        super(fragment);
        mCount = count + edit;
        setContext();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index%2==0) {
            return new Page_1(index+1);
        }
        else {
            pg2 = new Page_2(index+1);
            context_padapter = pg2.getContext();
            return pg2;
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public int getRealPosition(int position) { return position % mCount; }

    public void setContext() {

    }
}
