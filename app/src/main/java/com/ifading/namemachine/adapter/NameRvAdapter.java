package com.ifading.namemachine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifading.namemachine.R;
import com.ifading.namemachine.db.NameBean;
import com.ifading.namemachine.db.NameNoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjingsheng on 17/10/20.
 */

public class NameRvAdapter extends RecyclerView.Adapter {
    private List<NameBean> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NameNoteViewHolder(View.inflate(parent.getContext(), R.layout.item_name_note, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NameNoteViewHolder) holder).setName(mData.get(position).getName());
    }

    private class NameNoteViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;

        NameNoteViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.item_name_note_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position < 0) {
                        return;
                    }
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position < 0) {
                        return true;
                    }
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemLongClick(position);
                    }
                    return true;
                }
            });
        }

        public void setName(String name) {
            this.mTvName.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<NameBean> data) {
        this.mData = data;
    }

    public interface OnItemClickListener {
        void onItemClick(int positon);

        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
