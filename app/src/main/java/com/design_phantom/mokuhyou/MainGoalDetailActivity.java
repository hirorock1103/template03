package com.design_phantom.mokuhyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.Common.Media;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Dialog.DatePickDialog;
import com.design_phantom.mokuhyou.Dialog.DialogBasicMeasure;
import com.design_phantom.mokuhyou.Dialog.DialogDeleteConfirm;
import com.design_phantom.mokuhyou.Dialog.DialogHistory;
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.Master.MeasureHistory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainGoalDetailActivity extends AppCompatActivity
implements DialogBasicMeasure.DialogBasicMeasureListener,
        DialogHistory.DialogHistoryListener,
        DatePickDialog.DatePickResultListener,
        DialogDeleteConfirm.DialogDeleteNoticeListener
{

    //static value
    private final static int IMAGE_WIDTH = 900;//pic image limit width

    //basic information
    private int goalId;
    private GoalManager goalManager;

    //view
    private TextView title;
    private TextView startDate;
    private TextView targetDate;
    private TextView goalDateStr;
    private ScrollView leftScroll;
    private ScrollView rightScroll;
    private LinearLayout leftScrollContents;
    private LinearLayout rightScrollContents;

    //view button
    private ImageButton playmovie;
    private ImageView icNext;
    private ImageView icBefore;
    private ImageButton btShowGoal;//confirm_goal
    private ImageButton btShowToday;//today
    private LinearLayout createBasic;
    private LinearLayout createGoal;


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
        goalDateStr = findViewById(R.id.goal_date_str);
        leftScroll = findViewById(R.id.left_scroll);
        rightScroll = findViewById(R.id.right_scroll);
        leftScrollContents = findViewById(R.id.left_scroll_contents);
        rightScrollContents = findViewById(R.id.right_scroll_contents);
        icNext = findViewById(R.id.ic_next);
        icBefore = findViewById(R.id.ic_before);
        createBasic = findViewById(R.id.create_basic);
        createGoal = findViewById(R.id.create_new_record);
        playmovie = findViewById(R.id.playmovie);
        btShowGoal = findViewById(R.id.confirm_goal);
        btShowToday = findViewById(R.id.today);


        leftScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = leftScroll.getScrollY(); //for verticalScrollView
                //Common.log("scrollY" + scrollY);
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

                Date nextDate = Common.addDateFromTargetDate(targetDate.getText().toString(), Common.DATE_FORMAT_SAMPLE_1, "DAY", 1);

                Goal goal = goalManager.getListById(goalId);
                String goalDate = goal.getGoalDate();
                if(goalDate != null && goalDate.isEmpty() == false){
                    Date goalDateDate = Common.getDateByStr(goalDate, Common.DATE_FORMAT_SAMPLE_1);
                    if(nextDate.getTime() > goalDateDate.getTime()){
                        Snackbar.make(v, "--You cant over goaldate", Snackbar.LENGTH_SHORT).show();
                    }else{
                        targetDate.setText(Common.formatDate(nextDate, Common.DATE_FORMAT_SAMPLE_1));
                        setScrollViewData();
                        Snackbar.make(v, "--change next date", Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    targetDate.setText(Common.formatDate(nextDate, Common.DATE_FORMAT_SAMPLE_1));
                    setScrollViewData();
                    Snackbar.make(v, "--change next date", Snackbar.LENGTH_SHORT).show();
                }


            }
        });
        createBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "createBasic", Snackbar.LENGTH_SHORT).show();
                DialogBasicMeasure basicMeasure = new DialogBasicMeasure();
                Bundle bundle = new Bundle();
                bundle.putInt("goalId",goalId);
                bundle.putString("target",startDate.getText().toString());
                basicMeasure.setArguments(bundle);
                basicMeasure.show(getSupportFragmentManager(), "dialog");
            }
        });
        createGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "createNewRecord", Snackbar.LENGTH_SHORT).show();
                DatePickDialog datePickDialog = new DatePickDialog();
                Bundle bundle = new Bundle();
                bundle.putString("target", "goalDate");
                datePickDialog.setArguments(bundle);
                datePickDialog.show(getSupportFragmentManager(), "dateDialog");
            }
        });

        targetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickDialog datePickDialog = new DatePickDialog();
                Bundle bundle = new Bundle();
                bundle.putString("target", "targetDate");
                datePickDialog.setArguments(bundle);
                datePickDialog.show(getSupportFragmentManager(), "dateDialog");
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickDialog datePickDialog = new DatePickDialog();
                Bundle bundle = new Bundle();
                bundle.putString("target", "startDate");
                datePickDialog.setArguments(bundle);
                datePickDialog.show(getSupportFragmentManager(), "dateDialog");
            }
        });

        playmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

        btShowGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goal date
                Goal goal = goalManager.getListById(goalId);
                if(goal.getGoalDate() != null && goal.getGoalDate().isEmpty() == false){
                    targetDate.setText(goal.getGoalDate());
                    setScrollViewData();
                    Snackbar.make(v, "--show goal date", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(v, "--goal date is not defined!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btShowToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String today = Common.formatDate(new Date(), Common.DATE_FORMAT_SAMPLE_1);
                targetDate.setText(today);
                setScrollViewData();
                Snackbar.make(v, "--show  date", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void setDate(){
        Goal goal = goalManager.getListById(goalId);
        title.setText(goal.getGoalTitle());
        startDate.setText(goal.getGoalMeasureDate() == null || goal.getGoalMeasureDate().isEmpty() ? Common.formatDate(new Date(), Common.DATE_FORMAT_SAMPLE_1) : goal.getGoalMeasureDate());
    }

    private void setScrollViewData(){

        leftScrollContents.removeAllViews();
        rightScrollContents.removeAllViews();

        TransitionDrawable transitionDrawableStart = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(Color.BLACK),
                getResources().getDrawable(R.drawable.background1)
        });

        TransitionDrawable transitionDrawableGoal = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(Color.BLACK),
                getResources().getDrawable(R.drawable.background2)
        });

        leftScrollContents.setBackground(transitionDrawableStart);
        rightScrollContents.setBackground(transitionDrawableGoal);

        transitionDrawableGoal.startTransition(700);
        transitionDrawableStart.startTransition(700);

        //goal日の設定
        Goal goal = goalManager.getListById(goalId);
        String goalDate = goal.getGoalDate();
        if(goalDate != null && goalDate.isEmpty() == false){
            goalDateStr.setText(goalDate);
            if(goalDate.equals(targetDate.getText().toString())){
                //Common.log("Today is Goal Date!");
                //rightScroll.setBackgroundColor(Color.parseColor("#000000"));
            }else{
                //Common.log("Today is not Goal Date!");
                //rightScroll.setBackgroundColor(Color.parseColor("#eeeeee"));
            }
        }else{
            //Common.log("Today is not Goal Date!");
            goalDateStr.setText("--");
            //rightScroll.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        List<GoalMeasure> list = goalManager.getMeasureByGoalId(goalId);
        final GoalMeasure[] measures = list.toArray(new GoalMeasure[list.size()]);

        for(int i = 0; i < measures.length; i++){

            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_cmp01, null);
            ConstraintLayout beforeLayout = view.findViewById(R.id.layout_row);
            TextView title = view.findViewById(R.id.title);
            TextView intUnitName = view.findViewById(R.id.int_value_unit_name);
            title.setText(measures[i].getMeasureTitle());

            ImageView imageval = view.findViewById(R.id.image_value);
            TextView intval = view.findViewById(R.id.int_value);

            if(measures[i].getMeasureType() != null && measures[i].getMeasureType().equals("image")){
                byte[] tmpImg = measures[i].getMeasureImageValue();
                //Common.log("image:" + tmpImg.length);
                if(tmpImg != null && tmpImg.length > 0){
                    imageval.setImageBitmap(BitmapFactory.decodeByteArray(tmpImg, 0, tmpImg.length));
                }

            }else if(measures[i].getMeasureType().equals("int")){
                intval.setText(String.valueOf(measures[i].getMeasureIntValue()));
                intUnitName.setText(measures[i].getIntUnitName());
                int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(WC,WC);
                imageval.setLayoutParams(params);

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
            final int measureId = measures[i].getMeasureId();
            beforeLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("--Header beforetitle");
                    menu.add("--edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //edut
                            DialogBasicMeasure basicMeasure = new DialogBasicMeasure();
                            Bundle bundle = new Bundle();
                            bundle.putInt("measureId",measureId);
                            bundle.putInt("goalId",goalId);
                            bundle.putString("target", startDate.getText().toString());
                            basicMeasure.setArguments(bundle);
                            basicMeasure.show(getSupportFragmentManager(), "dialog");
                            return true;
                        }
                    });
                    menu.add("--delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //delete
                            DialogDeleteConfirm deleteConfirm = new DialogDeleteConfirm();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", measureId);
                            bundle.putString("dataType","basicMeasure");
                            deleteConfirm.setArguments(bundle);
                            deleteConfirm.show(getSupportFragmentManager(), "dialog");
                            return true;
                        }
                    });
                }
            });

            View view2;
            if(history != null){
                view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_cmp02, null);
                ConstraintLayout layout = view2.findViewById(R.id.layout_row);
                TextView setValue  = view2.findViewById(R.id.int_value);
                TextView historyIntValueUnitName  = view2.findViewById(R.id.int_value_unit_name);
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
                        historyIntValueUnitName.setText(measures[i].getIntUnitName());

                        int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(WC,WC);
                        imageValue.setLayoutParams(params);

                        break;

                    case "image":
                        byte[] tmpImage = history.getMeasureImageValue();
                        if(tmpImage != null && tmpImage.length > 0){
                            imageValue.setImageBitmap(BitmapFactory.decodeByteArray(tmpImage, 0 , tmpImage.length));
                        }

                        break;
                }
                final int historyId = history.getHistoryId();
                final String targetMeasureDate = targetDate.getText().toString();
                layout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle("--Header title");
                        menu.add("--edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //edit
                                DialogHistory dialogHistory = new DialogHistory();
                                Bundle bundle = new Bundle();
                                bundle.putInt("measureId", measureId);
                                bundle.putString("targetMeasureDate", targetMeasureDate);
                                bundle.putInt("historyId", historyId);
                                dialogHistory.setArguments(bundle);
                                dialogHistory.show(getSupportFragmentManager(), "dialog");

                                return true;
                            }
                        });

                        menu.add("--delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //delete
                                DialogDeleteConfirm deleteConfirm = new DialogDeleteConfirm();
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", historyId);
                                bundle.putString("dataType","history");
                                deleteConfirm.setArguments(bundle);
                                deleteConfirm.show(getSupportFragmentManager(), "dialog");

                                return true;
                            }
                        });
                    }
                });


            }else{
                view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_row_comp3, null);
                ConstraintLayout layout = view2.findViewById(R.id.layout_row);
                ImageView imageValue = view2.findViewById(R.id.image_value);
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
                switch (measures[i].getMeasureType()){
                    case "int":
                        int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(WC,WC);
                        imageValue.setLayoutParams(params);
                        imageValue.setVisibility(View.GONE);

                        break;

                }
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

    @Override
    public void deleteResultNotice(int order) {
        View view = findViewById(android.R.id.content);
        Snackbar.make(view,getString(R.string.success_msg2),Snackbar.LENGTH_LONG ).show();
        setScrollViewData();
        setDate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == DialogHistory.RESULT_PICK_IMAGE_FROM_FRAGMENT || requestCode == DialogBasicMeasure.RESULT_PICK_IMAGE_FROM_FRAGMENT){

            try{
                byte[] byteImage;

                Uri imageUri = data.getData();
                byteImage = Common.getImageByteFromUri(getApplicationContext(), imageUri, IMAGE_WIDTH);

                //確認用の画像
                Bitmap img = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);

                //find fragment
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
                if(fragment != null){

                    switch (requestCode){
                        case DialogHistory.RESULT_PICK_IMAGE_FROM_FRAGMENT:
                            DialogHistory dialogHistory = (DialogHistory)fragment;
                            dialogHistory.setImage(img,byteImage);
                            break;
                        case DialogBasicMeasure.RESULT_PICK_IMAGE_FROM_FRAGMENT:
                            DialogBasicMeasure dialogBasicMeasure = (DialogBasicMeasure)fragment;
                            dialogBasicMeasure.setImage(img,byteImage);
                            break;
                    }

                }
            }catch (Exception e){
                Common.log(e.getMessage());
            }

        }

        if(requestCode == DialogHistory.RESULT_CAMERA_FROM_FRAGMENT || requestCode == DialogBasicMeasure.RESULT_CAMERA_FROM_FRAGMENT){

            // dataから画像を取り出す
            try{
                if(data.getExtras() != null){

                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    //設定された幅になるまで画像を縮小する
                    int p = 1;

                    while(width > IMAGE_WIDTH){
                        //縮小率を決める
                        p *= 2;
                        width /= p;
                    }


                    Bitmap resizeImage;
                    if(p > 1){
                        resizeImage = Bitmap.createScaledBitmap(bitmap,(int)(width/p),(int)(height/p),true);
                    }else{
                        resizeImage = bitmap;
                    }

                    byte[] byteImage = Common.convertBitMapToByteArray2(resizeImage);

                    //find fragment
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
                    if(fragment != null){

                        switch (requestCode){
                            case DialogHistory.RESULT_CAMERA_FROM_FRAGMENT:
                                DialogHistory dialogHistory = (DialogHistory)fragment;
                                dialogHistory.setImage(bitmap,byteImage);
                                break;
                            case DialogBasicMeasure.RESULT_CAMERA_FROM_FRAGMENT:
                                DialogBasicMeasure dialogBasicMeasure = (DialogBasicMeasure)fragment;
                                dialogBasicMeasure.setImage(bitmap,byteImage);
                                break;
                        }

                    }

                }
            }catch (Exception e){
                Common.log(e.getMessage());
            }



        }


    }

    @Override
    public void datePickResultNotice(String dateStr, String target) {

        Common.log(target);

        if(target != null && target.isEmpty() == false){

            View view = findViewById(android.R.id.content);
            GoalManager goalManager = new GoalManager(getApplicationContext());
            String goalDate;
            Date goalDatedate;

            switch (target){

                case "startDate":

                    Date start1 = Common.getDateByStr(dateStr, Common.DATE_FORMAT_SAMPLE_1);
                    startDate.setText(Common.formatDate(start1, Common.DATE_FORMAT_SAMPLE_1));
                    //set goal to the goal
                    Goal goal1 = goalManager.getListById(goalId);
                    goal1.setGoalMeasureDate(dateStr);
                    long result1 = goalManager.updateGoal(goal1);
                    if(result1 > 0){
                        //setScrollViewData();
                        Snackbar.make(view, "--datePickResultNotice Change date -- start is setted", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(view, "--datePickResultNotice Change date -- start is failed", Snackbar.LENGTH_SHORT).show();
                    }
                    break;

                case "targetDate":
                    //Snackbar.make(v, "before", Snackbar.LENGTH_SHORT).show();
                    Date nextDate = Common.getDateByStr(dateStr, Common.DATE_FORMAT_SAMPLE_1);
                    //Date nextDate = Common.addDateFromTargetDate(dateStr, Common.DATE_FORMAT_SAMPLE_1, "DAY", -1);
                    Date nextDateAdd = Common.addDateFromTargetDate(dateStr, Common.DATE_FORMAT_SAMPLE_1, "DAY", 1);

                    Date start2 = Common.getDateByStr(startDate.getText().toString(), Common.DATE_FORMAT_SAMPLE_1);

                    Goal goal3 = goalManager.getListById(goalId);
                    goalDate = goal3.getGoalDate();
                    goalDatedate = Common.getDateByStr(goalDate, Common.DATE_FORMAT_SAMPLE_1);

                    if(start2.getTime() > nextDate.getTime()) {
                        Snackbar.make(view, "--error nextDate is bellow startdate not allowed", Snackbar.LENGTH_SHORT).show();
                    }else if(nextDate.getTime() > goalDatedate.getTime()){
                        Snackbar.make(view, "--error nextDate is over goalDate not allowed", Snackbar.LENGTH_SHORT).show();
                    }else{
                        targetDate.setText(dateStr);
                        setScrollViewData();
                        Snackbar.make(view, "--datePickResultNotice Change date", Snackbar.LENGTH_SHORT).show();
                    }
                    break;

                case "goalDate":

                    goalDatedate = Common.addDateFromTargetDate(dateStr, Common.DATE_FORMAT_SAMPLE_1, "DAY", -1);
                    Date start3 = Common.getDateByStr(startDate.getText().toString(), Common.DATE_FORMAT_SAMPLE_1);

                    if(start3.getTime() > goalDatedate.getTime()) {
                        Snackbar.make(view, "--error goalDate is bellow startdate not allowed", Snackbar.LENGTH_SHORT).show();
                    }else{
                        targetDate.setText(dateStr);//as goal
                        //set goal to the goal
                        Goal goal2 = goalManager.getListById(goalId);
                        goal2.setGoalDate(dateStr);
                        long result2 = goalManager.updateGoal(goal2);
                        if(result2 > 0){
                            setScrollViewData();
                            Snackbar.make(view, "--datePickResultNotice Change date -- goal is setted", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(view, "--datePickResultNotice Change date -- goal is failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    break;

            }

        }

    }


}


