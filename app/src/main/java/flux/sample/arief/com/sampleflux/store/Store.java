package flux.sample.arief.com.sampleflux.store;


import android.util.Log;

import flux.sample.arief.com.sampleflux.action.Action;
import flux.sample.arief.com.sampleflux.dispatcher.Dispatcher;

public abstract class Store {

    final Dispatcher dispatcher;

    protected Store(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void emitStoreChange() {
        Log.d("AF ", "update change ");
        dispatcher.emitChange(changeEvent());
    }

    abstract StoreChangeEvent changeEvent();
    public abstract void onAction(Action action);

    public interface StoreChangeEvent {}
}
