package com.example.employeedirectoryapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeedirectoryapplication.R;
import com.example.employeedirectoryapplication.adapter.EmployeeAdapter;
import com.example.employeedirectoryapplication.callback.EmployeeListCallback;
import com.example.employeedirectoryapplication.database.EmployeeTable;
import com.example.employeedirectoryapplication.model.Employee;
import com.example.employeedirectoryapplication.network.ApiClient;
import com.example.employeedirectoryapplication.util.Constants;
import com.example.employeedirectoryapplication.util.Network;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Employee> syncList = new ArrayList<>();
    private EmployeeTable employeeTable;
    private EmployeeAdapter adapter;
    private RelativeLayout progressBar;
    private TextView txtEmpty;
    private boolean isDeleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
        employeeTable = new EmployeeTable(this);
        adapter = new EmployeeAdapter(this);
        txtEmpty = findViewById(R.id.txtEmpty);
        progressBar = findViewById(R.id.progressBar);

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();

        //Check RecyclerView is empty
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                txtEmpty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtEmpty.setVisibility(View.GONE);
        if(isDeleted){
            populateList();
        }
    }

    //Swipe left or right to delete item
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final Employee item = adapter.removeItem(position);
            Toast.makeText(MainActivity.this,"Item Deleted", Toast.LENGTH_LONG).show();
        }
    };

    /*
     * Every time activity start, Check table and sync data.
     * Otherwise load data from server.
     * */
    private void populateList() {
        syncList = employeeTable.getEmployeeList();
        if (syncList.size() > 0) {
            adapter.addList(syncList);
        } else {
            if (Network.isConnected(this)) {
                progressBar.setVisibility(View.VISIBLE);
                isDeleted = false;
                syncList();
            } else {
                Toast.makeText(MainActivity.this, Constants.ERROR_NETWORK,Toast.LENGTH_SHORT);
            }
        }
    }
    //Sync data from server.
    private void syncList() {
        ApiClient.enqueue(new EmployeeListCallback() {
            @Override
            public void onSuccess(List<Employee> list) {
                progressBar.setVisibility(View.GONE);
                if (list.size() > 0) {
                    //Clear list, table and load data into RecyclerView
                    syncList.clear();
                    employeeTable.clearTable();
                    employeeTable.insertList(list);
                    syncList = employeeTable.getEmployeeList();
                    adapter.addList(syncList);
                }
            }

            @Override
            public void onFailure(String status) {
                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT);
            }
        });
    }
}