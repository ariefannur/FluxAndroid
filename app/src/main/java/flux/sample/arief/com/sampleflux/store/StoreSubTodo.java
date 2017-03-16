package flux.sample.arief.com.sampleflux.store;

import flux.sample.arief.com.sampleflux.action.Action;
import flux.sample.arief.com.sampleflux.dispatcher.Dispatcher;

/**
 * Copyright (C) PT. Sebangsa Bersama - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class StoreSubTodo extends Store {


    protected StoreSubTodo(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    StoreChangeEvent changeEvent() {
        return null;
    }

    @Override
    public void onAction(Action action) {

    }
}
