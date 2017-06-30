package com.hubli.imperium.imaperiumdiary.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Faheem on 11-05-2017.
 */

public class GenericMethods {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TEMP_PATH = "temp_image.jpg";

    public static String getThumbNailURL(String str){
        return str.replace("profilepic","propic_thumb");
    }
    public static boolean checkImageUrl(String url){
        return (url != null && url.contains("jpeg")) ? true:false;
    }

    public static int getDateSum(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH) + (c.get(Calendar.MONTH) + 1) + c.get(Calendar.YEAR);
    }

    public static String getCurrentTimeString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(c.getTime());
    }
    public static String getTimeString(String dateString){
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            date = dateFormat.parse(dateString);
            String time = "";
            long seconds = (System.currentTimeMillis() - date.getTime())/1000;
            if(seconds < 60 ){
                time  = "seconds ago";
            }else if(seconds <  60 * 60){
                time = (int)(seconds /60)+" min ago";
            }else if(seconds < 86400){
                time = (int) (seconds / (60*60))+"hr ago";
            }else if(seconds < 604800){
                time = (int) (seconds / (60*60*24)) + " days ago";
            }else if(seconds > 518400) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd,  HH:mm a");
                time = simpleDateFormat.format(new Date(date.getTime())).replace("  ", " at ");
            } else if((seconds/(60*60*24*30))>365){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MMM-dd,  HH:mm a");
                time = simpleDateFormat.format(new Date(date.getTime())).replace("  ", " at ");
            }
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getTimeInMili(String timeString){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = dateFormat.parse(timeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap getCameraImage(){
        File f = new File(Environment.getExternalStorageDirectory()
                .toString());
        for (File temp : f.listFiles()) {
            if (temp.getName().equals(TEMP_PATH)) {
                f = temp;
                break;
            }
        }
        return  orientation(f.getAbsolutePath());
    }

    public static Bitmap orientation(String path){
        Bitmap img;
        try {
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            img = BitmapFactory.decodeFile(path,
                    btmapOptions);
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            img = Bitmap.createBitmap(img,0,0,img.getWidth(),img.getHeight(),matrix,true);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
