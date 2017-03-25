# Gifflen-Android

在Android平台合成gif动态图片.

![](app/src/main/res/mipmap-xxhdpi/ic_launcher_square.png)

### 示例Apk程序
[点我下载](https://fir.im/18z5)




这个项目是基于 [Gifflen](http://jiggawatt.org/badc0de/android/index.html#gifflen) 的.

通过对Gifflen的C++源码进行一些定制和修改,然后编译出native library（即 .so 文件）,然后打包到 APK 中. Java 代码就可以通过 Java Native Interface（JNI）调用 native library 中的方法了。
Android Studio2.2版本之后,其对NDK已经有了很好的支持:
- 可以通过LLVM对底层代码跟踪断点,再也不用像以前一样只能靠打LOG瞎猜了
- 默认使用 CMake 编译原生库
- 同时也支持ndk-build
- 可以关联本地库到gradle




具体的实现细节可以移步我的博客: [lchad](https://www.lchad.github.io)
