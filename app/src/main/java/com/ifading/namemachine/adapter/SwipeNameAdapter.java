package com.ifading.namemachine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifading.namemachine.R;


import java.util.ArrayList;

/**
 * 滑动名字卡片的adpter
 *
 * Created by yangjingsheng on 17/10/14.
 */

public class SwipeNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> mDatas = new ArrayList<>();

    public void setData(ArrayList<String> data) {
        mDatas = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NameViewHolder) holder).mTvName.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private class NameViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;

        NameViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.item_name_tv_name);
        }
    }
}
