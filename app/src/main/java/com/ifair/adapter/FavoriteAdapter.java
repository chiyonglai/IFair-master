package com.ifair.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.listener.OnMagazineClickListener;
import com.ifair.module.MagazineResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyTimeUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fiona on 2017/4/14.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<MagazineResponse.DataBean> dataBeanList;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private Context context;
    private OnMagazineClickListener onMagazineClickListener;

    public void setOnMagazineClickListener(OnMagazineClickListener onMagazineClickListener) {
        this.onMagazineClickListener = onMagazineClickListener;
    }

    public FavoriteAdapter(List<MagazineResponse.DataBean> dataBeanList, Context context) {
        this.dataBeanList = dataBeanList;
        this.context = context;
    }

    public void setDataList(List<MagazineResponse.DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_magazine, parent, false);
            return new RecycleHolder(view);



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("test", "onBindViewHolder=" + position);
        position=position+1;
        if (holder instanceof RecycleHolder) {
            Glide.with(context).load(dataBeanList.get(position - 1).getGoods_image()).fitCenter().into(((RecycleHolder) holder).image);
            ((RecycleHolder) holder).tvGoodsName.setText(dataBeanList.get(position - 1).getGoods_name());
            //如果是true，代表是可以預購的
            if (Objects.equals(dataBeanList.get(position-1).getIs_preorder(), "true"))
                ((RecycleHolder) holder).relaItemPreOrders.setVisibility(View.VISIBLE);
            else
                ((RecycleHolder) holder).relaItemPreOrders.setVisibility(View.GONE);

            int index = position % 7;
            if (index == 0 || index == 4 || index == 5 || index == 6) {
                ((RecycleHolder) holder).linePrice.setVisibility(View.VISIBLE);
                ((RecycleHolder) holder).tvGoodsPriceYellow.setVisibility(View.VISIBLE);
                ((RecycleHolder) holder).tvGoodsPrice.setText(context.getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(dataBeanList.get(position - 1).getOriginal_price())))));
                ((RecycleHolder) holder).tvGoodsPrice.setTextColor(context.getResources().getColor(R.color.color_line));
                ((RecycleHolder) holder).tvGoodsPriceYellow.setText(context.getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(dataBeanList.get(position - 1).getPrice())))));
                ((RecycleHolder) holder).tvGoodsPreDate.setVisibility(View.GONE);

            } else {
                int[] num = MyTimeUtils.returnPreOrderReciprocal(dataBeanList.get(position - 1).getPre_date_e(), new Date(System.currentTimeMillis()));
                String goodsPreDate1 = "<font color='#f6b300'><b><big>" + String.valueOf(num[0]) + "</big></b></font>";
                String goodsPreDate2 = "<font color='#f6b300'><b>" + String.valueOf(num[1]) + "</b></font>";
                String goodsPreDate3 = "<font color='#f6b300'><b>" + String.valueOf(num[2]) + "</b></font>";
                ((RecycleHolder) holder).tvGoodsPreDate.setText(Html.fromHtml(context.getString(R.string.magazine_time, goodsPreDate1, goodsPreDate2, goodsPreDate3)));
                ((RecycleHolder) holder).tvGoodsPrice.setText(context.getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(dataBeanList.get(position - 1).getPrice())))));
                ((RecycleHolder) holder).tvGoodsPrice.setTextColor(context.getResources().getColor(R.color.color_text_black));
                ((RecycleHolder) holder).linePrice.setVisibility(View.GONE);
                ((RecycleHolder) holder).tvGoodsPriceYellow.setVisibility(View.GONE);
                ((RecycleHolder) holder).tvGoodsPreDate.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return  dataBeanList.size();
    }


    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.card_magazine)
        CardView cardMagazine;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_goods_pre_date)
        TextView tvGoodsPreDate;
        @BindView(R.id.line_price)
        View linePrice;
        @BindView(R.id.tv_goods_price_yellow)
        TextView tvGoodsPriceYellow;
        @BindView(R.id.rela_item_preOrders)
        RelativeLayout relaItemPreOrders;

        public RecycleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onMagazineClickListener != null) {
                onMagazineClickListener.onItemClick(this.getAdapterPosition());
            }
        }

    }

}