package com.patrau.roxana.photoedit.objects;

import android.graphics.drawable.Drawable;

public class Effect {

    private String effectName;
    private int effectImage;

    public Effect(String effectName, int effectImage) {
        this.effectName = effectName;
        this.effectImage = effectImage;
    }

    public String getEffectName() {
        return effectName;
    }

    public int getEffectImage() {
        return effectImage;
    }
}
