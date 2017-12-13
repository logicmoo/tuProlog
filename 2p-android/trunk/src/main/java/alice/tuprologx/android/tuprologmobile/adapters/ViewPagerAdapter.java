package alice.tuprologx.android.tuprologmobile.adapters;

/**
 * Created by Lucrezia on 05/07/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import alice.tuprologx.android.tuprologmobile.fragments.QueryFragment;
import alice.tuprologx.android.tuprologmobile.fragments.TheoryFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                QueryFragment tab1 = new QueryFragment();
                return tab1;
            case 1:
                TheoryFragment tab2 = new TheoryFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
