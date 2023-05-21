package com.example.imius.fragment;

import static android.graphics.Color.TRANSPARENT;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.imius.databinding.DialogNoInternetBinding;

public class NoInternetDialog extends DialogFragment {

    private DialogNoInternetBinding binding;

    public static NoInternetDialog newInstance() {
        return new NoInternetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogNoInternetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.dialogNoInternetBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) layoutParams);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

    }
}
