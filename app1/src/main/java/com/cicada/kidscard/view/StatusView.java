package com.cicada.kidscard.view;

import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cicada.kidscard.R;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.storage.db.DBKidsCardHelp;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;
import com.cicada.kidscard.utils.NetworkUtils;
import com.tamsiree.rxtool.RxConstTool;

import java.text.DecimalFormat;

import androidx.annotation.Nullable;

/**
 * 状态栏
 * <p>
 * Create time: 2021/6/8 09:38
 *
 * @author liuyun.
 */
public class StatusView extends LinearLayout {
    private Context mContext;
    private LinearLayout netSpeedLayout;
    private ImageView netCheckIv, netIcon;
    protected TextView uploadDataTv, upTv, downTv;
    protected TextView tv_wifi_name;
    private final DecimalFormat format = new DecimalFormat("0.00");

    public StatusView(Context context) {
        super(context);
        initView(context);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        int layoutId = R.layout.view_status_bar;
        if(AppContext.isIs32Device()){
            layoutId = R.layout.view_status_bar;
        }else if(AppContext.isIsYMDevice()){
            layoutId = R.layout.view_status_bar_ym;
        }
        View rootView = View.inflate(context, layoutId, null);
        netSpeedLayout = rootView.findViewById(R.id.net_speed_layout);
        netCheckIv = rootView.findViewById(R.id.iv_net_status);
        netIcon = rootView.findViewById(R.id.iv_wifi);
        uploadDataTv = rootView.findViewById(R.id.upload_data_tv);
        upTv = rootView.findViewById(R.id.up_tv);
        downTv = rootView.findViewById(R.id.down_tv);
        tv_wifi_name = rootView.findViewById(R.id.tv_wifi_name);
        addView(rootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setNetStatusView(AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE,false));
    }

    /**
     * 设置待上传数据数量
     */
    public void setUploadDataCount() {
        long count = DBKidsCardHelp.getInstance(mContext).findCardRecordCount();
        if (count > 0) {
            uploadDataTv.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            sb.append(count).append("条数据待上传");
            uploadDataTv.setText(sb.toString());
        } else {
            uploadDataTv.setVisibility(View.GONE);
        }
    }

    public void setNetStatusView(boolean netWorkAvailable) {
        if (netWorkAvailable) {
            if (netSpeedLayout.getVisibility() == View.GONE) {
                netSpeedLayout.setVisibility(View.VISIBLE);
                netCheckIv.setImageResource(R.drawable.base_net_green_shape);
                netStatus();
            }
        } else {
            if (netSpeedLayout.getVisibility() == View.VISIBLE) {
                netSpeedLayout.setVisibility(View.GONE);
                netCheckIv.setImageResource(R.drawable.base_net_red_shape);
                tv_wifi_name.setVisibility(View.GONE);
                tv_wifi_name.setText("");
                netStatus();
            }
        }
    }

    private void netStatus() {
        String netType = NetworkUtils.getNetworkType(AppContext.getContext());
        if (netType.equals(NetworkUtils.NET_TYPE_WIFI)) {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            netIcon.setImageResource(R.drawable.wifi_change);
            netIcon.setImageLevel(Math.abs(wifiInfo.getRssi()));
            tv_wifi_name.setVisibility(View.VISIBLE);
            tv_wifi_name.setText(NetworkUtils.getWIFIName(mContext));
        } else if (netType.equals(NetworkUtils.NET_TYPE_ETHERNET)) {
            netIcon.setImageResource(R.drawable.net_eth);
            tv_wifi_name.setText("");
            tv_wifi_name.setVisibility(View.GONE);
        } else if (netType.equals(NetworkUtils.NET_TYPE_MOBILE)) {
            netIcon.setImageResource(R.drawable.net_mobile);
            tv_wifi_name.setText("");
            tv_wifi_name.setVisibility(View.GONE);
        } else {
            tv_wifi_name.setText("");
            tv_wifi_name.setVisibility(View.GONE);
            netIcon.setImageResource(R.drawable.wifi_change);
            netIcon.setImageLevel(200);
        }
    }

    public void updateNetSpeed() {
        try {
            netStatus();
            float upTraffi = Float.parseFloat(TrafficStats.getTotalTxBytes() + "");
            float downTraffi = Float.parseFloat(TrafficStats.getTotalRxBytes() + "");
            float lastUp = BaseSharePreference.getInstance().getCurrentNetSpeedUp();
            float lastDown = BaseSharePreference.getInstance().getCurrentNetSpeedDown();
            float up = Math.abs(upTraffi - lastUp);
            float down = Math.abs(downTraffi - lastDown);
            BaseSharePreference.getInstance().setCurrentNetSpeedUp(upTraffi);
            BaseSharePreference.getInstance().setCurrentNetSpeedDown(downTraffi);
            upTv.setText(totalTxBytes(up));
            downTv.setText(totalTxBytes(down));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 换算网速
     *
     * @param totalTxBytes
     * @return
     */
    private String totalTxBytes(float totalTxBytes) {
        StringBuilder sb = new StringBuilder();
        if (totalTxBytes > RxConstTool.MB) {
            sb.append(format.format(totalTxBytes / RxConstTool.MB)).append("Mb/s");
        } else {
            sb.append(format.format(totalTxBytes / RxConstTool.KB)).append("Kb/s");
        }
        return sb.toString();
    }
}
