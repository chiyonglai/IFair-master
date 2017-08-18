package com.ifair.myUtil;

import android.content.Context;
import android.os.Environment;

import com.ideabus.ideabuslibrary.util.TimeUtils;
import com.ifair.BuildConfig;

import java.io.File;

/**
 *
 */

public class MyFileUtil {

    public static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/" + BuildConfig.APPLICATION_ID;

    public static File getOutputMediaFile(int type) {
        String storagePath = "";
        if (storagePath.equals("")) {
            storagePath = filePath + "/image";
            File file = new File(storagePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        File mediaFile = null;
        if (type == 1) {
            mediaFile = new File(storagePath + File.separator + "IMG_" + TimeUtils.getCurrentTime("yyyyMMddhhmmss") + ".png");
        } else {
            mediaFile = new File(storagePath + File.separator + "VID_" + TimeUtils.getCurrentTime("yyyyMMddhhmmss") + ".mp4");
        }
        return mediaFile;
    }
}
