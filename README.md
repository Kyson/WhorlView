# WhorlView

一个加载View

## 使用

### step1

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

### step2

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

