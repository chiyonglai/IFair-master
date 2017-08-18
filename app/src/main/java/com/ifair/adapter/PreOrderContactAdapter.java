package com.ifair.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifair.R;
import com.ifair.listener.OnPreOrderClickListener;
import com.ifair.module.PreOrderContact;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能
 */

public class PreOrderContactAdapter extends RecyclerView.Adapter<PreOrderContactAdapter.MyViewHolder>  {

    private Context mContext;
    private List<PreOrderContact> preOrderContactList;

    private OnPreOrderClickListener onPreOrderClickListener;

    public void setOnPreOrderClickListener(OnPreOrderClickListener listener) {
        this.onPreOrderClickListener = listener;
    }

    public PreOrderContactAdapter(Context mContext, List<PreOrderContact> preOrderContactList) {
        this.mContext = mContext;
        this.preOrderContactList = preOrderContactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preorder_information, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtName.setText(preOrderContactList.get(position).getType());

        if (preOrderContactList.get(position).ischeck()) {
            holder.imgIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
        } else {
            holder.imgIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
        }
    }

    @Override
    public int getItemCount() {
        return preOrderContactList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.txt_name)
        TextView txtName;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            imgIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onPreOrderClickListener != null) onPreOrderClickListener.onItemClick(this.getAdapterPosition());
        }
    }
}
