package flux.sample.arief.com.sampleflux.store;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.List;

import flux.sample.arief.com.sampleflux.action.Action;
import flux.sample.arief.com.sampleflux.action.TodoActions;
import flux.sample.arief.com.sampleflux.dispatcher.Dispatcher;
import flux.sample.arief.com.sampleflux.model.Todo;
import flux.sample.arief.com.sampleflux.repositori.LocalRepository;

/**
 * All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class StoreTodo extends Store {

    static StoreTodo instance;
    static LocalRepository localRepository;

    protected StoreTodo(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public void addLocalRepo(LocalRepository localRepository){
        this.localRepository = localRepository;
    }

    public static StoreTodo get(Dispatcher dispatcher){
        if(instance == null){
            instance = new StoreTodo(dispatcher);
        }
        return instance;
    }

    @Override
    StoreChangeEvent changeEvent() {
        return new TodoStoreChangeEvent();
    }

    @Override
    @Subscribe
    public void onAction(Action action) {
        Log.d("AF ", "store : "+action.getType()+" : "+action.getData());
        switch (action.getType()){
            case TodoActions.ADD_TODO:
                Todo todo = (Todo) action.getData().get(TodoActions.ADD_TODO);
                if(addTodo(todo)) emitStoreChange();
                break;

            case TodoActions.DELETE_TODO:
                int id = (int) action.getData().get(TodoActions.DELETE_TODO);
                deleteTodo(id);
                emitStoreChange();
                break;

            case TodoActions.UPDATE_TODO:
                Todo td = (Todo) action.getData().get(TodoActions.UPDATE_TODO);
                if(updateTodo(td)) emitStoreChange();

                break;
        }
    }


    boolean addTodo(Todo todo){
        return localRepository.addTodo(todo) > 0 ? true : false;
    }

    boolean updateTodo(Todo todo){
        boolean update = localRepository.updateTodo(todo) > 0 ? true : false;
        return update;
    }

    public List<Todo> getAllTodo(){
        return localRepository.getAllTodo();
    }

    boolean deleteTodo(int id){
        return localRepository.deleteTodo(id);
    }

    public class TodoStoreChangeEvent implements StoreChangeEvent {
    }
}
