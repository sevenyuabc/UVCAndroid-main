package com.cicada.kidscard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cicada.kidscard.R;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.ParseTemperature;

import androidx.annotation.Nullable;

/**
 * 体温展示
 * <p>
 * Create time: 2020-02-14 15:50
 *
 * @author liuyun.
 */
public class TemperatureView extends LinearLayout {
    private Context mContext;
    private View rootView;
    private LinearLayout ll_temp;
    private TextView tv_temp;

    public TemperatureView(Context context) {
        super(context);
        initView(context);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        rootView = View.inflate(context, R.layout.view_temperature, null);
        addView(rootView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContext = context;
        ll_temp = (LinearLayout) rootView.findViewById(R.id.temperature_layout);
        tv_temp = (TextView) rootView.findViewById(R.id.temperature_tv);
    }

    /**
     * @param temp 体温数据大于等于35°，小于等于37.5°标记为『正常』
     *             体温数据小于等于35°，或大于等于45°，标记为『异常』
     *             体温数据大于37.5，小于45°，标记为『高温』
     */
    public void setTemperature(String temp) {
        tv_temp.setText(temp + "℃");
        //0-正常 1-高温 -1-异常
        int status = ParseTemperature.getTemperatureStatus(temp);
        if (-1 == status || -2 == status) {
            LogUtils.d("==yy== temp", "异常");
            ll_temp.setBackground(mContext.getResources().getDrawable(R.drawable.temperature_bg_yellow));
        } else if (1 == status) {
            LogUtils.d("==yy== temp", "高温");
            ll_temp.setBackground(mContext.getResources().getDrawable(R.drawable.temperature_bg_red));
        } else {//体温数据大于等于35°，小于等于37.5°标记为『正常』
            LogUtils.d("==yy== temp", "正常");
            ll_temp.setBackground(mContext.getResources().getDrawable(R.drawable.temperature_bg_blue));
        }
    }
}
