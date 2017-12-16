package me.sstefani.todo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import me.sstefani.todo.model.Checklist;
import me.sstefani.todo.model.Task;

public class ChecklistActivity extends AppCompatActivity {

    ListView checklistView;
    List<Task> tasks = new ArrayList<>();
    ArrayAdapter adapter;

    private String newTaskName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Checklist checklist = (Checklist) getIntent().getSerializableExtra("CHECKLIST");

        TextView title = findViewById(R.id.checklist_title);
        title.setText(checklist.getName());

        checklistView.setLongClickable(true);
        checklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                taskLongSelected(pos, checklist.getId());
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChecklistActivity.this);
                builder.setTitle("Create New Task");

                // Set up the input
                final EditText input = new EditText(ChecklistActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newTaskName = input.getText().toString();
                        createTask(newTaskName, checklist.getId());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void createTask(String title, Long checklistId) {
        Task task = new Task(title);

        GsonRequest<Task> jsonRequest = new GsonRequest(
                Request.Method.POST, "/api/checklists/" + checklistId + "/tasks", task, Task.class,
                new Response.Listener<Task>() {
                    @Override
                    public void onResponse(Task task) {
                        tasks.add(task);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work! " + error.getStackTrace());
            }
        });

        VolleyController.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void deleteTask(Long taskId, Long checklistId) {
        GsonRequest<Task> jsonRequest = new GsonRequest(
                Request.Method.DELETE, "/api/checklists/" + checklistId + "/tasks/" + taskId, Task.class,
                new Response.Listener<Task>() {
                    @Override
                    public void onResponse(Task task) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work! " + error.getStackTrace());
            }
        });

        VolleyController.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void taskLongSelected(final int position, final long checklistId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChecklistActivity.this);
        alertDialogBuilder.setTitle("Delete Task");

        final Task task = tasks.get(position);

        alertDialogBuilder
                .setMessage("Do you really want to delete '" + task.getTitle() + "'?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTask(task.getId(), checklistId);
                        tasks.remove(task);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
