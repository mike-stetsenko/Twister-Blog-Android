package com.mairos.twisterblog;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.mairos.twisterblog.model.Post;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Mike on 31.01.2015.
 */
public class AdapterDataMatcher {
    public static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    Log.d("unit_tests", "TEST adapter |" + ((Post) adapter.getItem(i)).title + "|");
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
