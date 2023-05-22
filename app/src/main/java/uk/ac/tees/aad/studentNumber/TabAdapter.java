package uk.ac.tees.aad.studentNumber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 2;

    public TabAdapter(FragmentManager fragmentManager, int behavior) {
        super(fragmentManager, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HotelListFragment();
            case 1:
                return new UserProfileFragment();
            default:
                throw new IllegalArgumentException("Invalid tab position");
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Hotels";
            case 1:
                return "Profile";
            default:
                return super.getPageTitle(position);
        }
    }
}
