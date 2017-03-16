package flux.sample.arief.com.sampleflux.action;

import android.util.Log;

import flux.sample.arief.com.sampleflux.dispatcher.Dispatcher;
import flux.sample.arief.com.sampleflux.model.Todo;

/**
 * Copyright (C) PT. Sebangsa Bersama - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class ActionTodo {

    private static ActionTodo instance;
    final Dispatcher dispatcher;

    public ActionTodo(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static ActionTodo get(Dispatcher dispatcher){
        if(instance == null){
            instance = new ActionTodo(dispatcher);
        }
        return instance;
    }

    public void create(Todo todo){
        Log.d("AF", "action "+todo.name);
        dispatcher.dispatch(TodoActions.ADD_TODO, TodoActions.ADD_TODO, todo);
    }

    public void update(Todo todo){
        dispatcher.dispatch(TodoActions.UPDATE_TODO, todo);
    }

    public void delete(int id){
        dispatcher.dispatch(TodoActions.DELETE_TODO, TodoActions.DELETE_TODO, id);
    }

    public void edit(Todo todo) {
        dispatcher.dispatch(TodoActions.UPDATE_TODO, TodoActions.UPDATE_TODO, todo);
    }
}
