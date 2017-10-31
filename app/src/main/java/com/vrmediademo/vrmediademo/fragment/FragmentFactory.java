package com.vrmediademo.vrmediademo.fragment;

import android.app.Fragment;

/**
 * Created by djf on 2017/5/4.
 */

public class FragmentFactory {

    public Fragment creatFragment(int type) {
        switch (type) {
            case 0:
                return new CompareFragment();
            case 1:
                return new LiuFragment();
            case 2:
                return new OriFragment();
            case 6:
                return new ExoPlayerFragment();
        }
        return null;
    }
}
