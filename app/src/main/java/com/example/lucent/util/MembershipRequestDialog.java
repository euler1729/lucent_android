package com.example.lucent.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class MembershipRequestDialog extends Dialog {
    FragmentActivity activity;
    public MembershipRequestDialog(@NonNull Context context, FragmentActivity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources()
                .getColor(androidx.media.R.color.secondary_text_default_material_dark)));

    }
}
