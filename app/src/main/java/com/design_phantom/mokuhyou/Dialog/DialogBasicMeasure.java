package com.design_phantom.mokuhyou.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.R;

import java.util.Date;

public class DialogBasicMeasure extends AppCompatDialogFragment {

    //
    private int goalId;

    private RadioGroup radioGroup;
    private TextView title;
    private Button btPickdate;
    private TextView measureDate;
    private TextView imageAreaTitle;
    private EditText intEdit;
    private LinearLayout intValueArea;
    private LinearLayout imgValueArea;

    GoalManager goalManager;

    //listener
    private DialogBasicMeasureListener listener;

    public interface DialogBasicMeasureListener{
        public void DialogBasicMeasureResult(String error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogBasicMeasureListener)context;
        }catch (ClassCastException e){
            Common.log("DialogBasicMeasureではDialogBasicMeasureListenerをimplementsしてください");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_basic_contents, null);
        goalManager = new GoalManager(getContext());

        try{
            goalId = getArguments().getInt("goalId");
        }catch (Exception e){
            goalId = 0;
        }

        //setview
        radioGroup = view.findViewById(R.id.radio_parent);
        title = view.findViewById(R.id.measure_basic_edit);
        imageAreaTitle = view.findViewById(R.id.image_area_title);
        btPickdate = view.findViewById(R.id.date_pick);
        measureDate = view.findViewById(R.id.measure_date);
        intValueArea = view.findViewById(R.id.int_area);
        imgValueArea = view.findViewById(R.id.image_area);
        intEdit = view.findViewById(R.id.int_value_edit);

        //setlistener
        setListener();

        //setdata
        setData();

        //set dialog bellow
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setTitle(getString(R.string.dialog_title3));
        builder.setNegativeButton(getString(R.string.CANCEL), null);
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String error = "";

                StringBuilder errorBuilder = new StringBuilder();
                if(goalId == 0){
                    errorBuilder.append(getString(R.string.error_msg5)+"\n");
                }
                if(title.getText().toString().isEmpty()){
                    errorBuilder.append(getString(R.string.error_msg4)+"\n");
                }
                if(measureDate.getText().toString().isEmpty()){
                    errorBuilder.append(getString(R.string.error_msg7)+"\n");
                }

                int selectId = radioGroup.getCheckedRadioButtonId();
                String type = "";
                switch (selectId){
                    case R.id.int_value:
                        type = "int";
                        break;
                    case R.id.image_value:
                        type = "image";
                        break;
                }
                if(type.isEmpty()){
                    errorBuilder.append(getString(R.string.error_msg6)+"\n");
                }else{

                    if(type.equals("int")){
                        if(intEdit.getText().toString().isEmpty()){
                            errorBuilder.append(getString(R.string.error_msg8)+"\n");
                        }
                    }else if(type.equals("image")){

                    }

                }

                error = errorBuilder.toString();

                if(error.isEmpty()){

                    long resultId = 0;

                    GoalMeasure measure1 = new GoalMeasure();
                    measure1.setParentGoalId(goalId);
                    measure1.setMeasureTitle(title.getText().toString());
                    measure1.setMeasureDate(measureDate.getText().toString());
                    measure1.setMeasureType(type);
                    if(type.equals("int")){
                        measure1.setMeasureIntValue(Integer.parseInt(intEdit.getText().toString()));
                    }
                    if(type.equals("image")){
                        measure1.setMeasureImageValue(null);
                    }

                    GoalManager goalManager = new GoalManager(getContext());
                    Goal goal = goalManager.getListById(goalId);
                    goal.setGoalMeasureDate(measureDate.getText().toString());
                    goalManager.updateGoal(goal);

                    //add measure
                    resultId = goalManager.addMeasure(measure1);

                    if(resultId > 0){
                        //listener.DialogBasicMeasureResult(error);
                    }else{
                        error = "failed to add measure!";
                        //listener.DialogBasicMeasureResult(error);
                    }

                }

                listener.DialogBasicMeasureResult(error);
            }
        });

        Dialog dialog = builder.create();

        return dialog;

    }

    private void setData(){
        intValueArea.setVisibility(View.GONE);
        imgValueArea.setVisibility(View.GONE);

        String date = null;
        if(goalId > 0){
            Goal goal = goalManager.getListById(goalId);
            date = goal.getGoalMeasureDate();
        }
        if(date == null || date.isEmpty()){
            measureDate.setText(Common.formatDate(new Date(), Common.DATE_FORMAT_SAMPLE_1));
        }else{
            measureDate.setText(date);
        }

    }

    private void setListener(){

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Common.log("checked:" + checkedId);
                switch (checkedId){

                    case R.id.int_value:
                        //show
                        intValueArea.setVisibility(View.VISIBLE);
                        imgValueArea.setVisibility(View.GONE);
                        imageAreaTitle.setVisibility(View.GONE);
                        break;
                    case R.id.image_value:
                        //show
                        intValueArea.setVisibility(View.GONE);
                        imgValueArea.setVisibility(View.VISIBLE);
                        imageAreaTitle.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });

        btPickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open date pick dialog
            }
        });

    }


}
