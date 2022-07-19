package com.example.notes;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

public enum Importance {
    MINOR(R.string.minor, R.color.minor_bg_color),
    MEDIUM(R.string.medium, R.color.medium_bg_color),
    IMPORTANT(R.string.important,R.color.important_bg_color);

    @StringRes
    private final int title;

    @ColorRes
    private final int impColorRes;

    Importance(int title, int themeRes) {
        this.title = title;
        this.impColorRes = themeRes;
    }

    public int getColorRes() {
        return impColorRes;
    }
}
