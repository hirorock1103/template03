package com.design_phantom.mokuhyou.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.R;

import java.io.File;
import java.util.Date;

public class DialogBasicMeasure extends AppCompatDialogFragment {

    public final static int RESULT_CAMERA_FROM_FRAGMENT = 1003;
    public final static int RESULT_PICK_IMAGE_FROM_FRAGMENT = 1004;

    //
    private int goalId;
    private int measureID;
    private String targetDate;
    private byte[] imageByte;

    private RadioGroup radioGroup;
    private TextView title;
    private TextView measureDate;
    private TextView imageAreaTitle;
    private ImageView image;
    private Button btTakePic;
    private Button btFindFile;
    private EditText intEdit;
    private EditText intUnitEdit;
    private LinearLayout intValueArea;
    private LinearLayout imgValueArea;

    GoalManager goalManager;

    //listener
    private DialogBasicMeasureListener listener;

    public interface DialogBasicMeasureListener{
        public void DialogBasicMeasureResult(String error);
    }

    public void setImage(Bitmap bitmap, byte[] byteImage){
        image.setImageBitmap(bitmap);
        this.imageByte = byteImage;
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
            targetDate = getArguments().getString("target");
        }catch (Exception e){
            goalId = 0;
        }

        try{
            measureID = getArguments().getInt("measureId");

        }catch (Exception e){
            measureID = 0;
        }

        Common.log("s:" + targetDate);
        Common.log("s:" + goalId);

        //setview
        radioGroup = view.findViewById(R.id.radio_parent);
        title = view.findViewById(R.id.measure_basic_edit);
        imageAreaTitle = view.findViewById(R.id.image_area_title);
        measureDate = view.findViewById(R.id.measure_date);
        intValueArea = view.findViewById(R.id.int_area);
        imgValueArea = view.findViewById(R.id.image_area);
        intEdit = view.findViewById(R.id.int_value_edit);
        intUnitEdit = view.findViewById(R.id.int_value_unit_name);
        image = view.findViewById(R.id.image_src);
        btTakePic = view.findViewById(R.id.takepic);
        btFindFile = view.findViewById(R.id.findfile);

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

                    if(measureID > 0){

                        GoalMeasure measure2 = goalManager.getMeasureById(measureID);
                        measure2.setMeasureTitle(title.getText().toString());
                        measure2.setMeasureType(type);
                        if(type.equals("int")){
                            measure2.setMeasureIntValue(Integer.parseInt(intEdit.getText().toString()));
                            measure2.setIntUnitName(intUnitEdit.getText().toString());
                        }
                        if(type.equals("image")){
                            measure2.setMeasureImageValue(imageByte);
                        }

                        resultId = goalManager.updateGoalMeasure(measure2);


                    }else{

                        GoalMeasure measure1 = new GoalMeasure();
                        measure1.setParentGoalId(goalId);
                        measure1.setMeasureTitle(title.getText().toString());
                        measure1.setMeasureType(type);
                        if(type.equals("int")){
                            measure1.setMeasureIntValue(Integer.parseInt(intEdit.getText().toString()));
                            measure1.setIntUnitName(intUnitEdit.getText().toString());
                        }
                        if(type.equals("image")){
                            measure1.setMeasureImageValue(imageByte);
                        }

                        //add measure
                        resultId = goalManager.addMeasure(measure1);

                    }



                    //共通
                    GoalManager goalManager = new GoalManager(getContext());
                    Goal goal = goalManager.getListById(goalId);
                    goal.setGoalMeasureDate(measureDate.getText().toString());
                    goalManager.updateGoal(goal);


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

        if(intEdit.getText().toString().isEmpty()){
            intEdit.setText("0");
        }

        /*
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
        */

        measureDate.setText(targetDate);


        if(measureID > 0){
            GoalMeasure measure = goalManager.getMeasureById(measureID);
            title.setText(measure.getMeasureTitle());
            if(measure.getMeasureType().equals("int")){
                radioGroup.check(R.id.int_value);
                intEdit.setText(String.valueOf(measure.getMeasureIntValue()));
                intUnitEdit.setText(measure.getIntUnitName());
            }else{
                radioGroup.check(R.id.image_value);
                imageByte = measure.getMeasureImageValue();
                if(imageByte != null && imageByte.length > 0){
                    image.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
                }
            }
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

        btFindFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take photo --https://akira-watson.com/android/camera-intent.html
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                //image file directory
                File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = picDirectory.getPath();

                //uri
                Uri data = Uri.parse(path);
                photoPickerIntent.setDataAndType(data, "image/*");
                getActivity().startActivityForResult(photoPickerIntent, RESULT_PICK_IMAGE_FROM_FRAGMENT);
            }
        });

        btTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take photo
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(intent, RESULT_CAMERA_FROM_FRAGMENT);
            }
        });

    }


}
