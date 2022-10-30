package com.example.lucent.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.lucent.R;
import com.example.lucent.view.Navigator;

public class PopupDialog extends Dialog {
    Button popup_login_btn;
    FragmentActivity activity;
    Navigator navigator = new Navigator();
    public PopupDialog(Context context, FragmentActivity activity){
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources()
                .getColor(androidx.media.R.color.secondary_text_default_material_dark)));
        setContentView(R.layout.login_popup);
        popup_login_btn = findViewById(R.id.id_login_popup_btn);
        popup_login_btn.setOnClickListener(v -> {
            Log.e("Dialog","Login button clicked");
//            listener.btnListener();
            navigator.navLogin(activity);
            PopupDialog.this.hide();
        });
    }
}
