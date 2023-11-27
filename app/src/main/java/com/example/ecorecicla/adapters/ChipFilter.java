package com.example.ecorecicla.adapters;

import android.content.Context;
import android.util.AttributeSet;

import com.example.ecorecicla.R;
import com.google.android.material.chip.Chip;

public class ChipFilter extends Chip {

    private FilterClickListener filterClickListener;

    public ChipFilter(Context context) {
        super(context);
        init();
    }

    public ChipFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChipFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(v -> {
            if (filterClickListener != null) {
                filterClickListener.onChipClick(this);
            }
        });
    }

    public void setFilterClickListener(FilterClickListener filterClickListener) {
        this.filterClickListener = filterClickListener;
    }

    public interface FilterClickListener {
        void onChipClick(ChipFilter chipFilter);
    }
}
