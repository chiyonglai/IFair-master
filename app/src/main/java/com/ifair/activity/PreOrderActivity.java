package com.ifair.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.adapter.PreOrderContactAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnAddMemberOrderListener;
import com.ifair.listener.OnGetPreOrderDetailListener;
import com.ifair.listener.OnGetPreOrderInformationListener;
import com.ifair.listener.OnSendAddPreOrderInformationListener;
import com.ifair.listener.OnSendEditPreOrderInformationListener;
import com.ifair.module.AddPreOrderInformationResponse;
import com.ifair.module.BaseResponse;
import com.ifair.module.EditPreOrderInformationResponse;
import com.ifair.module.PreOrderContact;
import com.ifair.module.PreOrderResponse;
import com.ifair.module.PreorderInformationResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.Variable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreOrderActivity extends BaseNFcActivity implements OnGetPreOrderDetailListener, OnGetPreOrderInformationListener,
        OnSendAddPreOrderInformationListener, OnSendEditPreOrderInformationListener, OnAddMemberOrderListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_preorder_name)
    TextView txtPreorderName;
    @BindView(R.id.txt_preorder_quantity)
    TextView txtPreorderQuantity;
    @BindView(R.id.txt_preorder_price)
    TextView txtPreorderPrice;
    @BindView(R.id.txt_preorder_total)
    TextView txtPreorderTotal;
    @BindView(R.id.txt_preorder_number)
    TextView txtPreorderNumber;
    @BindView(R.id.linear_information)
    LinearLayout linearInformation;
    @BindView(R.id.edit_preorder_name)
    EditText editPreorderName;
    @BindView(R.id.edit_preorder_phone)
    EditText editPreorderPhone;
    @BindView(R.id.edit_preorder_address)
    EditText editPreorderAddress;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.linear_preorder_detail)
    LinearLayout linearPreorderDetail;
    @BindView(R.id.img_sub)
    ImageView imgSub;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.img_preorder_product)
    RoundedImageView imgPreorderProduct;
    @BindView(R.id.txt_preorder_over)
    TextView txtPreorderOver;
    @BindView(R.id.recycler_view_information)
    RecyclerView recyclerViewInformation;
    @BindView(R.id.activity_pre_order)
    LinearLayout activityPreOrder;

    //商品ID
    private String good_id;
    private List<String> array;
    private PreOrderContactAdapter preOrderContactAdapter;
    private EditText editText;
    private List<PreOrderContact> preOrderContactList;
    //紀錄
    private PreOrderResponse.DataBean preOrderResponse;
    //紀錄預購數目
    private int preOrderNum = 1;
    //廣播接收器
    private NfcBroadCast nfcBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        ButterKnife.bind(this);
        adjustUI();
        initParam();
    }

    /**
     * 廣播接收器
     */
    class NfcBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Variable.Intent_Close_NFC_Activity)) finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nfcBroadCast);
    }

    /**
     * 動態設定畫面
     */
    private void adjustUI() {
        getScreenSize(this);
        ViewGroup.LayoutParams linearPreorderDetailLp = linearPreorderDetail.getLayoutParams();
        linearPreorderDetailLp.width = (int) (width * 0.8);
        linearPreorderDetail.setLayoutParams(linearPreorderDetailLp);

        imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_yellow));
    }

    @Override
    protected void initParam() {
        getLoginResponse();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        good_id = bundle.getString("Goods_id");

        preOrderContactList = new ArrayList<>();

        txtTitle.setText(getResources().getString(R.string.news_preOrder));
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetPreOrderDetailListener(this);
        cloudAPI.getPreOrderDetail(good_id, this);

        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));
    }

    /**
     * 設定資料
     */
    private void setData() {
        preOrderNum = 1; //預購數目

        Glide.with(this).load(preOrderResponse.getGoods_image2()).centerCrop().into(imgPreorderProduct); //圖片小圖
        txtPreorderName.setText(preOrderResponse.getGoods_name()); //商品名稱
        if (preOrderResponse.getPre_amount() != null)
            txtPreorderQuantity.setText(getResources().getString(R.string.magazine_restriction, preOrderResponse.getAmount())); //限購組數
        txtPreorderOver.setText(getString(R.string.preorder_number_over, String.valueOf(preOrderResponse.getPre_amount())));
        txtPreorderPrice.setText(getString(R.string.magazine_price, AppUtil.numberFormat(preOrderResponse.getOriginal_price()))); //原價
        txtPreorderPrice.setPaintFlags(txtPreorderPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //設定刪除線
        txtPreorderTotal.setText(getString(R.string.magazine_price, AppUtil.numberFormat(preOrderResponse.getPrice()))); //總金額

    }


    /**
     * 計算總金額和控制 + - 數量
     *
     * @param type 1 減少 2 新增
     */
    private void setPreOrderNumber(int type) {
        if (type == 1) {
            if (preOrderNum > 1) {
                imgSub.setImageDrawable(getResources().getDrawable(R.drawable.magazine_btn_a_sub_1));
                imgAdd.setImageDrawable(getResources().getDrawable(R.drawable.magazine_btn_a_add_0));
                preOrderNum--;
            }
        } else {
            if (preOrderResponse.getAmount() != null) {
                int limitNum = Integer.parseInt(preOrderResponse.getAmount());
                if (preOrderNum < limitNum) {
                    preOrderNum++;
                    imgSub.setImageDrawable(getResources().getDrawable(R.drawable.magazine_btn_a_sub_0));
                    imgAdd.setImageDrawable(getResources().getDrawable(R.drawable.magazine_btn_a_add_1));
                }
            } else {
                preOrderNum++;
            }
        }
        int total = Integer.parseInt(preOrderResponse.getPrice()) * preOrderNum;
        txtPreorderTotal.setText(getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(total)))); //總金額
        txtPreorderNumber.setText(String.valueOf(preOrderNum));  //預購數
    }

    /**
     * 新增預購
     */
    private void InsertPreOrderAPI() {
        //API
        cloudAPI.setOnAddMemberOrderListener(this);
        cloudAPI.addMemberOrder(AppUtil.getMacAddress(), userEmail, AppUtil.editToString(editPreorderName), AppUtil.editToString(editPreorderPhone),
                AppUtil.editToString(editPreorderAddress), preOrderResponse.getGoods_id(), preOrderResponse.getGoods_name(), preOrderResponse.getPrice(),
                 String.valueOf(preOrderNum),String.valueOf(Integer.valueOf(preOrderResponse.getPrice()) * preOrderNum), this);
    }

    /**
     * 詢問是否要儲存
     */
    private void askPreOrder() {
        if (!userEmail.equals("")) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("");
            ad.setMessage(getResources().getString(R.string.preorder_save_information));
            //儲存
            ad.setNegativeButton(R.string.setting_save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    askPreOrderInformation();
                }
            });
            //不存
            ad.setNeutralButton(R.string.setting_not_save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    InsertPreOrderAPI();
                }
            });
            ad.show();
        } else {
            InsertPreOrderAPI();
        }
    }

    /**
     * 預購資訊訊問
     */
    private void askPreOrderInformation() {
        final View item = LayoutInflater.from(PreOrderActivity.this).inflate(R.layout.alertdialog_item, null);
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("");
        ad.setView(item);
        ad.setMessage(getResources().getString(R.string.preorder_defined_name));
        //儲存
        ad.setNegativeButton(R.string.setting_save, (dialog12, which) -> {
            editText = (EditText) item.findViewById(R.id.edittext);
            hideSoftInput();
            if (!AppUtil.editToString(editText).equals(""))
                setPreOrderInformation();
            else
                Toast.makeText(PreOrderActivity.this, getString(R.string.alert_enter_content, "名稱"), Toast.LENGTH_SHORT).show();
        });
        //不存
        ad.setNeutralButton(R.string.setting_not_save, (dialog1, which) -> InsertPreOrderAPI());
        ad.show();

    }

    /**
     * 設定預購資訊
     */
    private void setPreOrderInformation() {
        hideSoftInput();
        if (array != null && array.size() == 3) {
            //更新
            cloudAPI = CloudAPI.getInstance();
            cloudAPI.setOnSendEditPreOrderInformationListener(this);
            cloudAPI.setEditProOrderInformation(preOrderResponse.getGoods_id(), userEmail, editPreorderName.getText().toString(), editText.getText().toString(), editPreorderPhone.getText().toString(), editPreorderAddress.getText().toString(), this);
        } else {
            //新增
            cloudAPI = CloudAPI.getInstance();
            cloudAPI.setOnSendAddPreOrderInformationListener(this);
            cloudAPI.setAddProOrderInformation(userEmail, editPreorderName.getText().toString(), editText.getText().toString(), editPreorderPhone.getText().toString(), editPreorderAddress.getText().toString(), this);
        }
    }

    /**
     * 顯示聯絡人
     */
    private void showPreOrderContact() {
        linearInformation.setVisibility(View.VISIBLE);
        preOrderContactAdapter = new PreOrderContactAdapter(this, preOrderContactList);
        recyclerViewInformation.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInformation.setAdapter(preOrderContactAdapter);
        //預設顯示第0個
        showClickContact(preOrderContactList.get(0), 0);
        preOrderContactAdapter.setOnPreOrderClickListener(position -> {
            showClickContact(preOrderContactList.get(position), position);
        });
    }

    /**
     * 顯示聯絡人
     *
     * @param preOrderContact 聯絡人內容
     */
    private void showClickContact(PreOrderContact preOrderContact, int position) {
        for (PreOrderContact preOrder: preOrderContactList) {
            preOrder.setIscheck(false);
        }
        preOrderContactList.get(position).setIscheck(true);

        editPreorderName.setText(preOrderContact.getName());
        editPreorderPhone.setText(preOrderContact.getPhone());
        editPreorderAddress.setText(preOrderContact.getAddress());

        preOrderContactAdapter.notifyDataSetChanged();
    }

    //-----------------------------------API-------------------------------------------------------//

    @Override
    public void getPreOrderDetailMessage(PreOrderResponse preOrderResponse, String error) {
        if (preOrderResponse != null) {
            if (preOrderResponse.getCode() == CloudCode.Code_SUCCESS) {
                this.preOrderResponse = preOrderResponse.getData().get(0);
                setData();
                array = new ArrayList<>();
                //記錄會員的EMIAL
                if (!userEmail.equals("")) {
                    cloudAPI = CloudAPI.getInstance();
                    cloudAPI.setOnGetPreOrderInformationListener(this);
                    cloudAPI.getProOrderInformation(userEmail, this);
                }
            }
        }
    }


    @Override
    public void getPreOrderInformationMessage(PreorderInformationResponse preorderInformationResponse, String Error) {
        if (preorderInformationResponse != null) {
            if (preorderInformationResponse.getCode() == CloudCode.Code_SUCCESS) {
                preOrderContactList.clear();
                if (preorderInformationResponse.getData().size() > 0) {
                    for (int i = 0; i < preorderInformationResponse.getData().size(); i++) {
                        PreOrderContact preOrderContact = new PreOrderContact(
                                preorderInformationResponse.getData().get(i).getOrder_address_id(),
                                preorderInformationResponse.getData().get(i).getEmail(),
                                preorderInformationResponse.getData().get(i).getType(),
                                preorderInformationResponse.getData().get(i).getName(),
                                preorderInformationResponse.getData().get(i).getPhone(),
                                preorderInformationResponse.getData().get(i).getAddress(),
                                false);
                        preOrderContactList.add(i, preOrderContact);
                    }
                    showPreOrderContact();
                }
            }
        }
    }

    /**
     * 新增資訊
     */
    @Override
    public void setAddPreOrderInformationMessage(AddPreOrderInformationResponse addPreOrderInformationResponse, String Error) {
        if (addPreOrderInformationResponse != null && addPreOrderInformationResponse.getCode() == CloudCode.Code_SUCCESS) {
            InsertPreOrderAPI();
        }
    }

    /**
     * 更新資訊
     */
    @Override
    public void setEditPreOrderInformationMessage(EditPreOrderInformationResponse editPreOrderInformationResponse, String Error) {
        if (editPreOrderInformationResponse != null && editPreOrderInformationResponse.getCode() == CloudCode.Code_SUCCESS) {
            InsertPreOrderAPI();
        }
    }

    @Override
    public void addMemberOrderResponse(BaseResponse baseResponse, String error) {
        if (error.equals("")) {
            if (baseResponse.getCode() == CloudCode.Code_SUCCESS) {
                //預購成功
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("");
                ad.setMessage(getString(R.string.preorder_success));
                ad.setNegativeButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                ad.show();
            } else {
                alert(baseResponse.getMessage());
            }
        } else {
            alert(error);
        }
    }

    //-----------------------------------API-------------------------------------//


    @OnClick({R.id.img_sub, R.id.img_add, R.id.img_back, R.id.btn_send, R.id.activity_pre_order})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.img_sub:
                setPreOrderNumber(1);
                break;
            case R.id.img_add:
                setPreOrderNumber(2);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_send:
                if (AppUtil.editToString(editPreorderName).equals("")) {
                    alert(getString(R.string.alert_enter_content, getString(R.string.pre_order_name)));
                } else if (AppUtil.editToString(editPreorderPhone).equals("")) {
                    alert(getString(R.string.alert_enter_content, getString(R.string.pre_order_phone)));
                } else if (AppUtil.editToString(editPreorderAddress).equals("")) {
                    alert(getString(R.string.alert_enter_content, getString(R.string.pre_order_address)));
                } else {
                    askPreOrder();
                }
                break;
            case R.id.activity_pre_order:
                hideSoftInput();
                break;
        }
    }
}
