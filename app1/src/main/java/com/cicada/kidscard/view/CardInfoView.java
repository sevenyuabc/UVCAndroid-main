package com.cicada.kidscard.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cicada.kidscard.R;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.utils.Preconditions;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * 卡信息
 * <p>
 * Create time: 2021/6/9 17:42
 *
 * @author liuyun.
 */
public class CardInfoView extends LinearLayout {
    private Context mContext;
    ImageView ivUserIcon;
    TextView nameTv, classNameTv, cardNoTv;
    private CardView cvUserInfo, cvCardInfo;

    public CardInfoView(Context context) {
        super(context);
        initView(context);
    }

    public CardInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CardInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        int layoutId = R.layout.view_card_info;
        if(AppContext.isIsYMDevice()){
            layoutId = R.layout.view_card_info_ym;
        }
        View rootView = View.inflate(context, layoutId, null);
        nameTv = rootView.findViewById(R.id.name_tv);
        classNameTv = rootView.findViewById(R.id.class_name_tv);
        cardNoTv = rootView.findViewById(R.id.cardnumber_tv);
        cvUserInfo = rootView.findViewById(R.id.cv_userinfo);
        cvCardInfo = rootView.findViewById(R.id.cv_cardinfo);
        ivUserIcon = rootView.findViewById(R.id.iv_userIcon);
        addView(rootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 显示卡号
     *
     * @param curCardNumber
     */
    public void showCardNo(String curCardNumber) {
        cardNoTv.setText(curCardNumber);
        cvCardInfo.setVisibility(View.VISIBLE);
        cvUserInfo.setVisibility(View.GONE);
        this.setVisibility(VISIBLE);
    }

    /**
     * 显示用户信息
     *
     * @param name
     * @param className
     * @param userIcon
     */
    public void showUserInfo(String name, String className, String userIcon, Activity activity) {
        if (Preconditions.isNotEmpty(name)) {
            name = name.replaceFirst(name.substring(0, 1), "*");
            nameTv.setText(name);
            nameTv.setVisibility(VISIBLE);
        } else {
            nameTv.setVisibility(GONE);
        }
        if (Preconditions.isNotEmpty(className)) {
            classNameTv.setText(className);
            classNameTv.setVisibility(VISIBLE);
        } else {
            classNameTv.setVisibility(GONE);
        }
        cvUserInfo.setVisibility(View.VISIBLE);
        if (Preconditions.isNotEmpty(userIcon) && null != ivUserIcon && null != activity && !activity.isDestroyed()) {
            Glide.with(mContext)
                    .load(userIcon)
                    .placeholder(R.drawable.default_image_p3)
                    .error(R.drawable.default_image_p3)
                    .dontAnimate()
                    .into(ivUserIcon);
        } else {
            if (null != ivUserIcon) {
                ivUserIcon.setImageResource(R.drawable.default_image_p3);
            }
        }
    }

    /**
     * 清空用户信息
     */
    public void clearCardInfo(Activity activity) {
        nameTv.setText("");
        classNameTv.setText("");
        cardNoTv.setText("");
        cvCardInfo.setVisibility(View.GONE);
        cvUserInfo.setVisibility(View.GONE);
        this.setVisibility(GONE);
        if (null != ivUserIcon && null != activity && !activity.isDestroyed()) {
//            Glide.with(activity).clear(ivUserIcon);
        }
    }
}
