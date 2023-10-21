package com.cicada.kidscard.view.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cicada.kidscard.R;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.utils.JsonUtils;
import com.cicada.kidscard.utils.Preconditions;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.zhpan.bannerview.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * TODO
 * <p>
 * Create time: 2021/6/3 10:29
 *
 * @author liuyun.
 */
public class BannerView extends FrameLayout {
    private Context mContext;
    private BannerViewPager<BannerInfo> bannerViewPager;
    private List<BannerInfo> bannerInfoList = new ArrayList<>();

    public BannerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View rootView = View.inflate(context, R.layout.view_banner_layout, null);
        bannerViewPager = rootView.findViewById(R.id.banner_view);
        addView(rootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void startBanner() {
        setDataList();
        BannerAdapter bannerAdapter = new BannerAdapter(mContext);
        bannerViewPager
                .setInterval(5000)
                .setScrollDuration(500)
                .setIndicatorVisibility(View.GONE)
                .setOffScreenPageLimit(1)
                .setAdapter(bannerAdapter)
                .create(bannerInfoList);
        bannerViewPager.startLoop();
    }

    private void setDataList() {
        String bannerListStr = AppSharedPreferences.getInstance().getStringValue(Constants.ADV_INFO,"");
        if (Preconditions.isEmpty(bannerListStr)) {
            bannerInfoList.clear();
            bannerInfoList.add(new BannerInfo(R.drawable.ad_1));
            bannerInfoList.add(new BannerInfo(R.drawable.ad_2));
            bannerInfoList.add(new BannerInfo(R.drawable.ad_3));
        } else {
            bannerInfoList = JsonUtils.parseArray(bannerListStr, BannerInfo.class);
        }
    }


    public void refreshData() {
        setDataList();
        bannerViewPager.refreshData(bannerInfoList);
    }

    public void stopBanner() {
        bannerViewPager.stopLoop();
    }
}
