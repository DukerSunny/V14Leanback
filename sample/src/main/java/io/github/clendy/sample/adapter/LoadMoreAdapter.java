package io.github.clendy.sample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import io.github.clendy.leanback.utils.AnimUtil;
import io.github.clendy.sample.R;
import io.github.clendy.sample.model.Entity;


/**
 * LoadMoreAdapter
 *
 * @author Clendy
 * @date 2016/11/16 016 15:07
 * @e-mail yc330483161@outlook.com
 */
public class LoadMoreAdapter extends RecyclerView.Adapter<LoadMoreAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_NORMAL = 1;
    private static final int ITEM_VIEW_TYPE_FOOTER = 2;

    private Context mContext;
    private List<Entity> mItems = new ArrayList<>();

    private OnItemClickListener mClickListener;

    private final int[] mImgs;
    private final int[] mColors;

    public void setClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }


    public LoadMoreAdapter(Context context) {
        this.mContext = context;
        mImgs = new int[]{
                R.mipmap.gyy1,
                R.mipmap.gyy2,
                R.mipmap.gyy3,
                R.mipmap.gyy4,
                R.mipmap.gyy5
        };
        mColors = new int[]{
                mContext.getResources().getColor(R.color.colorAccentAlpha),
                mContext.getResources().getColor(R.color.colorPrimaryAlpha)
        };
    }

    public List<Entity> getItems() {
        return mItems;
    }

    public void setItems(List<Entity> items) {
        if (items == null || items.size() == 0) {
            return;
        }

        if (mItems == null) {
            mItems = new ArrayList<>();
        } else {
            mItems.clear();
        }
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<Entity> items) {
        if (items == null || items.size() == 0) {
            return;
        }

        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        int size = items.size();
        int itemsCount = mItems.size();

        mItems.addAll(itemsCount, items);

        notifyItemRangeChanged(itemsCount, size);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, ITEM_VIEW_TYPE_NORMAL);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.viewType == ITEM_VIEW_TYPE_NORMAL) {
            holder.itemView.setTag(position);
            holder.itemView.setFocusable(true);

            if (mItems != null && mItems.size() > 0) {
                final Entity entity = mItems.get(position);
                Glide.with(mContext)
                        .load(mImgs[position % mImgs.length])
                        .asBitmap()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.mImg);
                holder.mTitle.setText(position + "." + entity.getTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickListener != null) {
                            mClickListener.onItemClick(v, holder.getLayoutPosition(), entity);
                        }
                    }
                });

                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            holder.mTitle.setBackgroundColor(mColors[0]);
                            AnimUtil.scaleAnim(v, 1.2f, 1.2f, 300);
                        } else {
                            holder.mTitle.setBackgroundColor(mColors[1]);
                            AnimUtil.scaleAnim(v, 1.0f, 1.0f, 300);
                        }
                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_VIEW_TYPE_NORMAL;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        int viewType;

        ImageView mImg;
        TextView mTitle;

        ViewHolder(final View view, int viewType) {
            super(view);
            this.viewType = viewType;
            mImg = ((ImageView) itemView.findViewById(R.id.img));
            mTitle = ((TextView) itemView.findViewById(R.id.title));
        }
    }
}
