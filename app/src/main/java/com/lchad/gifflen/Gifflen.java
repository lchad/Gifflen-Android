package com.lchad.gifflen;

/**
 * Created by lchad on 2017/3/24.
 * Github : https://www.github.com/lchad
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Gifflen {

    private static final String TAG = "Giffle";

    static {
        System.loadLibrary("gifflen-lib");
    }

    private static final int DEFAULT_COLOR = 256;
    private static final int DEFAULT_QUALITY = 10;
    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 320;
    private static final int DEFAULT_DELAY = 500;

    private int mColor;
    private int mQuality;
    private int mDelay;

    private Gifflen(int color, int quality, int delay) {
        this.mColor = color;
        this.mQuality = quality;
        this.mDelay = delay;
    }

    public Builder newBuilder() {
        return new Builder();
    }

    /**
     * Gifflen AddFrame
     *
     * @param pixels
     * @return
     */
    public native int AddFrame(int[] pixels);

    /**
     * Gifflen Init
     *
     * @param name    File Name : "***.gif"
     * @param width
     * @param height
     * @param color
     * @param quality
     * @param delay
     * @return
     */
    public native int Init(String name, int width, int height, int color, int quality, int delay);

    /**
     * * Gifflen Close
     */
    public native void Close();

    /**
     * Create Gif Animation File
     *
     * @return
     */
    public boolean encode(int width, int height, String path, List<File> files) {

        int state;
        int[] pixels = new int[width * height];

        state = Init(path, width, height, mColor, mQuality, mDelay / 10);
        if (state != 0) {
            // Failed
            return false;
        }

        for (File aFileList : files) {
            Bitmap bitmap;
            try {
                File file = aFileList;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                return false;
            }

            if (width < bitmap.getWidth() || height < bitmap.getHeight()) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            AddFrame(pixels);
            bitmap.recycle();
        }

        Close();

        return true;
    }

    /**
     * Create Gif Animation File
     *
     * @param context
     * @param path         File Name
     * @param width
     * @param height
     * @param drawableList Drawable Resrouce R.drawable.* File List
     * @return
     */
    public boolean encode(final Context context, final String path, final int width, final int height, final int[] drawableList) {
        if (drawableList == null || drawableList.length == 0) {
            return false;
        }
        int state;
        int[] pixels = new int[width * height];

        state = Init(path, width, height, mColor, mQuality, mDelay / 10);
        if (state != 0) {
            // Failed
            return false;
        }

        for (int drawable : drawableList) {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
            if (width < bitmap.getWidth() || height < bitmap.getHeight()) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            AddFrame(pixels);
            bitmap.recycle();
        }
        Close();

        return true;
    }

    /**
     * Create Gif Animation File
     *
     * @param context
     * @param path       File Name
     * @param width
     * @param height
     * @param typedArray Drawable Resrouce Array R.array.* File List
     * @return
     */
    public boolean encode(final Context context, final String path, final int width, final int height, final TypedArray typedArray) {
        if (typedArray == null || typedArray.length() == 0) {
            return false;
        }
        int state;
        int[] pixels = new int[width * height];

        state = Init(path, width, height, mColor, mQuality, mDelay / 10);
        if (state != 0) {
            // Failed
            return false;
        }

        for (int i = 0; i < typedArray.length(); i++) {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeResource(context.getResources(), typedArray.getResourceId(i, -1));
            if (width < bitmap.getWidth() || height < bitmap.getHeight()) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            AddFrame(pixels);
            bitmap.recycle();
        }
        Close();

        return true;
    }

    public static final class Builder {

        public Builder() {
            color = DEFAULT_COLOR;
            quality = DEFAULT_QUALITY;
            delay = DEFAULT_DELAY;
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
        }

        private int color;
        private int quality;
        private int delay;
        private int width;
        private int height;

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder quality(int quality) {
            this.quality = quality;
            return this;
        }

        public Builder delay(int delay) {
            this.delay = delay;
            return this;
        }

        public Gifflen build() {
            if (this.color < 2 || this.color > 256) {
                throw new IllegalStateException("the mColor param should be 2...256!");
            }
            if (this.quality <= 0) {
                quality = 1;
            }
            if (quality > 100) {
                quality = 100;
            }
            if (delay <= 0) {
                throw new IllegalStateException("the mDelay time must not be less than one miliisecond!");
            }
            return new Gifflen(this.color, this.quality, this.delay);
        }
    }
    // TODO: 2017/3/25 增加encoding结束回调
//    private CallBack mCallBack;
//
//    public void setCallBack(CallBack callBack) {
//        mCallBack = callBack;
//    }
//
//    public interface CallBack {
//        void onFinish();
//    }
//
//    public void callbackFromJni() {
//        if (mCallBack != null) {
//            mCallBack.onFinish();
//        }
//    }
}