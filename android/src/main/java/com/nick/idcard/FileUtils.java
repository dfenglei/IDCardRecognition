package com.nick.idcard;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class FileUtils {
    public static String imageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/nick/image/";

    public static String getImageDirectoryPath(){
        File file=new File(imageDirectoryPath);
        if(!file.exists())
            file.mkdir();

        return imageDirectoryPath;
    }


    public static boolean getExternalStorageState() {
//获取内部存储状态
        String state = Environment.getExternalStorageState();
//如果状态不是mounted，无法读写
        return state.equals(Environment.MEDIA_MOUNTED);

    }

    public static void saveImg2LocalWithName(Bitmap mBitmap, String fileName) {

        if(!getExternalStorageState()){
            return;
        }
        try {
            File file = new File(getImageDirectoryPath() + fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static String getCommonFileName(){

        //通过UUID生成字符串文件名
        String fileName1 = UUID.randomUUID().toString();

        return fileName1;
    }


}
