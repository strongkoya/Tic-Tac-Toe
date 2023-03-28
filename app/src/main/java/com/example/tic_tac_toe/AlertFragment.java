package com.example.tic_tac_toe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertFragment extends DialogFragment {

    private final String mContentText;
    private final int mAnimationType;
    private DialogInterface.OnClickListener positiveButtonListener;
    private String text;
    private MyCanvasView myCanvasView;

    public AlertFragment(String contentText, int animationType) {
        mContentText = contentText;
        mAnimationType = animationType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Créer une instance de la vue du fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_alert, null);

        /*View view = inflater.inflate(R.layout.fragment_alert, container, false);*//*

        TextView textView = view.findViewById(R.id.alert_text_view);
        textView.setText(mContentText);

        Animation animation = getAnimation(mAnimationType);
        textView.startAnimation(animation);



        view.findViewById(R.id.alert_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
                }
                dismiss();
            }
        });*/

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Créer une instance de la vue du fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_alert, null);

        /*View view = inflater.inflate(R.layout.fragment_alert, container, false);*/

        TextView textView = view.findViewById(R.id.alert_text_view);
        textView.setText(mContentText);

        Animation animation = getAnimation(mAnimationType);
        textView.startAnimation(animation);


        myCanvasView = view.findViewById(R.id.MyCanvasView);
        myCanvasView.startAnimation();
        view.findViewById(R.id.alert_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
                }
                dismiss();
            }
        });

        // Créer une nouvelle AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);


        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        builder.setTitle(null);
        builder.setCancelable(false);
        return builder.create();
    }


    private Animation getAnimation(int animationType) {
        switch (animationType) {
            case 1:
                return AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            case 2:
                return AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            case 3:
                return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            default:
                return null;
        }
    }

    public void setPositiveButton(DialogInterface.OnClickListener listener) {

        positiveButtonListener = listener;

    }

}

