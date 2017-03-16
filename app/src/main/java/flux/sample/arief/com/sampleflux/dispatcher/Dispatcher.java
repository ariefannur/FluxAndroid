package flux.sample.arief.com.sampleflux.dispatcher;

import android.util.Log;

import com.squareup.otto.Bus;

import flux.sample.arief.com.sampleflux.action.Action;
import flux.sample.arief.com.sampleflux.store.Store;

/**
 * Copyright (C) PT. Sebangsa Bersama - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class Dispatcher {

    private final Bus bus;
    private static Dispatcher instance;

    public static Dispatcher get(Bus bus) {
        if (instance == null) {
            instance = new Dispatcher(bus);
        }
        return instance;
    }

    Dispatcher(Bus bus) {
        this.bus = bus;
    }

    public void register(final Object cls) {
        bus.register(cls);

    }

    public void unregister(final Object cls) {
        bus.unregister(cls);
    }

    public void emitChange(Store.StoreChangeEvent o) {
        post(o);
    }

    public void dispatch(String type, Object... data) {
        if (isEmpty(type)) {
            throw new IllegalArgumentException("Type must not be empty");
        }
        Log.d("AF ", "map data "+data.length);

        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
        }

        Log.d("AF ", "map data "+data.length);
        Action.Builder actionBuilder = Action.type(type);
        int i = 0;
        while (i < data.length) {
            String key = (String) data[i++];
            Object value = data[i++];
            Log.d("AF ", "key "+key);
            Log.d("AF ", "value "+value);
            actionBuilder.bundle(key, value);
        }
        post(actionBuilder.build());
    }

    private boolean isEmpty(String type) {
        return type == null || type.isEmpty();
    }

    private void post(final Object event) {
        Log.d("AF ", "object "+(event == null ? "null" : event.toString()));
        if(event != null)
        bus.post(event);
    }
}
