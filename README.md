# Gifflen-Android

在Android平台通过静态图片合成Gif动态图片.

![](app/src/main/res/mipmap-xxhdpi/ic_launcher_square.png)

## 示例Apk程序
[点我下载](https://fir.im/18z5)

## 一脸懵逼.gif 是如何产生的?
![](/img/GIF.gif)


由于Android平台对Gif的支持不是很好,没有现成的Java Api可以用,所以我们需要借助NDK作为桥梁,通过C++语言实现对32-bit ARGB图片进行 Color Quantization,转化成Gif动态图(256色域).这个项目的色彩转换算法是基于 [Gifflen @Bitmap color reduction and GIF encoding](http://jiggawatt.org/badc0de/android/index.html#gifflen) 的.

通过对Gifflen的C++源码进行一些定制和修改,然后编译出native library（即 .so 文件）,然后打包到 APK 中.我们可以很容易的在Android系统上创建一个Gif动图. 本项目的NDK部分代码是采用CMake的方式进行构建的.Android Studio2.2版本之后已经对NDK编程有了很好的支持.

---

## 使用方法

**1.添加动态链接库**

在此项目根目录下的so目录有我编译好的动态链接库文件,包含`arm-v8a`, `armaib`, `armabi-v7a`, `mips`, `mips64`, `x86`, `x86-64` 等7个平台,你可以根据自己的需要添加对应的native library到项目中.

**2.添加 Gifflen 工具类**

应用层的所有接口都在Gifflen.java这个类中,你要做的就是在自己项目的app/main/java路径下创建com/lchad/gifflen文件夹,然后把Gifflen.java复制到这个路径下,注意,请一定要把Gifflen.java放到以上指定位置(即your project name/app/main/java/com/lchad/gifflen)下,否则会在运行时报错,找不到jni方法.这一点需格外注意.

**3.配置读写存储的权限**

```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
如果是Android6.0及以上需注意动态申请权限.

```
 ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
```

**4.初始化工具类 Gifflen **
```
Gifflen mGiffle = new Gifflen.Builder()
                        .color(mColor)	//色域范围是2~256,且必须是2的整数次幂.
                        .delay(mDelayTime) //每相邻两帧之间播放的时间间隔.
                        .quality(mQuality) //色彩量化时的quality值.
                        .width(500)	//生成Gif文件的宽度(像素).
                        .height(500)	//生成Gif文件的高度(像素).
                        .listener(new Gifflen.OnEncodeFinishListener() {  //创建完毕的回调
                             @Override
                             public void onEncodeFinish(String path) {
                                 Toast.makeText(MainActivity.this, "已保存gif到" + path, Toast.LENGTH_LONG).show();
                                 try {
                                     GifDrawable gifFromPath = new GifDrawable(mStorePath);
                                     mGifImageView.setImageDrawable(gifFromPath);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         })
                        .build();
```

**5.开始创建 Gif 图片**

- 从File列表创建
```
	List<File> files = getFileList();
        mGiffle.encode(320, 320, "target path", files);
        mGiffle.encode("target path", files); 
```

- 从 Uri 列表创建
```
        List<Uri> uris = getUriList();
        mGiffle.encode(context, 500, 500, uris);
        mGiffle.encode(context, "target path", uris);
```

- 从 TypeArray 创建
```
	 TypeArra mDrawableList = getResources().obtainTypedArray(R.array.source);
	 mGiffle.encode(MainActivity.this, "target path", 500, 500, mDrawableList);
	 mGiffle.encode(MainActivity.this, "target path", mDrawableList);
```

- 从 Bitmap 数组创建
```
        Bitmap[] bitmaps = getBitmaps();
        mGiffle.encode("target path", 500, 500, bitmaps);
        mGiffle.encode("target path", bitmaps);
	//注意:此时要考虑Bitmap的大小以及个数,否则可能会造成OOM.
```



- 从drawable id 数组创建
```
	int[] drawableIds = new int[]{
                R.drawable.mengbi1,
                R.drawable.mengbi2,
                R.drawable.mengbi3};
        mGiffle.encode(context, "target path", 500, 500, drawableIds);
        mGiffle.encode(context, "target path", drawableIds);
```

以上五种创建方式都支持重载(本质上都是对Bitmap进行操作),宽度和高度在encode()的时候都可以缺省,这时会使用创建Gifflen时传入的值,如果创建Gifflen时仍然没有传值,则会使用一个默认的值320.
encode()属于耗时操作,建议将其执行放到子线程内部,以免阻塞UI线程.

我开了一个使用GIfflen-Android的示例项目: [GIfflen-Android-Config-Sample](https://github.com/lchad/Gifflen-Config-Sample),可以做一个参考.

具体的实现原理以及细节可以移步我的博客: [lchad](https://www.lchad.github.io)



### License

	Copyright 2017 lchad
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
