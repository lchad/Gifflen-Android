# Gifflen-Android

在Android平台合成gif动态图片.

![](app/src/main/res/mipmap-xxhdpi/ic_launcher_square.png)

### 示例Apk程序
[点我下载](https://fir.im/18z5)

---

## 一脸懵逼.gif 是如何产生的?
![](/img/GIF.gif)


由于Android平台对Gif的支持不是很好,所以我们需要通过C++来对图片进行 Color Quantization.这个项目的算法是基于 [Gifflen](http://jiggawatt.org/badc0de/android/index.html#gifflen) 的.

通过对Gifflen的C++源码进行一些定制和修改,然后编译出native library（即 .so 文件）,然后打包到 APK 中. Java 代码就可以通过 Java Native Interface（JNI）调用 native library 中的方法了。
此项目采用CMake的方式来构建native code, 因为Android Studio2.2版本之后,其对NDK已经有了很好的支持:
- 可以通过LLVM对底层代码跟踪断点,再也不用像以前一样只能靠打LOG瞎猜了
- 默认使用 CMake 编译原生库
- 同时也支持ndk-build
- 可以关联本地库到gradle




具体的实现原理以及细节可以移步我的博客: [lchad](https://www.lchad.github.io)



### License

	Copyright 2017 lchad
	Licensed under the Apache License, Version 2.0 (the "License");	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
