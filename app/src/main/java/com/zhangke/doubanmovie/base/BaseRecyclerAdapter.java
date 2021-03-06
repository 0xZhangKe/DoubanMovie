package com.zhangke.doubanmovie.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by 张可 on 2017/8/1.
 */

public abstract class BaseRecyclerAdapter<T extends BaseRecyclerAdapter.ViewHolder, P> extends RecyclerView.Adapter<T> {

    protected Context context;
    protected List<P> listData;
    protected LayoutInflater inflater;
    protected OnRecyclerItemClickListener onItemClickListener;

    public BaseRecyclerAdapter(Context context, List<P> listData) {
        this.context = context;
        this.listData = listData;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseRecyclerAdapter.this.onItemClickListener != null) {
                        onItemClickListener.onItemClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnRecyclerItemClickListener {
        /**
         * @param view       被点击的 view
         * @param position   点击 position
         */
        void onItemClick(View view, int position);
    }
}
