package com.design_phantom.mokuhyou.Dialog;

import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.Master.MeasureHistory;
import com.design_phantom.mokuhyou.R;

import java.io.File;
import java.util.List;

public class DialogHistory extends AppCompatDialogFragment {

    public final static int RESULT_CAMERA_FROM_FRAGMENT = 1001;
    public final static int RESULT_PICK_IMAGE_FROM_FRAGMENT = 1002;

    private int measureId;
    private String targetMeasureDate;
    private int historyId;

    private String type;
    private byte[] imageByte;
    //view
    private TextView measureTitle;
    private TextView measureDate;
    private TextView typeName;
    private TextView intValUnitName;
    private Button btTakePic;
    private Button btFindFile;
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

    public void setImage(Bitmap bitmap, byte[] byteImage){
        image.setImageBitmap(bitmap);
        this.imageByte = byteImage;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_basic_history, null);
        //set view
        measureTitle = view.findViewById(R.id.measure_title);
        measureDate = view.findViewById(R.id.measure_date);
        typeName = view.findViewById(R.id.type_name);
        intValUnitName = view.findViewById(R.id.int_value_unit_name);
        image = view.findViewById(R.id.image_src);
        inputIntValue = view.findViewById(R.id.int_value_edit);
        intArea = view.findViewById(R.id.int_area);
        imageArea = view.findViewById(R.id.image_area);
        btTakePic = view.findViewById(R.id.takepic);
        btFindFile = view.findViewById(R.id.findfile);

        intArea.setVisibility(View.GONE);
        imageArea.setVisibility(View.GONE);

        //setListener
        setListener();

        goalManager = new GoalManager(getContext());

        try{

            measureId = getArguments().getInt("measureId");
            targetMeasureDate = getArguments().getString("targetMeasureDate");
            GoalMeasure measure = goalManager.getMeasureById(measureId);

            measureTitle.setText(measure.getMeasureTitle());
            measureDate.setText(targetMeasureDate);
            intValUnitName.setText(measure.getIntUnitName() != null && measure.getIntUnitName().isEmpty() == false ? measure.getIntUnitName() : "-");
            typeName.setText(measure.getMeasureType());
            type = measure.getMeasureType();
            if(type.equals("int")){
                intArea.setVisibility(View.VISIBLE);
            }else if(type.equals("image")){
                imageArea.setVisibility(View.VISIBLE);
            }

        }catch(Exception e){
            measureId = 0;
            targetMeasureDate = null;
        }

        //historyId exists, then get hisytory Data
        MeasureHistory history = null;
        try{
            historyId = getArguments().getInt("historyId");
            if(historyId > 0){
                history = goalManager.getMeasureHistoryById(historyId);
                inputIntValue.setText(String.valueOf(history.getMeasureIntValue()));
                byte[] tmpImg = history.getMeasureImageValue();
                if(tmpImg != null && tmpImg.length > 0){
                    image.setImageBitmap(BitmapFactory.decodeByteArray(tmpImg, 0, tmpImg.length));
                }

            }
        }catch (Exception e){
            historyId = 0;
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

                    GoalManager goalManager = new GoalManager(getContext());

                    long resultId = 0;

                    if(historyId > 0){

                        //action
                        MeasureHistory history = goalManager.getMeasureHistoryById(historyId);
                        if(type.equals("int")){
                            history.setMeasureIntValue(Integer.parseInt(inputIntValue.getText().toString()));
                        }else{
                            history.setMeasureImageValue(imageByte);
                        }

                        resultId = goalManager.updateMeasureHistory(history);

                    }else{

                        //action
                        MeasureHistory history = new MeasureHistory();
                        history.setParentMeasureId(measureId);
                        history.setMeasureDate(targetMeasureDate);
                        if(type.equals("int")){
                            history.setMeasureIntValue(Integer.parseInt(inputIntValue.getText().toString()));
                        }else{
                            history.setMeasureImageValue(imageByte);
                        }
                        resultId = goalManager.addMeasureHistory(history);

                    }


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

    private void setListener(){

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
