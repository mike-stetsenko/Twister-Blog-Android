package com.mairos.twisterblog;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import com.mairos.twisterblog.gui.fragments.PostContentFragment;
import com.mairos.twisterblog.gui.fragments.PostsListFragment;
import com.mairos.twisterblog.model.Post;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements PostsListFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.content, PostsListFragment.newInstance());
            ft.commit();
        }
    }

    @Override
    public void onPostSelect(Post post) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, PostContentFragment.newInstance(post));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
                return false;
            } else {
                getFragmentManager().popBackStack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}