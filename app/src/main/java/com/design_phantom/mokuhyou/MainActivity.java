package com.design_phantom.mokuhyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.design_phantom.mokuhyou.Common.Common;

public class MainActivity extends AppCompatActivity {

    private Button btMoveList;
    private Button btAddGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btMoveList = findViewById(R.id.bt_move_list);
        btAddGoal = findViewById(R.id.bt_add_goal);

        //set listener
        setListener();
    }

    private void setListener(){

        btMoveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to list
                Intent intent = new Intent(MainActivity.this, MainGoalListActivity.class);
                startActivity(intent);
            }
        });


        btAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open dialog
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        menu.add("menu1").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Common.log("onMenuItemClick");
                return true;
            }
        });

        return true;
    }
}
