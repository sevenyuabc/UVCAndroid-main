package com.cicada.kidscard.view.banner;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cicada.kidscard.R;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import androidx.annotation.NonNull;


/**
 * TODO
 * <p>
 * Create time: 2021/5/13 16:23
 *
 * @author liuyun.
 */
public class BannerAdapter extends BaseBannerAdapter<BannerInfo> {
    private Context context;

    public BannerAdapter() {
    }

    public BannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected void bindData(BaseViewHolder<BannerInfo> holder, BannerInfo data, int position, int pageSize) {
        ImageView imageView = holder.findViewById(R.id.banner_iv);
        if (!TextUtils.isEmpty(data.getMachinePics()) && null != context) {
            String url = data.getMachinePics();
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.ad_1)
                    .error(R.drawable.ad_1)
                    .dontAnimate()
//                    .override(1920,1080)
//                    .skipMemoryCache(true)                          //禁止Glide内存缓存
//            DiskCacheStrategy.ALL：原始图片和转换过的图片都缓存
//            DiskCacheStrategy.RESOURCE:只缓存原始图片
//            DiskCacheStrategy.NONE：不缓存
//            DiskCacheStrategy.DATA：只缓存使用过的图片
//             .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存压缩后的图片
                    .into(imageView);
        } else {
            Glide.with(context).load(data.getResId())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_banner_item;
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<BannerInfo> holder) {
        super.onViewRecycled(holder);
        ImageView imageView = holder.findViewById(R.id.banner_iv);
//        Glide.with(context).clear(imageView);
    }
}
