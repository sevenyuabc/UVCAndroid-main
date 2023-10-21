package com.cicada.kidscard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cicada.kidscard.R;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;
import com.tamsiree.rxtool.RxDeviceTool;

import androidx.annotation.Nullable;

/**
 * 卡信息
 * <p>
 * Create time: 2021/6/9 17:42
 *
 * @author liuyun.
 */
public class TimeInfoView extends LinearLayout {
    private Context mContext;
    TextView tvVersion, tvSn;

    public TimeInfoView(Context context) {
        super(context);
        initView(context);
    }

    public TimeInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimeInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        int layoutId =  R.layout.layout_time;
        View rootView = View.inflate(context, layoutId, null);
        tvVersion = rootView.findViewById(R.id.tv_version);
        tvSn = rootView.findViewById(R.id.tv_sn);
        addView(rootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvVersion.setText("V" + RxDeviceTool.getAppVersionName(mContext));
        tvSn.setText(mContext.getString(R.string.device_text) + BaseSharePreference.getInstance().getSn());
    }
}
