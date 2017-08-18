package com.ifair.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnSendGoodsForgedListener;
import com.ifair.module.BaseResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyFileUtil;
import com.ifair.myUtil.Variable;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * NFC 假品，提報假貨
 */
public class ProductSubmitActivity extends BaseNFcActivity implements OnSendGoodsForgedListener{

    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_camera)
    ImageView imgCamera;
    @BindView(R.id.btn_up_file)
    Button btnUpFile;
    @BindView(R.id.edt_where_goods)
    EditText edtWhereGoods;
    @BindView(R.id.edt_explanation)
    EditText edtExplanation;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.activity_product_submit)
    LinearLayout activityProductSubmit;
    @BindView(R.id.rela_clear)
    RelativeLayout relaClear;

    //相簿請求Code
    private static final int IMAGE_REQUEST_CODE = 100;
    //照片請求Code
    private static final int CAMERA_REQUEST_CODE = 200;

    //壓縮後檔案
    private File compressorImageFile;
    //壓縮後檔案路徑
    private String compressorImagePath = "";
    //要開啟的類別
    private int intentType;
    //廣播接收器
    private NfcBroadCast nfcBroadCast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_submit);
        ButterKnife.bind(this);

        adjustUI();
        initParam();
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        getScreenSize(this);
        LinearLayout.LayoutParams btnSendLp = (LinearLayout.LayoutParams) btnSend.getLayoutParams();
        btnSendLp.width = (int) (width * 0.8);
        btnSend.setLayoutParams(btnSendLp);

        RelativeLayout.LayoutParams btnUpFileLp = (RelativeLayout.LayoutParams) btnUpFile.getLayoutParams();
        btnUpFileLp.setMargins(0, (int) (height * 0.32), 0, 0);
        btnUpFile.setLayoutParams(btnUpFileLp);

        RelativeLayout.LayoutParams imgCameraLp = (RelativeLayout.LayoutParams) imgCamera.getLayoutParams();
        imgCameraLp.setMargins(0, (int) (height * 0.17), 0, 0);
        imgCamera.setLayoutParams(imgCameraLp);

        RelativeLayout.LayoutParams relaClearLp = (RelativeLayout.LayoutParams) relaClear.getLayoutParams();
        relaClearLp.setMargins(0, (int) (height * 0.1), 0, 0);
        relaClear.setLayoutParams(relaClearLp);
    }

    @Override
    protected void initParam() {
        txtTitle.setText(getString(R.string.nfc_submit_info));
        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));
        imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_yellow));
    }

    /**
     * 確認要開啟的
     * @param type
     */
    private void intentType(int type) {
        intentType = type;
        //先確認權現是否有開啟
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(activityProductSubmit, "需要給予權限", Snackbar.LENGTH_INDEFINITE)
                        .setAction("確認", view1 -> {
                            ActivityCompat.requestPermissions(this, File_Permission, PREMISSIONSFileRequest);
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, File_Permission, PREMISSIONSFileRequest);
            }
        } else {
            if (type == IMAGE_REQUEST_CODE) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            } else {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri cameraUri = FileProvider.getUriForFile(this, "com.ifair", MyFileUtil.getOutputMediaFile(1)); //
                AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).putString("file_path", MyFileUtil.getOutputMediaFile(1).getAbsolutePath()).commit();
                camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //新增存取Uri的權限
                camera.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri); //將拍照的照片存到指定路徑
                startActivityForResult(camera, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PREMISSIONSFileRequest &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            intentType(intentType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File actualImageFile = null;
            if (requestCode == IMAGE_REQUEST_CODE) {
                try {
                    actualImageFile = FileUtil.from(this, data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST_CODE){
                String file_path = AppUtil.getSharedPref(this, AppUtil.SHARED_PREF_NAME).getString("file_path", "");
                actualImageFile = new File(file_path);
            }
            if (actualImageFile != null) {
                compressorImageFile = new Compressor.Builder(this)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.PNG)
                        .build()
                        .compressToFile(actualImageFile);
                Glide.with(this).load(compressorImageFile).into(imgPicture);
                compressorImagePath = compressorImageFile.getPath();
                Logger.d(compressorImageFile.getPath());
                //當顯示照片時，需隱藏
                imgCamera.setVisibility(View.GONE);
                btnUpFile.setVisibility(View.GONE);
                relaClear.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 確認資料是否填寫
     * @return
     */
    private boolean checkData() {
        if (Objects.equals(compressorImagePath, "")) {
            alert("請上傳圖片");
            return false;
        } else if (Objects.equals(getWhereGoods(), "")) {
            alert("請填入地點");
            return false;
        } else if (Objects.equals(getExplanation(), "")) {
            alert("請填入說明");
            return false;
        }

        return true;
    }

    public String getWhereGoods() {
        return edtWhereGoods.getText().toString();
    }

    public String getExplanation() {
        return edtExplanation.getText().toString();
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
        if (nfcBroadCast != null) unregisterReceiver(nfcBroadCast);
    }


    //--------------------------------------API----------------------------------//

    @Override
    public void onSendGoodsForgedMessage(BaseResponse baseResponse, String error) {
        if (Objects.equals(error, "")) {
            if (baseResponse.getCode() == CloudCode.Code_SUCCESS) {
                runOnUiThread(() -> {
                    Toast.makeText(ProductSubmitActivity.this, getString(R.string.nfc_submit_success), Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }
    }

    //--------------------------------------API----------------------------------//

    @OnClick({R.id.img_back, R.id.img_camera, R.id.btn_up_file, R.id.btn_send, R.id.rela_clear})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_camera:
                intentType(CAMERA_REQUEST_CODE);
                break;
            case R.id.btn_up_file:
                intentType(IMAGE_REQUEST_CODE);
                break;
            case R.id.btn_send:
                if (checkData()) {
                    getLoginResponse();
                    cloudAPI = CloudAPI.getInstance();
                    cloudAPI.setOnSendGoodsForgedListener(this);
                    cloudAPI.sendGoodsForged(userEmail, compressorImagePath, getWhereGoods(), getExplanation(), this);
                }
                break;
            case R.id.rela_clear:
                if (compressorImageFile.exists()) {
                    compressorImageFile.delete();
                }
                compressorImagePath = "";
                imgPicture.setImageResource(0);
                imgPicture.setBackgroundResource(R.color.color_nfc_img_background);
                imgCamera.setVisibility(View.VISIBLE);
                btnUpFile.setVisibility(View.VISIBLE);
                relaClear.setVisibility(View.GONE);
                break;
        }
    }
}
