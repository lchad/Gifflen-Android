package com.lchad.gifflen;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by lchad on 2017/3/24.
 * Github : https://www.github.com/lchad
 */

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    /**
     * 申请权限返回标志字段
     */
    private static final int REQUEST_PERMISSIONS = 112;
    /**
     * 触发gif生成操作.
     */
    private Button mGenerate;
    /**
     * 点击跳转到资源图片(每一帧)的列表.
     */
    private Button mSourceList;
    private Button mReset;
    /**
     * 展示生成后的gif图片.
     */
    private GifImageView mGifImageView;
    /**
     * 拖动SeekBar来修改delay值.
     */
    private SeekBar mDelaySeekBar;
    /**
     * gif量化质量.
     */
    private SeekBar mQualitySeekBar;
    private TextView mQualityText;
    private TextView mDelayText;
    private SeekBar mColorSeekBar;
    private Button mDelayTip;
    private Button mQualityTip;
    private Button mColorTip;
    private TextView mColorText;
    /**
     * gif生成后存放的路径.
     */
    private String mStorePath;

    /**
     * 生成gif图片的工具类.
     */
    private Gifflen mGiffle;

    /**
     * 存储图片帧的资源id.
     */
    private TypedArray mDrawableList;
    /**
     * 生成的gif每一帧的时间间隔(ms),默认500.
     */
    private int mDelayTime = 500;

    private int mQuality = 10;

    private int mColor = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawableList = getResources().obtainTypedArray(R.array.source);
        findView();
        initView();

        //动态获取读写文件的权限.
        boolean hasPermission = checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }
    }

    private void initView() {
        mDelaySeekBar.setOnSeekBarChangeListener(this);
        mQualitySeekBar.setOnSeekBarChangeListener(this);
        mColorSeekBar.setOnSeekBarChangeListener(this);
        mSourceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MengBiListActivity.class));
            }
        });

        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGiffle = new Gifflen.Builder()
                        .color(mColor)
                        .delay(mDelayTime)
                        .quality(mQuality)
                        .build();
                mStorePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "gifflen-" + mQuality + "-" + mColor + "-" + mDelayTime + "-sapmle.gif";
                mGiffle.encode(MainActivity.this, mStorePath, 320, 320, mDrawableList);
                Toast.makeText(MainActivity.this, "已保存gif到" + mStorePath, Toast.LENGTH_LONG).show();
                try {
                    GifDrawable gifFromPath = new GifDrawable(mStorePath);
                    mGifImageView.setImageDrawable(gifFromPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mDelayTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.delay_tips))
                        .setView(getLayoutInflater().inflate(R.layout.delay_tips, null))
                        .show();
            }
        });
        mQualityTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.quality_tips))
                        .setView(getLayoutInflater().inflate(R.layout.quality_tips, null))
                        .show();
            }
        });

        mColorTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.color_tips))
                        .setView(getLayoutInflater().inflate(R.layout.color_tips, null))
                        .show();
            }
        });
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelaySeekBar.setProgress(500);
                mQualitySeekBar.setProgress(10);
                mColorSeekBar.setProgress(8);
            }
        });
    }

    private void findView() {
        mReset = (Button) findViewById(R.id.reset);
        mDelaySeekBar = (SeekBar) findViewById(R.id.delay_seek);
        mDelayText = (TextView) findViewById(R.id.delay_text);
        mDelayTip = (Button) findViewById(R.id.delay_tip);

        mGifImageView = (GifImageView) findViewById(R.id.gif_image);
        mSourceList = (Button) findViewById(R.id.view_list);
        mGenerate = (Button) findViewById(R.id.generate);

        mQualitySeekBar = (SeekBar) findViewById(R.id.quality_seek);
        mQualityText = (TextView) findViewById(R.id.quality_text);
        mQualityTip = (Button) findViewById(R.id.quality_tip);

        mColorSeekBar = (SeekBar) findViewById(R.id.color_seek);
        mColorTip = (Button) findViewById(R.id.color_tip);
        mColorText = (TextView) findViewById(R.id.color_text);
    }

    /**
     * 检查每个单项权限是否授予
     *
     * @param permission 权限名字.
     * @return 是否获得权限.
     */
    private boolean checkSinglePermission(String permission) {
        return ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                //...
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.delay_seek:
                mDelayTime = progress;
                mDelayText.setText("" + mDelayTime + " ms");
                break;
            case R.id.quality_seek:
                mQuality = progress;
                mQualityText.setText("" + mQuality + "");
                break;
            case R.id.color_seek:
                mColor = (int) Math.pow(2, progress);
                mColorText.setText("" + mColor + "");
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
