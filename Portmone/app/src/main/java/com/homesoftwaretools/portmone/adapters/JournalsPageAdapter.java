package com.homesoftwaretools.portmone.adapters;/*
 * Created by Wild on 06.05.2015.
 */



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.homesoftwaretools.portmone.fragments.BallanceReportFragment;
import com.homesoftwaretools.portmone.fragments.ExpenseJournalFragment;
import com.homesoftwaretools.portmone.fragments.IncomeJournalFragment;
import com.homesoftwaretools.portmone.fragments.TransferJournalFragment;

import java.lang.ref.WeakReference;

public class JournalsPageAdapter extends FragmentPagerAdapter {

    public JournalsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    private static final String[] names = {"Приходы", "Расходы", "Переводы", "Отчеты"};

    @Override
    public Fragment getItem(int i) {
        Fragment f;
        switch(i) {
            case 0:
                f = new IncomeJournalFragment();
                break;
            case 1:
                f = new ExpenseJournalFragment();
                break;
            case 2:
                f = new TransferJournalFragment();
                break;
            case 3:
                f = new BallanceReportFragment();
                break;
            default: f = null;
        }
        return f;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }


}
