package com.design_phantom.mokuhyou;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Dialog.DialogAddGoal;
import com.design_phantom.mokuhyou.Dialog.DialogDeleteConfirm;
import com.design_phantom.mokuhyou.Master.Goal;

import java.util.List;

public class MainGoalListActivity extends AppCompatActivity
    implements DialogAddGoal.DialogAddGoalListener,DialogDeleteConfirm.DialogDeleteNoticeListener
{

    private GoalAdapter adapter;
    private List<Goal> list;
    private RecyclerView recyclerView;
    private GoalManager goalManager;

    //view
    private View emptyPageView;
    //fb
    private FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_goal_list);

        //
        ActionBar actionBar = getSupportActionBar();

        goalManager = new GoalManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        fb = findViewById(R.id.fb);

        //setListener
        setListener();

        list = goalManager.getList();
        adapter = new GoalAdapter(list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        if(list.size() == 0){
            setEmptyPage();
        }

    }

    private void setEmptyPage(){
        emptyPageView = LayoutInflater.from(this).inflate(R.layout.empty_page, null);
        ConstraintLayout layout = findViewById(R.id.layout);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        emptyPageView.setLayoutParams(params);
        layout.addView(emptyPageView);
    }

    //set listener
    public void setListener(){
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog
                DialogAddGoal addGoal = new DialogAddGoal();
                addGoal.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void DialogAddGoalResultNotice() {

        //dialog result
        insertView();

        ConstraintLayout layout = findViewById(R.id.layout);
        layout.removeView(emptyPageView);

        //Snackbar
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, getString(R.string.success_msg1), Snackbar.LENGTH_SHORT).show();

    }

    private void insertView(){

        list = goalManager.getList();

        adapter.setList(list);
        adapter.notifyItemInserted(0);


    }
    private void reloadView(){

        list = goalManager.getList();

        if(list.size() == 0){
            ConstraintLayout layout = findViewById(R.id.layout);
            layout.addView(emptyPageView);
        }

        adapter.setList(list);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void deleteResultNotice(int order) {
        //delete
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, getString(R.string.success_msg2), Snackbar.LENGTH_SHORT).show();
        reloadView();
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView delete;
        private ConstraintLayout layout;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            delete = itemView.findViewById(R.id.delete);
            layout = itemView.findViewById(R.id.layout_row);
        }
    }

    public class GoalAdapter extends RecyclerView.Adapter<GoalViewHolder>{

        private List<Goal> list;

        public GoalAdapter(List<Goal> list){
            this.list = list;
        }

        public void setList(List<Goal> list){
            this.list = list;
        }

        @NonNull
        @Override
        public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(MainGoalListActivity.this).inflate(R.layout.item_row_close, viewGroup, false);
            return new GoalViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull GoalViewHolder holder, int i) {
            Goal goal = list.get(i);
            holder.title.setText(goal.getGoalTitle());
            final int goalId = goal.getGoalId();
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open dialog
                    DialogDeleteConfirm deleteConfirm = new DialogDeleteConfirm();
                    Bundle bundle = new Bundle();
                    bundle.putString("dataType", "goal");
                    bundle.putInt("id", goalId);
                    deleteConfirm.setArguments(bundle);
                    deleteConfirm.show(getSupportFragmentManager(), "dialog");
                }
            });
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //移動
                    Intent intent = new Intent(MainGoalListActivity.this, MainGoalDetailActivity.class);
                    intent.putExtra("goalId", goalId);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
