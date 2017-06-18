package com.microsoft.quicksearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.outlookassignment.R;
import com.microsoft.quicksearch.model.ResultData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for showing searched images along with title of image.
 * {@link Picasso} is used to load and cache images.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_android;
        ImageView img_android;

        ViewHolder(View view) {
            super(view);
            tv_android = (TextView)view.findViewById(R.id.result_item_title);
            img_android = (ImageView)view.findViewById(R.id.result_item_image);
        }
    }

    private final Context mContext;
    private List<ResultData> items;

    public ResultAdapter(Context context, List<ResultData> listToShow) {
        mContext = context;
        items = listToShow;
    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.result_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultAdapter.ViewHolder holder, int position) {
        holder.tv_android.setText(items.get(position).getTitle());
        Picasso.with(mContext)
                .load(items.get(position).getSource())
                .placeholder(android.R.drawable.ic_delete)
                .fit()
                .into(holder.img_android);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ResultData> data) {
        items = data;
        notifyDataSetChanged();
    }
}
