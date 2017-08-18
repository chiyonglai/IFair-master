package com.ifair.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.listener.OnMagazineClickListener;
import com.ifair.module.MagazineResponse;
import com.ifair.module.MessageResponse;
import com.ifair.myUtil.MyTimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private View headView;
    private List<MagazineResponse.DataBean> dataBeanList;

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private OnMagazineClickListener onMagazineClickListener;

    public void setOnMagazineClickListener(OnMagazineClickListener onMagazineClickListener) {
        this.onMagazineClickListener = onMagazineClickListener;
    }

    public ProductListAdapter(Context mContext, View headView, List<MagazineResponse.DataBean> dataBeanList) {
        this.mContext = mContext;
        this.headView = headView;
        this.dataBeanList = dataBeanList;
    }

    /**
      * 建立ITEM畫面
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(headView);
        } else if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_favorite, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            if (dataBeanList.size() > 0) {

                Glide.with(mContext).load(dataBeanList.get(position - 1).getGoods_image()).fitCenter().into(((ViewHolder) holder).imgFavorite); //圖片
                ((ViewHolder) holder).txtFavoriteTitle.setText(dataBeanList.get(position - 1).getGoods_name()); //標題
                ((ViewHolder) holder).txtFavoriteData.setText(dataBeanList.get(position - 1).getIntroduction()); //資訊

                ((ViewHolder) holder).txtNewProduct.setVisibility(View.INVISIBLE); // 7天內上架
                if (MyTimeUtils.returnDays(dataBeanList.get(position - 1).getUp_date(), 7))
                    ((ViewHolder) holder).txtNewProduct.setVisibility(View.VISIBLE);

                ((ViewHolder) holder).txtPreOrdersProduct.setVisibility(View.INVISIBLE);// 有預購
                if (Objects.equals(dataBeanList.get(position - 1).getIs_preorder(), "true"))
                    ((ViewHolder) holder).txtPreOrdersProduct.setVisibility(View.VISIBLE);
            }
//
//            int txt_width = ((ViewHolder) holder).txtFavoriteData.getWidth();
//            ((ViewHolder) holder).txtFavoriteData.setMaxWidth(txt_width);
            //判斷簡介資訊是否有超過兩行
//            ((ViewHolder) holder).txtFavoriteData.post(() -> {
//                Boolean bol = true;
//                int lineCount = ((ViewHolder) holder).txtFavoriteData.getLineCount();
//                Paint paint = new Paint();
//                paint.setTextSize(((ViewHolder) holder).txtFavoriteData.getTextSize());
//                final float size = paint.measureText(((ViewHolder) holder).txtFavoriteData.getText().toString()) / 36; //一個字元為42，計算輸入多少字
//                int num = (int) Math.floor((double) ((ViewHolder) holder).txtFavoriteData.getWidth() / 36) * 2;//計算輸入框一行可以輸入多少字元 ，行數限二行所以乘於2
////
////                if ((int) size > num) {
////                    ((ViewHolder) holder).txtFavoriteDataMore.setVisibility(View.VISIBLE);     //超過就顯示「更多」顯示
////                } else
////                    ((ViewHolder) holder).txtFavoriteDataMore.setVisibility(View.GONE);   //沒超過就不顯示「更多」
//            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }


    /**
     * 更新Adapter
     *
     * @param dataBeen
     */
    public void updateUI(List<MagazineResponse.DataBean> dataBeen) {
        if (dataBeen != null) {
            dataBeanList.clear();
            dataBeanList.addAll(dataBeen);
            this.notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return headView == null ? dataBeanList.size() : dataBeanList.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.img_favorite)
        ImageView imgFavorite;
        @BindView(R.id.txt_new_product)
        TextView txtNewProduct;
        @BindView(R.id.txt_pre_orders_product)
        TextView txtPreOrdersProduct;
        @BindView(R.id.txt_favorite_title)
        TextView txtFavoriteTitle;
        @BindView(R.id.txt_favorite_data)
        TextView txtFavoriteData;
        @BindView(R.id.txt_favorite_data_more)
        TextView txtFavoriteDataMore;
        @BindView(R.id.linear_data)
        LinearLayout linearData;
        @BindView(R.id.card_view)
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onMagazineClickListener != null) {
                onMagazineClickListener.onItemClick(this.getAdapterPosition());
            }
        }
    }
}
