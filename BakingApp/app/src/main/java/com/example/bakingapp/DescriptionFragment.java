package com.example.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DescriptionFragment extends Fragment {

    private StepSetter stepSetter;
    private int position = 0;
    private boolean tabletFlag = false;

    public DescriptionFragment(){}

    @SuppressLint("ValidFragment")
    public DescriptionFragment(StepSetter stepSetter, int position, boolean tabletFlag){
        this.stepSetter = stepSetter;
        this.position = position;
        this.tabletFlag = tabletFlag;
    }

    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepClicked(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnStepClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.fragment_text);

        if(savedInstanceState == null) {
            textView.setText(stepSetter.getDescription());
        }else{
            StepSetter setter = savedInstanceState.getParcelable("desc");
            textView.setText(setter.getDescription());
        }

        // IF NOT TABLET
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabletFlag) {
                    mCallback.onStepClicked(Integer.parseInt(stepSetter.getId()));
                } else {
                    inflater.getContext().startActivity(new Intent(inflater.getContext(), StepDetails.class)
                            .putExtra("stepSetter", stepSetter)
                            .putExtra("parent_position", position));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("desc", stepSetter);
        outState.putInt("position", position);
        outState.putBoolean("tabletFlag", tabletFlag);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!= null) {
            stepSetter = savedInstanceState.getParcelable("desc");
            position = savedInstanceState.getInt("position");
            tabletFlag = savedInstanceState.getBoolean("tabletFlag");
        }
    }
}
