package flux.sample.arief.com.sampleflux.model;

import java.util.List;

/**
 *- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class Todo {
    public int id;
    public String name;
    public String date;
    public List<SubTodo> lsSubTodo;

    public Todo(){}
    public Todo(int id, String name, String date){
        this.id = id;
        this.name = name;
        this.date = date;
    }
}
