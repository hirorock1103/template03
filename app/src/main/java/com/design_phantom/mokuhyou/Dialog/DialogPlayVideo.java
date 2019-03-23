package com.design_phantom.mokuhyou.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.design_phantom.mokuhyou.R;

public class DialogPlayVideo extends AppCompatDialogFragment {

    private ConstraintLayout layout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_video, null);
        layout = view.findViewById(R.id.layout);
        final ImageSwitcher imageSwitcher = new ImageSwitcher(getContext());
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getContext());
            }
        });

        // インアニメーションを設定する。
        AlphaAnimation inAnimation = new AlphaAnimation(0,1);
        inAnimation.setDuration(500);
        inAnimation.setStartOffset(500);
        imageSwitcher.setInAnimation(inAnimation);

        // アウトアニメーションを設定する。
        AlphaAnimation outAnimation = new AlphaAnimation(1,0);
        outAnimation.setDuration(500);
        imageSwitcher.setOutAnimation(outAnimation);



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setTitle("--video--");
        builder.setNegativeButton(getString(R.string.CANCEL), null);
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();

    }
}
