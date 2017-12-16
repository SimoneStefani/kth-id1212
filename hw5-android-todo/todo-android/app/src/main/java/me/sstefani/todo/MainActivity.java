package me.sstefani.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.sstefani.todo.model.Checklist;
import me.sstefani.todo.model.User;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<Checklist> lists = new ArrayList<>();
    ArrayAdapter adapter;

    private String newChecklistName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Create New Todo List");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newChecklistName = input.getText().toString();
                        createChecklist(newChecklistName);
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

        fetchUser();
        fetchChecklists();

        listView = findViewById(R.id.checklists);

        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, lists);
        listView.setAdapter(adapter);

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                listLongSelected(pos);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
                intent.putExtra("CHECKLIST", lists.get(pos));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            DataHolder.getInstance().setCurrentUser(null);
            DataHolder.getInstance().setJwt(null);

            Intent intent = new Intent(MainActivity.this, LandingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchChecklists() {
        GsonRequest<Checklist[]> jsonRequest = new GsonRequest(
                Request.Method.GET, "/api/checklists", Checklist[].class,
                new Response.Listener<Checklist[]>() {
                    @Override
                    public void onResponse(Checklist[] checklists) {
                        Collections.addAll(lists, checklists);
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

    private void fetchUser() {
        GsonRequest<User> jsonRequest = new GsonRequest(
                Request.Method.GET, "/api/user", User.class,
                new Response.Listener<User>() {
                    @Override
                    public void onResponse(User user) {
                        DataHolder.getInstance().setCurrentUser(user);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work! " + error.getStackTrace());
            }
        });

        VolleyController.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void createChecklist(String title) {
        Checklist checklist = new Checklist(title);
        Long usrId = DataHolder.getHolder().getCurrentUser().getId();
        GsonRequest<Checklist> jsonRequest = new GsonRequest(
                Request.Method.POST, "/api/checklists/user/" + usrId, checklist, Checklist.class,
                new Response.Listener<Checklist>() {
                    @Override
                    public void onResponse(Checklist checklist) {
                        lists.add(checklist);
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

    private void deleteChecklist(Long id) {
        GsonRequest<Checklist> jsonRequest = new GsonRequest(
                Request.Method.DELETE, "/api/checklists/" + id, Checklist.class,
                new Response.Listener<Checklist>() {
                    @Override
                    public void onResponse(Checklist checklist) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work! " + error.getStackTrace());
            }
        });

        VolleyController.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void listLongSelected(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Delete Todo List");

        final Checklist checklist = lists.get(position);

        alertDialogBuilder
                .setMessage("Do you really want to delete '" + checklist.getName() + "'?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteChecklist(checklist.getId());
                        lists.remove(checklist);
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
