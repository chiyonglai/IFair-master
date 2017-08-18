package com.ifair.myUtil;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import com.ideabus.ideabuslibrary.util.BaseUtils;
import com.ifair.R;
import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Struct;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 基礎的工具
 */

public class AppUtil extends BaseUtils {

    public static final String IvAES = "hZici5D1VeRjMBIB";
    public static final String KeyAES = "7WO1abjlk0W5oSS3HTAPhQjPhpQ76qdR";

    private static final String TAG = "AppUtil";
    public static final String SHARED_PREF_NAME = "IFair";





    /**
     * 加入底線
     * @param text
     * @return
     */
    public static SpannableString returnUnderLine(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    /**
     * 取得手機的Device ID (IMEI/MEID)
     * @param activity
     * @return
     */
    public static String getIMEI(Activity activity) {
        return ((TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 取得Mac_address
     * @return
     */
    public static String getMacAddress() {
        String macAddress = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
//                Log.d("mac", "interfaceName = " + iF.getName() + ", mac=" + mac);
                if (iF.getName().equals("wlan0")) {
                    macAddress = mac;
                    return macAddress;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 金額顯示格式
     * @param num
     * @return
     */
    public static String numberFormat(String num) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        return decimalFormat.format(Double.parseDouble(num));
    }

    /**
     * 取得Youtube的ID
     * @param url
     * @return
     */
    public static String extractYoutubeId(String url) {
        try {
            String query = new URL(url).getQuery();
            String[] param = query.split("&");

            String id = null;
            for (String row : param) {
                String[] param1 = row.split("=");
                if (param1[0].equals("v")) {
                    id = param1[1];
                }
            }
            return id;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 取得youtube ID
     * @param ytUrl
     * @return
     */
    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

    /**
     * 加密
     * @param secretKey
     * @param iv
     * @param msg
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(SecretKey secretKey, byte[] iv, String msg) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        Log.i(TAG, "AES_CBC_PKCS5PADDING IV: " + cipher.getIV());
        Log.i(TAG, "AES_CBC_PKCS5PADDING: Algoritm " + cipher.getAlgorithm());
        byte[] byteCipherText = cipher.doFinal(msg.getBytes("UTF-8"));
        return byteCipherText;
    }

    /**
     * 解密
     * @param secretKey
     * @param cipherText
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decrypt(SecretKey secretKey, byte[] cipherText, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] decryptedText = cipher.doFinal(cipherText);
        String strDecrypt = new String(decryptedText);
        return strDecrypt;
    }

    /**
     * 加密
     * @param text
     * @return
     */
    public static byte[] EncryptAES(byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(IvAES.getBytes("UTF-8"));
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(KeyAES.getBytes("UTF-8"), "AES");
            //Cipher 用來加解密的API
            Cipher mCipher = null;
            mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.ENCRYPT_MODE,mSecretKeySpec,mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        } catch(Exception ex) {
            return null;
        }
    }

    //AES解密，帶入byte[]型態的16位英數組合文字、32位英數組合Key、需解密文字
    public static byte[] DecryptAES(byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(IvAES.getBytes("UTF-8"));
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(KeyAES.getBytes("UTF-8"), "AES");
            Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.DECRYPT_MODE,
                    mSecretKeySpec,
                    mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        } catch(Exception ex) {
            return null;
        }
    }

    /**
     * 取得亂數
     * @return
     */
    public static String getRegSN(){
        String[] RegSNContent = {
                "0","1","2","3","4","5","6","7","8","9",
                "A","B","C","D","E","F","G","H","I","J",
                "K","L","M","N","O","P","Q","R","S","T",
                "U","V","W","X","Y","Z","a","b","c","d",
                "e","f","g","h","i","j","k","l","m","n","o",
                "p","q","r","s","t","u","v","w","x","y","z"};
        String regsn = "";
        for(int i = 0; i < 32; i++)
            regsn += RegSNContent[(int)(Math.random()*RegSNContent.length)];
        return regsn;
    }

    /**
     * 將TagId轉成字串
     * @param tagId
     * @return
     */
    public static String bytToHexString(byte[] tagId) {
        StringBuilder strBuildTag = new StringBuilder();
        for (int i = 0; i < tagId.length; i++) {
            //將tag轉換成16進制
            if (String.format("%x", tagId[i]).trim().length() == 1) {
                strBuildTag.append("0").append(String.format("%x", tagId[i]).trim());
            } else {
                strBuildTag.append(String.format("%x", tagId[i]).trim());
            }
        }

        return strBuildTag.toString();
    }

    /**
     * 取得ToolBar高度
     * @param mContext
     * @return
     */
    public static int getToolBarHeight(Context mContext) {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
        int toolBarHeight = typedArray.getDimensionPixelOffset(0, -1);
        typedArray.recycle();
        return toolBarHeight;
    }

    /**
     * 將EditText文字轉成字串
     * @param editText
     * @return
     */
    public static String editToString(EditText editText) {
        return editText.getText().toString();
    }


    /**
     * 將轉成Drawable
     * @param res
     * @param resId
     * @return
     */
    public static Drawable ridToDrawable(Resources res, int resId) {
        BitmapDrawable bd = null;
        try {
            InputStream is = res.openRawResource(resId);
            bd = new BitmapDrawable(res, is);//new BitmapDrawable(res, BitmapFactory.decodeStream(is));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        return bd;
    }

    /**
     * 震動手機
     */
    public static void vibratePhone(Activity mActivity) {
        int vibrate = AppUtil.getSharedPref(mActivity, AppUtil.SHARED_PREF_NAME).getInt(Variable.Nfc_vibrate, 1);
        if (vibrate == 1) {
            Vibrator myVibrator = (Vibrator) mActivity.getApplication().getSystemService(Service.VIBRATOR_SERVICE);
            myVibrator.vibrate(1000);
        }
    }

    /**
     * 前往瀏覽器
     * @param mActivity
     * @param url 網址
     */
    public static void intentUrl(Activity mActivity, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(mActivity.getResources().getColor(R.color.colorAccent));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_arrow_back));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(mActivity, Uri.parse(url));
    }

}
