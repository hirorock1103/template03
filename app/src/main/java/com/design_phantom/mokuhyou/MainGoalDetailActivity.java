package com.design_phantom.mokuhyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.media.Image;
import android.os.Build;
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
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.Master.MeasureHistory;

import java.nio.ByteBuffer;
import java.util.Date;

public class MainGoalDetailActivity extends AppCompatActivity {

    private int goalId;
    private GoalManager goalManager;

    //view
    private TextView title;
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
        leftScroll = findViewById(R.id.left_scroll);
        rightScroll = findViewById(R.id.right_scroll);
        leftScrollContents = findViewById(R.id.left_scroll_contents);
        rightScrollContents = findViewById(R.id.right_scroll_contents);
        icNext = findViewById(R.id.ic_next);
        icBefore = findViewById(R.id.ic_before);
        createBasic = findViewById(R.id.create_basic);
        createNewRecord = findViewById(R.id.create_new_record);
        
        //test data
        GoalMeasure measure1 = new GoalMeasure();
        measure1.setParentGoalId(goalId);
        measure1.setMeasureTitle("腹筋３０回");
        measure1.setMeasureDate(Common.formatDate(new Date(), Common.DATE_FORMAT_SAMPLE_1));
        measure1.setMeasureType("int");
        measure1.setMeasureImageValue(null);
        measure1.setMeasureIntValue(3);

        GoalMeasure measure2 = new GoalMeasure();
        measure2.setParentGoalId(goalId);
        measure2.setMeasureTitle("腹筋画像");
        measure2.setMeasureDate(Common.formatDate(new Date(), Common.DATE_FORMAT_SAMPLE_1));
        measure2.setMeasureType("image");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fukkinsample);
        byte[] imageByte = Common.convertBitMapToByteArrray(bitmap);
        measure2.setMeasureImageValue(imageByte);
        measure2.setMeasureIntValue(0);

        GoalMeasure measure3 = new GoalMeasure();
        measure3.setParentGoalId(goalId);
        measure3.setMeasureTitle("腕立て");
        measure3.setMeasureDate(Common.formatDate(new Date(), Common.DATE_FORMAT_SAMPLE_1));
        measure3.setMeasureType("int");
        measure3.setMeasureImageValue(null);
        measure3.setMeasureIntValue(5);

        GoalMeasure[] measures = new GoalMeasure[5];
        measures[0] = measure1;
        measures[1] = measure2;
        measures[2] = measure3;
        measures[3] = measure3;
        measures[4] = measure3;


        for(int i = 0; i < measures.length; i++){
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_cmp01, null);
            TextView title = view.findViewById(R.id.title);
            title.setText(measures[i].getMeasureTitle());

            ImageView imageval = view.findViewById(R.id.image_value);
            TextView intval = view.findViewById(R.id.int_value);

            if(measures[i].getMeasureType() != null && measures[i].getMeasureType().equals("image")){
                //imageval.setImageBitmap(BitmapFactory.decodeByteArray(measures[i].getMeasureImageValue(), 0, measures[i].getMeasureImageValue().length));
                imageval.setImageBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.fukkinsample));
            }else if(measures[i].getMeasureType().equals("int")){
                intval.setText(String.valueOf(measures[i].getMeasureIntValue()));
            }

            leftScrollContents.addView(view);

            //set right scroll view
            //MeasureHistory history = goalManager.getHistoryByMeasureId(measures[i].getMeasureId());
            MeasureHistory history = null;
            if(i == 0 || i == 1  || i == 2){
                history = new MeasureHistory();
            }
            View view2;
            if(history != null){
                view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_cmp02, null);
                TextView setValue  = view2.findViewById(R.id.int_value);
                ImageView imageValue  = view2.findViewById(R.id.image_value);
                ImageView trend = view2.findViewById(R.id.trend);
                if(i == 1){
                    imageValue.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fukkinsample));
                }
                if(i == 0 || i == 2 ){
                    int value = 12;
                    if(i == 2){
                        value = 3;
                    }
                    int resorceId = R.mipmap.ic_trendflat;
                    if(measures[i].getMeasureIntValue() < value){
                        resorceId = R.mipmap.ic_trendup;
                    }else if(measures[i].getMeasureIntValue() > value){
                        resorceId = R.mipmap.ic_trenddown;
                    }

                    trend.setImageBitmap(BitmapFactory.decodeResource(getResources(), resorceId));
                    setValue.setText(String.valueOf(value));
                }


            }else{
                view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_comp3, null);
            }

            rightScrollContents.addView(view2);
        }


        leftScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = leftScroll.getScrollY(); //for verticalScrollView
                Common.log("scrollY" + scrollY);
                rightScroll.smoothScrollTo(0,scrollY);
            }
        });

        /*
        rightScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = rightScroll.getScrollY();
                leftScroll.smoothScrollTo(0, scrollY);
            }
        });
        */


        title.setText(goal.getGoalTitle());

        //lisitenr
        icBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "before", Snackbar.LENGTH_SHORT).show();
            }
        });
        icNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "next", Snackbar.LENGTH_SHORT).show();
            }
        });
        createBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "createBasic", Snackbar.LENGTH_SHORT).show();
            }
        });
        createNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "createNewRecord", Snackbar.LENGTH_SHORT).show();
            }
        });


    }
}
