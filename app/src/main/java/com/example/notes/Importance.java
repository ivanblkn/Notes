package com.example.notes;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

public enum Importance {
    MINOR(R.string.minor, R.drawable.ic_minor),
    MEDIUM(R.string.medium, R.drawable.ic_medium),
    IMPORTANT(R.string.important,R.drawable.ic_important);

    @StringRes
    private final int title;

    @DrawableRes
    private final int impColorRes;

    Importance(int title, int themeRes) {
        this.title = title;
        this.impColorRes = themeRes;
    }

    public int getColorRes() {
        return impColorRes;
    }
}
