package com.ifair.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.listener.OnNewsClickListener;
import com.ifair.module.MessageResponse;
import com.ifair.myUtil.MyTimeUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 最新消息的Adapter
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private static final String TAG = "NewsAdapter";



    private Context mContext;
    private List<MessageResponse.DataBean> dataBeanList;


    public NewsAdapter(Context mContext, List<MessageResponse.DataBean> dataBeanList) {
        this.mContext = mContext;
        this.dataBeanList = dataBeanList;
    }

    private OnNewsClickListener onNewsClickListener;

    public void setOnNewsClickListener(OnNewsClickListener listener) {
        this.onNewsClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext).load(dataBeanList.get(position).getImg_file()).fitCenter().into(holder.imgProduct); //圖片
        holder.txtDate.setText(MyTimeUtils.timeToString(dataBeanList.get(position).getOn_time())); //日期
        holder.txtTitle.setText(dataBeanList.get(position).getTitle()); //標題
        holder.txtContent.setText(dataBeanList.get(position).getContent()); //內文
        holder.txtLikeNum.setText(dataBeanList.get(position).getLike()); //按讚次數
        if (!dataBeanList.get(position).getSource_content().equals("")) {
            holder.txtSource.setText(mContext.getString(R.string.news_source, dataBeanList.get(position).getSource_content())); //來源
        } else {
            holder.txtSource.setVisibility(View.GONE);
        }


        //如果是true代表有按過
        if (Objects.equals(dataBeanList.get(position).getIs_like(), "true"))
            holder.imgLike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.all_btn_a_thumb_1));
        else
            holder.imgLike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.all_btn_a_thumb_0));

        //如果是true，代表是可以預購的
        if (Objects.equals(dataBeanList.get(position).getIs_buy(), "true"))
            holder.relaItemPreOrders.setVisibility(View.VISIBLE);
        else
            holder.relaItemPreOrders.setVisibility(View.GONE);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    /**
     * 更新Adapter
     */
    public void updateUI(List<MessageResponse.DataBean> dataBeen) {
        dataBeanList.clear();
        dataBeanList.addAll(dataBeen);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_item_preOrder)
        TextView txtItemPreOrder;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindView(R.id.img_like)
        ImageView imgLike;
        @BindView(R.id.txt_like_num)
        TextView txtLikeNum;
        @BindView(R.id.card_news)
        CardView cardNews;
        @BindView(R.id.img_product)
        ImageView imgProduct;
        @BindView(R.id.rela_item_preOrders)
        RelativeLayout relaItemPreOrders;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_source)
        TextView txtSource;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            cardNews.setOnClickListener(this);
            imgLike.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.card_news:
                    if (onNewsClickListener != null) {
                        onNewsClickListener.onItemClick(this.getAdapterPosition());
                    }
                    break;
                case R.id.img_like:
                    if (onNewsClickListener != null) {
                        onNewsClickListener.onLikeClick(this.getAdapterPosition());
                    }
                    break;
            }
        }
    }
}
