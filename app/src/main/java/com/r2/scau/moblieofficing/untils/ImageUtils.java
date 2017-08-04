package com.r2.scau.moblieofficing.untils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static android.R.attr.bitmap;

/**
 * Created by Edward on 2017/8/4.
 */

public class ImageUtils {

    public static TextDrawable getIcon(String content, int textsize){
        if(content == null) return null;
        String str = content;
        if(isContainChinese(content)){
            if(content.getBytes().length > 6)
            {
                str = str.replace(getSubString(content,2),"");
            }
        }
        else{
            if(str.length() > 1)
            {
                str = str.substring(0,1);
            }
        }
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable drawable =  TextDrawable.builder().beginConfig().width(30).height(30)
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(textsize)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(str, color);
        return drawable;
    }

    public static boolean isContainChinese(String str){
        return str.length() != str.getBytes().length;
    }

    public static String getSubString(String str, int length) {
        int count = 0;
        int offset = 0;
        char[] c = str.toCharArray();
        int size = c.length;
        if(size >= length){
            for (int i = 0; i < c.length; i++) {
                if (c[i] > 256) {
                    offset = 2;
                    count += 2;
                } else {
                    offset = 1;
                    count++;
                }
                if (count == length) {
                    return str.substring(0, i + 1);
                }
                if ((count == length + 1 && offset == 2)) {
                    return str.substring(0, i);
                }
            }
        }else{
            return str;
        }
        return "";
    }

    public static File changeDrawableToFile(Drawable drawable,String path, String fileName){
        //BitmapFactory.
//        BitmapDrawable bd = (BitmapDrawable) drawable;
//        Bitmap bm = bd.getBitmap();
        return changeBitmapToFile(drawableToBitmap(drawable), path, fileName);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static File changeBitmapToFile(Bitmap bm,String path, String fileName){
        File file=new File(path, fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}
