package com.edgehill.mad.mymedicare;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Convenience class used to return back fonts that aren't part of the local font library of the
 * device.
 */
public class TypeFaces{

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String name){
        synchronized(cache){
            if(!cache.containsKey(name)){
                Typeface t = Typeface.createFromAsset(
                        c.getAssets(),
                        String.format("fonts/%s.ttf", name)
                );
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }

}
