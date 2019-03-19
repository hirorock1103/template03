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

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.DB.GoalManager;
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.R;

public class DialogAddGoal extends AppCompatDialogFragment {

    //
    private int goalId;
    private EditText goalTitle;

    private DialogAddGoalListener listener;

    public interface DialogAddGoalListener{
        public void DialogAddGoalResultNotice();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.listener = (DialogAddGoalListener)context;
        }catch(ClassCastException e){
            Common.log(e.getMessage());
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_goal, null);

        goalTitle = view.findViewById(R.id.title_edit);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view)
                .setTitle(getString(R.string.dialog_title2))
                .setNegativeButton(getString(R.string.CANCEL), null)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String error = "";
                        if(goalTitle.getText().toString() == null || goalTitle.getText().toString().isEmpty()){
                            error = getString(R.string.error_msg1);
                        }

                        if(error.isEmpty()){

                            long result = 0;

                            Goal goal = new Goal();
                            goal.setGoalTitle(goalTitle.getText().toString());

                            GoalManager manager = new GoalManager(getContext());
                            result = manager.addGoal(goal);

                            if(result > 0){
                                listener.DialogAddGoalResultNotice();
                            }



                        }



                    }
                });

        Dialog dialog = builder.create();

        return dialog;




    }
}
