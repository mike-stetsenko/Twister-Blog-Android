package com.mairos.twisterblog;

import android.support.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

/**
 * Created by Mike on 31.01.2015.
 */
public class ItemContentMatcher {

    public static Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher){
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, Map>(Map.class) {
            @Override
            public boolean matchesSafely(Map map) {
                return hasEntry(equalTo("title"), itemTextMatcher).matches(map);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item content: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }
}
