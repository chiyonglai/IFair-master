package com.ifair.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.ifair.R;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.v4.app.ActivityCompat;
import com.ifair.MyApplication;
import android.support.design.widget.Snackbar;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.eventModule.NfcEvent;
import com.ifair.listener.OnNFCSendTagIdListener;
import com.ifair.listener.OnSendVerifyListener;
import com.ifair.module.NFCBaseResponse;
import com.ifair.module.NFCVerifyResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.LocationUtil;
import com.ifair.myUtil.Variable;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Objects;

/**
 * 功能
 */

public class BaseNFcActivity extends BaseActivity implements OnNFCSendTagIdListener, OnSendVerifyListener {

    private static final String TAG = "BaseNFcActivity";

    //Tag ID
    private String strTag = "";
    //區域
    private String strArea = "";

    private MyApplication myApplication;
    //廣播接收器
    private BaseNfcReceive baseNfcReceive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        myApplication.setStartScan(true);
        initAPI();
        scanNfc();

        baseNfcReceive = new BaseNfcReceive();
        registerReceiver(baseNfcReceive, new IntentFilter(Variable.Intent_NFC));
    }

    @Override
    protected void initParam() {
        //不要呼叫
    }

    private void initAPI() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnNFCSendTagIdListener(this);
        cloudAPI.setOnSendVerifyListener(this);
    }

    /**
     * 掃描NFC
     */
    private void scanNfc() {
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, tag -> {
                if (myApplication.isStartScan()) {
                    IsoDep isoDepCurrentTag = IsoDep.get(tag);
                    //取得TagID
                    byte[] tagId = tag.getId();
                    //TagID
                    strTag = AppUtil.bytToHexString(tagId);
                    //收到TagId後，跟雲端要password
                    getLoginResponse();
                    String lat = "0";
                    String lon = "0";
                    if (LocationUtil.getLocation() != null) {
                        Location location = LocationUtil.getLocation();
                        lat = String.valueOf(location.getLatitude());
                        lon = String.valueOf(location.getLongitude());
                    }
                    getArea();  //取得區域or城市
                    cloudAPI.sendTAGId(userEmail, strTag,strArea,lat,lon, isoDepCurrentTag);
                }
            }, nfcFlag, null);
        }
    }

    private void playNfcNoise(String product_id) {
        int nfcNoise = AppUtil.getSharedPref(this, AppUtil.SHARED_PREF_NAME).getInt(Variable.Nfc_noise, 1);
        if (nfcNoise == 1) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                AssetFileDescriptor asset;
                if (product_id.equals("")) {
                    asset = getAssets().openFd("fake.mp3");
                } else {
                    asset = getAssets().openFd("true.mp3");
                }
                mediaPlayer.setDataSource(asset.getFileDescriptor(), asset.getStartOffset(), asset.getLength());
                mediaPlayer.prepare();
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.start();


                asset.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 廣播接收器
     * 更新NFC設定
     */
    private class BaseNfcReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Variable.Intent_NFC)) {
                scanNfc();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(baseNfcReceive);
    }

    /**
     * 送出EventBus事件，秀出Nfc結果
     * @param product_id 產品ID
     */
    private void sendEventBus(String product_id) {
        playNfcNoise(product_id);
        Intent closeIntent = new Intent(Variable.Intent_Close_NFC_Activity);
        sendBroadcast(closeIntent);
        EventBus.getDefault().post(new NfcEvent(product_id));
    }

    //------------------------------------------ API ----------------------------------//

    @Override
    public void onNFCSendTagIdMessage(NFCBaseResponse nfcBaseResponse, String error, String tag_id, IsoDep isoDepCurrentTag) {
        if (Objects.equals(error, "")) {
            if (nfcBaseResponse.getCode() == CloudCode.Code_SUCCESS) {
                if (!Objects.equals(nfcBaseResponse.getData().get(0).getTag_pw(), "")) {
                    Log.i(TAG, "readingLockedNDEFFile: " + isoDepCurrentTag.toString());
                    String verifyCode = "";
                    verifyCode = readingLockedNDEFFile(isoDepCurrentTag , nfcBaseResponse.getData().get(0).getTag_pw(), nfcBaseResponse.getData().get(0).getMsg_length());
//                    Logger.d(verifyCode);

                    if (verifyCode != null) {
                        if (Objects.equals(verifyCode, "connectError")) {
                            //連線錯誤，什麼事也不做
                        } else if (!Objects.equals(verifyCode, "error")) {
                            cloudAPI.sendVerify("", tag_id, verifyCode);

                        } else if (Objects.equals(verifyCode, "error")) {
                            //和Tag密碼不符，判斷Fake
                            sendEventBus("");
                        }
                    }
                }
            } else if (nfcBaseResponse.getCode() == CloudCode.Code_No_Password){
                //雲端找不到密碼，判斷Fake
                sendEventBus("");
            }
        }
    }

    @Override
    public void onSendVerifyMessage(NFCVerifyResponse nfcVerifyResponse, String error) {
        if (Objects.equals(error, "")) {
            if (nfcVerifyResponse.getCode() == CloudCode.Code_SUCCESS) {
                if (!Objects.equals(nfcVerifyResponse.getData().get(0).getGoods_code(), "")) {
                    sendEventBus(nfcVerifyResponse.getData().get(0).getGoods_code());
                    //掃描到真品Tag，震動手機
                    AppUtil.vibratePhone(this);
                } else {
                    //Fake
                    sendEventBus("");
                }
            } else {
                //Fake
                sendEventBus("");
            }
        }
    }

    //------------------------------------------ API ----------------------------------//

    /**
     * 取得 Tag 的 file
     * @param tagPassword tag密碼
     * @return 回傳驗證碼
     */
    private String readingLockedNDEFFile(IsoDep isoDepCurrentTag, String  tagPassword, String verifyLength) {

        //NDEF Tag Application Select command 不能變動的byte值
        byte[] NdefSelectApplicationFrame = new byte[] {
                (byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00,
                (byte) 0x07, (byte) 0xD2, (byte) 0x76, (byte) 0x00,
                (byte) 0x00, (byte) 0x85, (byte) 0x01, (byte) 0x01};
        //NDEF Select command 不能變動的byte值
        //選擇 RF 資料區塊
        //末端0x000x 第幾個區塊的資料
        byte[] NdefSelectCmd = new byte[] {
                (byte) 0x00,  (byte) 0xA4,  (byte) 0x00, (byte) 0x0C,
                (byte) 0x02, (byte) 0x00,  (byte) 0x01};


        //因為驗證碼長度不同，所以擷取的長度也需一起變動
        int intVerifyLength = Integer.valueOf(verifyLength) + 9;
        String hex_msgL = Integer.toBinaryString(intVerifyLength);
        byte temp_readBinary = Byte.parseByte(hex_msgL, 2);
        //ReadBinary mmand
        //在使用這個指令前，須先下 NDEF Select command
        byte[] readBinary = new byte[] {
                (byte) 0x00, (byte) 0xB0, (byte) 0x00, (byte) 0x00, temp_readBinary};
        Log.i(TAG, "readingLockedNDEFFile: " + temp_readBinary);
        Log.i(TAG, "readingLockedNDEFFile: " + AppUtil.convertBytesToHexString(readBinary));
        // Verify command
        //The delivery state for all passwords is 0x00000000000000000000000000000000.
        // Read password
        //byte[]  VerifyCommand = new byte[] { (byte) 0x00, (byte) 0x20,
        //        (byte) 0x00, (byte) 0x01, (byte) 0x10,
        //        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        //        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        // Write password
        //  byte[]  VerifyCommand = new byte[] { (byte) 0x00, (byte) 0x20,
        //           (byte) 0x00, (byte) 0x02, (byte) 0x10,
        //           (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30,
        //           (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30 };

        byte[] verifyCommand = new byte[] {
                (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x01, (byte) 0x10};

        //須將密碼放在驗證碼最後面
        String strVerify = new String(verifyCommand);
        strVerify = strVerify + tagPassword;

//        Logger.d("strVerify: " + AppUtil.convertBytesToHexString(strVerify.getBytes()));

        byte[] response1 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
        byte[] response2 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
        byte[] response3 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
        byte[] response4 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };

        try {
            Log.i(TAG, "readingLockedNDEFFile: " + isoDepCurrentTag);
            isoDepCurrentTag.connect();
            Logger.d("是否連線成功：" + isoDepCurrentTag.isConnected());

            //Reading a locked NDEF file
            response1 = isoDepCurrentTag.transceive(NdefSelectApplicationFrame);
            response2 = isoDepCurrentTag.transceive(NdefSelectCmd);
            response3 = isoDepCurrentTag.transceive(strVerify.getBytes());
            response4 = isoDepCurrentTag.transceive(readBinary);

            isoDepCurrentTag.close();
        } catch (NullPointerException e) {
            isoDepCurrentTag = null;
            e.printStackTrace();
            return "error";
        } catch (TagLostException e) {
            //Tag was lost
            e.printStackTrace();
        } catch (IOException e) {
            //可能連線失敗
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        //先取得送出密碼後的回應
        //代表成功
        //0x90 0x00 代表結束
        Logger.d("response3 : " + AppUtil.convertBytesToHexString(response3));
        if (Objects.equals(AppUtil.convertBytesToHexString(response3), "9000")) {
//            Logger.d(AppUtil.convertBytesToHexString(response4));
            //需判斷readBinary是否正確
            if (!AppUtil.convertBytesToHexString(response4).contains("62")) {
                byte[] response5 = new byte[response4.length - 11];
                int a = 0;
                for (int i = 9; i < (response4.length - 2); i++) {
                    response5[a] = response4[i];
                    a++;
                }

                return new String(response5);
            } else {
                //失敗
                return "error";
            }
        } else if(Objects.equals(AppUtil.convertBytesToHexString(response3), "010000")) {
            return "connectError";
        }else {
            //失敗
            return "error";
        }
    }


    /**
     * 取得GPS
     */
    private void getArea() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //不用顯示
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        } else {
            strArea = LocationUtil.getCurrentLocation(this);
        }
    }

}
