package com.mairos.twisterblog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mike on 12.01.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    public int order();
}