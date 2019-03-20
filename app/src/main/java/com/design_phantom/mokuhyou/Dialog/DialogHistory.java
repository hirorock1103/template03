package com.design_phantom.mokuhyou.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.Master.MeasureHistory;
import com.design_phantom.mokuhyou.R;

import java.util.List;

public class DialogHistory extends AppCompatDialogFragment {

    private int measureId;
    private String targetMeasureDate;
    private String type;
    private byte[] imageByte;
    //view
    private TextView measureTitle;
    private TextView measureDate;
    private TextView typeName;
    private ImageView image;
    private EditText inputIntValue;
    private LinearLayout intArea;
    private LinearLayout imageArea;

    //manager
    private GoalManager goalManager;

    //listener
    private DialogHistoryListener listener;

    public interface DialogHistoryListener{
        public void dialogHistoryNotice(String error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (DialogHistoryListener)context;
        }catch(ClassCastException e){
            Common.log("you must implements DialogHistoryListener To the Activity");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_basic_history, null);
        //set view
        measureTitle = view.findViewById(R.id.measure_title);
        measureDate = view.findViewById(R.id.measure_date);
        typeName = view.findViewById(R.id.type_name);
        image = view.findViewById(R.id.image_src);
        inputIntValue = view.findViewById(R.id.int_value_edit);
        intArea = view.findViewById(R.id.int_area);
        imageArea = view.findViewById(R.id.image_area);

        intArea.setVisibility(View.GONE);
        imageArea.setVisibility(View.GONE);

        goalManager = new GoalManager(getContext());

        try{

            measureId = getArguments().getInt("measureId");
            targetMeasureDate = getArguments().getString("targetMeasureDate");
            GoalMeasure measure = goalManager.getMeasureById(measureId);

            measureTitle.setText(measure.getMeasureTitle());
            measureDate.setText(targetMeasureDate);
            typeName.setText(measure.getMeasureType());
            type = measure.getMeasureType();
            if(type.equals("int")){
                intArea.setVisibility(View.VISIBLE);
            }else if(type.equals("image")){
                imageArea.setVisibility(View.VISIBLE);
            }

            //history



        }catch(Exception e){
            measureId = 0;
            targetMeasureDate = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle(getString(R.string.dialog_title3));
        builder.setNegativeButton(getString(R.string.CANCEL), null);
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                String error = "";
                StringBuilder errorBuilder = new StringBuilder();
                if(measureId == 0){
                    errorBuilder.append("-measureId error-" + "\n");
                }
                if(targetMeasureDate == null || targetMeasureDate.isEmpty()){
                    errorBuilder.append("-targetMeasureDate error-" + "\n");
                }
                if(type == null || type.isEmpty()){
                    errorBuilder.append("-type error-" + "\n");
                }else{
                    if(type.equals("int")){
                        if(inputIntValue.getText().toString().isEmpty()){
                            errorBuilder.append("-int error-" + "\n");
                        }
                    }else if(type.equals("image")){

                    }
                }
                error = errorBuilder.toString();
                if(error.isEmpty()){

                    long resultId = 0;

                    //action
                    MeasureHistory history = new MeasureHistory();
                    history.setParentMeasureId(measureId);
                    history.setMeasureDate(targetMeasureDate);
                    if(type.equals("int")){
                        history.setMeasureIntValue(Integer.parseInt(inputIntValue.getText().toString()));
                    }else{
                        history.setMeasureImageValue(imageByte);
                    }

                    GoalManager goalManager = new GoalManager(getContext());
                    resultId = goalManager.addMeasureHistory(history);

                    if(resultId > 0){

                    }else{
                        error = "error -fail to add history-";
                    }

                }

                listener.dialogHistoryNotice(error);
            }
        });
        Dialog dialog = builder.create();
        return dialog;
    }
}
