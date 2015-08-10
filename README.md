# WhorlView

一个加载View

## 预览

![https://raw.githubusercontent.com/Kyson/WhorlView/master/art/whorl_progress_showcase.gif](https://raw.githubusercontent.com/Kyson/WhorlView/master/art/whorl_progress_showcase.gif)

## 使用

### step1

添加gradle配置

```
dependencies {
    compile 'com.tt:whorlviewlibrary:1.0.0'
}
```

### step2

在xml中添加WhorlView

```xml
<com.tt.whorlviewlibrary.WhorlView xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/whorl"
        android:layout_width="90dp"
        android:layout_height="90dp"
        app:WhorlView_BigWhorlColor="@color/material_blue"
        app:WhorlView_CircleSpeed="270"
        app:WhorlView_MiddleWhorlColor="@color/material_red"
        app:WhorlView_Parallax="fast"
        app:WhorlView_SmallWhorlColor="@color/material_green"></com.tt.whorlviewlibrary.WhorlView>
```

### step3

开始动画

```java
WhorlView whorlView = (WhorlView) this.findViewById(R.id.whorl);
whorlView.start();
```

## XML自定义属性

|属性|类型|说明|默认值|
|---|---|---|---|
|WhorlView_SmallWhorlColor|color|最内圈颜色|#F44336(红色)|
|WhorlView_MiddleWhorlColor|color|中间圈颜色|#4CAF50(绿色)|
|WhorlView_BigWhorlColor|color|最外圈颜色|#5677fc(蓝色)|
|WhorlView_CircleSpeed|int|转圈速度|270度每秒|
|WhorlView_Parallax|enum|视差效果|72度每秒|

## API

提供的api就两个，开始和停止。

`whorlView.start();`

`whorlView.stop();`

## 鸣谢

- [小书匠](http://markdown.xiaoshujiang.com/)的创意

> 微博看到有人分享markdown工具看到这个网站，感觉不错，不过最让我印象深刻的就是这个加载效果咯。

## 更多

- [我的个人博客](http://www.hikyson.cn)

- [我的Github](https://github.com/Kyson)

- [我的OSC](http://git.oschina.net/cocobaby)

- [我的新浪微博](http://weibo.com/1980495343/profile?rightmod=1&wvr=6&mod=personinfo)

## License

Copyright (c) 2015 Kyson

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

