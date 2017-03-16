package flux.sample.arief.com.sampleflux.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import flux.sample.arief.com.sampleflux.MyApplication;
import flux.sample.arief.com.sampleflux.R;
import flux.sample.arief.com.sampleflux.action.ActionTodo;
import flux.sample.arief.com.sampleflux.dispatcher.Dispatcher;
import flux.sample.arief.com.sampleflux.model.Todo;
import flux.sample.arief.com.sampleflux.store.StoreTodo;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rvMain) RecyclerView rvMain;
    @BindView(R.id.swMain) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fabAdd) FloatingActionButton fabAdd;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Dispatcher dispatcher;
    private StoreTodo storeTodo;
    private ActionTodo actionTodo;
    private Adapter adapter;
    private Menu menu;
    private DialogAddTodo dialogAddTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle("To Do Apps");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        init dependency
        dispatcher = Dispatcher.get(new Bus());
        storeTodo = StoreTodo.get(dispatcher);
        storeTodo.addLocalRepo(((MyApplication) getApplication()).getLocalRepository());
        actionTodo = ActionTodo.get(dispatcher);

//        recycleview and adapter
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        adapter.update(storeTodo.getAllTodo());
        rvMain.setAdapter(adapter);

//        refresh layout
        swipeRefreshLayout.setOnRefreshListener(this);
        dialogAddTodo = new DialogAddTodo(MainActivity.this);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogAddTodo.show(false, null);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onRefresh() {
        update();
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 400);
    }


    class DialogAddTodo extends Dialog{
        @BindView(R.id.etDate)
        EditText etDate;

        @BindView(R.id.etName)
        EditText etname;

        @BindView(R.id.btnAdd)
        Button btnAdd;

        private int id;
        private boolean isEdit;

        public DialogAddTodo(Context context) {
            super(context);
            setContentView(R.layout.form_add_todo);
            ButterKnife.bind(this);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo todo = new Todo();
                    todo.date = etDate.getText().toString();
                    todo.name = etname.getText().toString();
                    todo.id = id;

                    if(!isEdit) {
                        actionTodo.create(todo);
                    }else{
                        actionTodo.edit(todo);
                    }
                    dismiss();
                }
            });

            etDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();

                    DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                            etDate.setText(d+" "+getMonth(m)+" "+y);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    dialog.show();
                }
            });
        }

       public void show(boolean isEdit, Todo todo){
           if(isEdit){
               this.id = todo.id;
               etname.setText(todo.name);
               etDate.setText(todo.date);
               this.isEdit = isEdit;
               btnAdd.setText("Update");
           }else{
               btnAdd.setText("Add");
               etname.setText("");
               etDate.setText("");
           }

           show();
       }
    }

    @Subscribe
    public void update(StoreTodo.TodoStoreChangeEvent event){
        update();
    }

    void update(){
        adapter.update(storeTodo.getAllTodo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatcher.register(this);
        dispatcher.register(storeTodo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dispatcher.unregister(this);
        dispatcher.unregister(storeTodo);
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder>{

        private List<Todo> lsTodos;

        public Adapter(){
            lsTodos = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final Todo todo = lsTodos.get(position);


            holder.txtTitle.setText(todo.name);
            holder.txtTgl.setText(todo.date);
            holder.txtCount.setText(todo.lsSubTodo != null ? todo.lsSubTodo.size() > 0 ? todo.lsSubTodo.size() +" note"  : "" : "");

            final PopupMenu popupMenu = new PopupMenu(MainActivity.this, holder.viewMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());
            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu.show();
                }
            });
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(item.getItemId() == R.id.delete){
                        actionTodo.delete(todo.id);
                    }else{

                        dialogAddTodo.show(true, todo);
                    }
                    return true;
                }
            });

        }

        public void update(List<Todo> lsTodos){
            this.lsTodos = lsTodos;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return lsTodos.size();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtTgl)
        TextView txtTgl;
        @BindView(R.id.txtCount)
        TextView txtCount;
        @BindView(R.id.viewMore)
        View viewMore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            viewMore.setBackgroundResource(android.R.drawable.ic_menu_edit);
        }
    }



    private String getMonth(int month){
        final String[] month_name = new String []{"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "November", "Desember"};
        return month_name[month];
    }
}
