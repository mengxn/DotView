# DotView

未读消息提示，可自定义颜色及Padding
> 在程序中随处可见红点提示，用于提示用户有数据更新或新功能。遵循不重复造轮子的原则，我们将这一部分独立出来，以备复用。

### 效果展示
> 在大部分情况下红点提示会显示在图标右上角，也有部分是在文字的前面。

![demo](image/demo_2.png)

### 使用
#### 引用
```groovy
compile 'me.codego.view:dot-view:1.0.3'
```
#### DotView
> 我们可以使用DotView，在任何你需要的地方。

1. xml布局
```xml
<me.codego.view.DotView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="37"
    android:textColor="#FFF"
    android:textSize="12sp"
    app:dotPadding="3dp" 
    app:dotColor="#00F"/>
```
dotColor：圆点背景色  
dotPadding：圆点 padding 值。不显示数字：相当于圆半径；显示数字：外圆到数字的距离  

#### DotLayout
> 我们也可以直接使用DotLayout，DotLayout集成更简单。

1. 我们可以使用 DotLayout 包裹任何需要提示的 View，例如：
```xml
<me.codego.view.DotLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dotOverPadding="3dp">

    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@mipmap/ic_launcher"/>
</me.codego.view.DotLayout>
```
dotColor：圆点背景色  
dotTextColor：圆点前景色，即数字显示颜色  
dotPadding：圆点 padding 值。不显示数字：相当于圆半径；显示数字：外圆到数字的距离  
dotTextSize：字号大小  
dotOverPadding：圆点相对于 View 在右上角溢出的距离  
dotLocation：圆点位置：left：左侧；right：右侧  

2. 调用代码进行展示
- 仅显示提示  
`dotLayout.show(true)`
- 显示数字提示  
`dotLayout.show(true, 4)`
