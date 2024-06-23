package com.example.losako;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CustomFloatingActionButton extends FloatingActionButton {

    public CustomFloatingActionButton(Context context) {
        super(context);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        // Conservez le comportement standard du clic
        super.performClick();
        // Pas de logique supplémentaire nécessaire dans cet exemple
        return true;
    }
}

