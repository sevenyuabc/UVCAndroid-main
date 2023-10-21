package com.cicada.kidscard.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cicada.kidscard.app.AppManager;
import com.cicada.kidscard.R;
import com.cicada.kidscard.config.AppContext;
import com.tamsiree.rxtool.RxDeviceTool;
import com.tamsiree.rxtool.RxImageTool;

import java.lang.ref.WeakReference;

public class ToastUtils {

    public static void toastShort(Context context, String text) {
        toastShort(text);
    }

    public static void toastShort(String text) {
        ToastShowManager.getInstance().showToastShort(AppContext.getContext(), text);
    }

    public static void toastShort(int resId) {
        ToastShowManager.getInstance().showToastShort(AppContext.getContext(), resId);
    }

    public static void toastLong(String text) {
        ToastShowManager.getInstance().showToastLong(AppContext.getContext(), text);
    }

    public static void toastLong(int resId) {
        ToastShowManager.getInstance().showToastLong(AppContext.getContext(), resId);
    }

    public static void toast(String text, int duration) {
        ToastShowManager.getInstance().showToast(AppContext.getContext(), text, duration);
    }

    public static void toast(int resId, int duration) {
        String text = AppContext.getContext().getResources().getString(resId);
        ToastShowManager.getInstance().showToast(AppContext.getContext(), text, duration);
    }

    private static Toast toastPublic;
    private static TextView textViewToast;

    /**
     * 关闭所有的提示Toast信息框
     *
     * @author xnjiang
     * @since v0.0.1
     */
    public static void hideToastImage() {
        if (toastPublic != null) {
            toastPublic.cancel();
        }
    }

    /**
     * 显示带文字和图片的Toast
     *
     * @param context
     * @param resid   0 不显示图片
     */
    public static void showToastImage(Context context, String strMssage, int resid) {
        showToastImage(strMssage, resid);
    }

    /**
     * 显示带文字和图片的Toast
     *
     * @param strMessage
     * @param resid
     */
    public static void showToastImage(String strMessage, int resid) {
        Context context = AppContext.getContext();
        if (!AppManager.isBackground()) {
            textViewToast = new TextView(context);
            textViewToast.setBackgroundResource(R.drawable.toast_bg);
            int intSet = RxImageTool.dp2px(30);
            int intDecrease = RxImageTool.dp2px(0);
            textViewToast.setPadding(intSet, intSet - intDecrease, intSet, intSet - intDecrease);
            int width = (RxDeviceTool.getScreenWidths(context) * 3) / 4;
            int height = width / 2;
            textViewToast.setWidth(width);
            textViewToast.setHeight(height);
            textViewToast.setGravity(Gravity.CENTER);
            textViewToast.setTextSize(RxImageTool.sp2px(context.getResources().getDimension(R.dimen.text_size_medium)));
            textViewToast.setTextColor(Color.WHITE);
            textViewToast.setLineSpacing(1.0f, 1.0f);
            textViewToast.setCompoundDrawablesWithIntrinsicBounds(0, resid, 0, 0);
            textViewToast.setText(strMessage);
            if (toastPublic == null) {
                toastPublic = new Toast(context);
                toastPublic.setGravity(Gravity.CENTER, 0, 0);
                toastPublic.setDuration(Toast.LENGTH_SHORT);
            }
            toastPublic.setView(textViewToast);
            toastPublic.show();
        }
    }


    public static void cancel() {
        if (null != toastPublic) {
            toastPublic.cancel();
        }
    }


    /**
     * 底部显示toast
     *
     * @param context
     * @param strMessage
     */
    public static void showToastBottom(Context context, String strMessage) {
        if (!AppManager.isBackground()) {
            if (textViewToast == null) {
                textViewToast = new TextView(context);
                textViewToast.setBackgroundResource(R.drawable.toast_bg);
                int intSet = 50;
                textViewToast.setPadding(intSet, intSet - 30, intSet, intSet - 30);
                textViewToast.setLayoutParams(new ViewGroup.LayoutParams(RxImageTool.dp2px(100), ViewGroup.LayoutParams.WRAP_CONTENT));
                textViewToast.setGravity(Gravity.CENTER);
                textViewToast.setTextSize(18f);
                textViewToast.setTextColor(Color.WHITE);
                textViewToast.setLineSpacing(1.0f, 1.0f);
            }
            textViewToast.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            textViewToast.setText(strMessage);
            if (toastPublic == null) {
                toastPublic = new Toast(context);
            }
            toastPublic.setGravity(Gravity.BOTTOM, 0, 120);
            toastPublic.setDuration(Toast.LENGTH_SHORT);
            toastPublic.setView(textViewToast);
            toastPublic.show();
        }
    }

    /**
     * 根据布局显示toast
     *
     * @param context
     * @param view
     */
    public static void showToasByView(Context context, View view) {
        if (!AppManager.isBackground()) {
            if (toastPublic == null) {
                toastPublic = new Toast(context);
            }
            toastPublic.setGravity(Gravity.CENTER, 0, 0);
            toastPublic.setDuration(Toast.LENGTH_SHORT);
            toastPublic.setView(view);
            toastPublic.show();
        }
    }


    private static class ToastShowManager {

        private static final String TAG = ToastShowManager.class.getSimpleName();

        private static volatile ToastShowManager mInstance = null;
        private ToastInfo mPreShowToastInfo;
        /**
         * 两个相同的Toast显示的时间间隔,以毫秒为单位.
         */
        private final long mShowSameToastInterval = 2500;

        public ToastShowManager() {
        }

        public static ToastShowManager getInstance() {
            if (mInstance == null) {
                synchronized (ToastShowManager.class) {
                    if (mInstance == null) {
                        mInstance = new ToastShowManager();
                    }
                }
            }
            return mInstance;
        }

        public void showToastShort(Context context, int resId) {
            if (context != null) {
                String text = context.getString(resId);
                showToastShort(context, text);
            }
        }

        public void showToastLong(Context context, int resId) {
            if (context != null) {
                String text = context.getString(resId);
                showToastLong(context, text);
            }
        }

        public void showToastShort(Context context, String text) {
            showToast(context, text, Toast.LENGTH_SHORT);
        }

        public void showToastLong(Context context, String text) {
            showToast(context, text, Toast.LENGTH_LONG);
        }

        public void showToast(Context context, String text, int duration) {
            if (!AppManager.isBackground()) {
                if (mPreShowToastInfo == null) {
                    updateToastInfoAndShowToast(context, text, duration);
                } else {
                    if (!TextUtils.isEmpty(mPreShowToastInfo.text) && mPreShowToastInfo.text.equals(text)) {
                        /*
                         * 当接下来要显示的Toast的文本与先前的相同时,按照以下规则进行显示. #1>
                         * 如果两个Toast之间的时间间隔大于预定义的时间间隔,则显示第二个Toast. #2>
                         * 如果两个Toast的Context不相同时,则显示第二个Toast. #3>
                         * 如果不满足1,2则只更新时间.
                         */
                        final Context preToastContext = mPreShowToastInfo.contextRef.get();
                        if ((System.currentTimeMillis() - mPreShowToastInfo.showTime > mShowSameToastInterval) || (preToastContext != context)) {
                            updateToastInfoAndShowToast(context, null, duration);
                        } else {
                            // 仅仅只更新保存的Toast的时间.
                            mPreShowToastInfo.showTime = System.currentTimeMillis();
                        }
                    } else {
                        updateToastInfoAndShowToast(context, text, duration);
                    }
                }
            }
        }

        /**
         * 更新保存的Toast信息,然后显示Toast.
         *
         * @param newContext
         * @param newText
         * @param duration
         */
        private void updateToastInfoAndShowToast(Context newContext, String newText, int duration) {
            if (mPreShowToastInfo == null) {
                mPreShowToastInfo = new ToastInfo();
            }

            if (newText != null) {
                mPreShowToastInfo.text = newText;
            }

            if (newContext != null) {
                mPreShowToastInfo.contextRef = new WeakReference<Context>(newContext);
            }

            Toast.makeText(newContext, mPreShowToastInfo.text, duration).show();
            mPreShowToastInfo.showTime = System.currentTimeMillis();
        }

        private static class ToastInfo {
            WeakReference<Context> contextRef;
            String text;
            long showTime;
        }
    }

}
