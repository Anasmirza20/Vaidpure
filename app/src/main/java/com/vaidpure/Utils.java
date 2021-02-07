package com.vaidpure;

import android.content.pm.PackageManager;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {

    public static final String TYPE_NO="type_no";
    public static final String SPIN_WHEEL="SpinWheel";
    public static final String SCRATCH="Scratch";
    public static final String VIDEO_WALL="VideoWall";
    public static final String JOB_ID="job-id";
    public static final String ERROR_ID="error-id";
    public static final String DEVICE_USER="device_user";
    public static final String DEVICE_ID="device_id";
    public static final String WORKING_ID="working_id";
    public static final String WORKING_TYPE="working_type";
    public static final String IMG_USER="img_user";
    public static final String IMG_TYPE="img_type";
    public static final String IMG_FILE="img_file";

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static MultipartBody.Part convertIntoMultipart(String filePath) {
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
        // Create MultipartBody.Part using file request-body,file name and part name
        return MultipartBody.Part.createFormData(IMG_FILE, file.getName(), fileReqBody);
    }
}
