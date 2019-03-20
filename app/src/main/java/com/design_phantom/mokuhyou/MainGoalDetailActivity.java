package com.design_phantom.mokuhyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.media.Image;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Dialog.DialogBasicMeasure;
import com.design_phantom.mokuhyou.Dialog.DialogHistory;
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.Master.MeasureHistory;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

public class MainGoalDetailActivity extends AppCompatActivity
implements DialogBasicMeasure.DialogBasicMeasureListener,
        DialogHistory.DialogHistoryListener
{

    private int goalId;
    private GoalManager goalManager;

    //view
    private TextView title;
    private TextView startDate;
    private TextView targetDate;
    private ScrollView leftScroll;
    private ScrollView rightScroll;
    private LinearLayout leftScrollContents;
    private LinearLayout rightScrollContents;

    //view button
    private ImageView icNext;
    private ImageView icBefore;
    private LinearLayout createBasic;
    private LinearLayout createNewRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_goal_detail);

        goalManager = new GoalManager(getApplicationContext());
        goalId = getIntent().getExtras().getInt("goalId");
        Goal goal = goalManager.getListById(goalId);

        //setview
        title = findViewById(R.id.title);
        startDate = findViewById(R.id.start_date);
        targetDate = findViewById(R.id.target_date);
        leftScroll = findViewById(R.id.left_scroll);
        rightScroll = findViewById(R.id.right_scroll);
        leftScrollContents = findViewById(R.id.left_scroll_contents);
        rightScrollContents = findViewById(R.id.right_scroll_contents);
        icNext = findViewById(R.id.ic_next);
        icBefore = findViewById(R.id.ic_before);
        createBasic = findViewById(R.id.create_basic);
        createNewRecord = findViewById(R.id.create_new_record);


        leftScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = leftScroll.getScrollY(); //for verticalScrollView
                Common.log("scrollY" + scrollY);
                rightScroll.smoothScrollTo(0,scrollY);
            }
        });


        //set target date
        targetDate.setText(Common.formatDate(new Date(),Common.DATE_FORMAT_SAMPLE_1));
        //set data
        setScrollViewData();
        //set toal
        setDate();


        //lisitenr
        icBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "before", Snackbar.LENGTH_SHORT).show();
                Date nextDate = Common.addDateFromTargetDate(targetDate.getText().toString(), Common.DATE_FORMAT_SAMPLE_1, "DAY", -1);
                //check if nextDate is bellow startdate
                Date start = Common.getDateByStr(startDate.getText().toString(), Common.DATE_FORMAT_SAMPLE_1);
                if(start.getTime() > nextDate.getTime()){
                    Snackbar.make(v, "--error nextDate is bellow startdate not allowed", Snackbar.LENGTH_SHORT).show();
                }else{
                    targetDate.setText(Common.formatDate(nextDate, Common.DATE_FORMAT_SAMPLE_1));
                    setScrollViewData();
                    Snackbar.make(v, "--change before date", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        icNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "next", Snackbar.LENGTH_SHORT).show();
                Date nextDate = Common.addDateFromTargetDate(targetDate.getText().toString(), Common.DATE_FORMAT_SAMPLE_1, "DAY", 1);
                targetDate.setText(Common.formatDate(nextDate, Common.DATE_FORMAT_SAMPLE_1));
                setScrollViewData();
                Snackbar.make(v, "--change next date", Snackbar.LENGTH_SHORT).show();
            }
        });
        createBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "createBasic", Snackbar.LENGTH_SHORT).show();
                DialogBasicMeasure basicMeasure = new DialogBasicMeasure();
                Bundle bundle = new Bundle();
                bundle.putInt("goalId",goalId);
                basicMeasure.setArguments(bundle);
                basicMeasure.show(getSupportFragmentManager(), "dialog");
            }
        });
        createNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "createNewRecord", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void setDate(){
        Goal goal = goalManager.getListById(goalId);
        title.setText(goal.getGoalTitle());
        startDate.setText(goal.getGoalMeasureDate() == null || goal.getGoalMeasureDate().isEmpty() ? "-" : goal.getGoalMeasureDate());
    }

    private void setScrollViewData(){

        leftScrollContents.removeAllViews();
        rightScrollContents.removeAllViews();

        List<GoalMeasure> list = goalManager.getMeasureByGoalId(goalId);
        final GoalMeasure[] measures = list.toArray(new GoalMeasure[list.size()]);

        for(int i = 0; i < measures.length; i++){
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_cmp01, null);
            TextView title = view.findViewById(R.id.title);
            title.setText(measures[i].getMeasureTitle());

            ImageView imageval = view.findViewById(R.id.image_value);
            TextView intval = view.findViewById(R.id.int_value);

            if(measures[i].getMeasureType() != null && measures[i].getMeasureType().equals("image")){
                //imageval.setImageBitmap(BitmapFactory.decodeByteArray(measures[i].getMeasureImageValue(), 0, measures[i].getMeasureImageValue().length));
                imageval.setImageBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.m1));
            }else if(measures[i].getMeasureType().equals("int")){
                intval.setText(String.valueOf(measures[i].getMeasureIntValue()));
            }
            leftScrollContents.addView(view);

            //set right scroll view
            List<MeasureHistory> historyList = goalManager.getMeasureHistoryByMeasureId(measures[i].getMeasureId(), targetDate.getText().toString());

            MeasureHistory history = null;
            if(historyList.size() == 0){
                history = null;
            }else{
                history = historyList.get(0);
            }

            View view2;
            if(history != null){
                view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_cmp02, null);
                TextView setValue  = view2.findViewById(R.id.int_value);
                ImageView imageValue  = view2.findViewById(R.id.image_value);
                ImageView trend = view2.findViewById(R.id.trend);

                //if history record exits....
                switch (measures[i].getMeasureType()){

                    case "int":
                        int resorceId = R.mipmap.ic_trendflat;
                        if(measures[i].getMeasureIntValue() < history.getMeasureIntValue()){
                            resorceId = R.mipmap.ic_trendup;
                        }else if(measures[i].getMeasureIntValue() > history.getMeasureIntValue()){
                            resorceId = R.mipmap.ic_trenddown;
                        }
                        trend.setImageBitmap(BitmapFactory.decodeResource(getResources(), resorceId));
                        setValue.setText(String.valueOf(history.getMeasureIntValue()));

                        break;

                    case "image":
                        imageValue.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.m1));
                        break;
                }


            }else{
                view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_comp3, null);
                ConstraintLayout layout = view2.findViewById(R.id.layout_row);
                final int measureId = measures[i].getMeasureId();
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //open dialog
                        DialogHistory dialogHistory = new DialogHistory();
                        Bundle bundle = new Bundle();
                        bundle.putInt("measureId", measureId);
                        bundle.putString("targetMeasureDate", targetDate.getText().toString());
                        dialogHistory.setArguments(bundle);
                        dialogHistory.show(getSupportFragmentManager(), "dialog");
                    }
                });
            }

            rightScrollContents.addView(view2);
        }


    }

    @Override
    public void DialogBasicMeasureResult(String error) {
        View view = findViewById(android.R.id.content);
        if(error.isEmpty()){
            Snackbar.make(view,getString(R.string.success_msg1),Snackbar.LENGTH_LONG ).show();
            setScrollViewData();
            setDate();
        }else{
            Snackbar.make(view,error,Snackbar.LENGTH_LONG ).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setScrollViewData();
        setDate();
    }

    @Override
    public void dialogHistoryNotice(String error) {
        View view = findViewById(android.R.id.content);
        if(error.isEmpty()){
            Snackbar.make(view,getString(R.string.success_msg1),Snackbar.LENGTH_LONG ).show();
            setScrollViewData();
            setDate();
        }else{
            Snackbar.make(view,error,Snackbar.LENGTH_LONG ).show();
        }
    }
}
