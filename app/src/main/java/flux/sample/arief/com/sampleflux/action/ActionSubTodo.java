package flux.sample.arief.com.sampleflux.action;

import flux.sample.arief.com.sampleflux.dispatcher.Dispatcher;
import flux.sample.arief.com.sampleflux.model.SubTodo;

/**
 * Copyright (C) PT. Sebangsa Bersama - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class ActionSubTodo {

    private static ActionSubTodo instance;
    final Dispatcher dispatcher;

    public ActionSubTodo(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static ActionSubTodo get(Dispatcher dispatcher){
        if(instance == null){
            instance = new ActionSubTodo(dispatcher);
        }
        return instance;

    }

    public void create(SubTodo subTodo){
        dispatcher.dispatch(TodoActions.ADD_SUB_TODO, subTodo);
    }

    public void delete(int id){
        dispatcher.dispatch(TodoActions.DELETE_SUB_TODO, id);
    }

    public void update(SubTodo subTodo){
        dispatcher.dispatch(TodoActions.UPDATE_SUB_TODO, subTodo);
    }
}
