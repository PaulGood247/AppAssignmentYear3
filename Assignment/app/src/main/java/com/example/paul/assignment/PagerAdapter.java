package com.example.paul.assignment;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter { //FragmentStatePagerAdapter was refernced on the internet
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) { //constructor
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) { //gets current position

        switch (position) {
            case 0:
                MondayFragment tab1 = new MondayFragment();
                return tab1;
            case 1:
                TuesdayFragment tab2 = new TuesdayFragment();
                return tab2;
            case 2:
                WednesdayFragment tab3 = new WednesdayFragment();
                return tab3;
            case 3:
                ThursdayFragment tab4 = new ThursdayFragment();
                return tab4;
            case 4:
                FridayFragment tab5 = new FridayFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
