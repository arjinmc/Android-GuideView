# Android-GuideView
An Android GuideView for step by step to lead users.

Refer to [repertory:GuideView](https://github.com/laxian/GuideView) of [laxian](https://github.com/laxian)
By the way, thanks laxin who has share this awesome GuideView.

This repertory is an enhanced edition.

## Effect Images

![img](https://github.com/arjinmc/Android-GuideView/blob/master/images/sample.gif)

## How to Use
This GuideView need a target view and a tips view:
* target view: you want to focus on it.
* tips view: the tips will show when ui focus on the target view.

```java
GuideView.Builder guideWiewBuilder = new GuideView.Builder(activitycontext);
GuideView guideView = guideWiewBuilder.create()
```
or
```java
GuideView guideView = new GuideView.Builder(activitycontext).create();
```

## GuideView.Builder
### target view
Here are two methods can set target view.

* targetView(View view)
* targetView(R.id.viewId)

### tips view
Here are two methods can set target view.

* tipsView(view)
* tipsView(R.layout.layoutId)

### click focus part
* isShouldClickFocus  
Set if need to click the target view to dismiss the GuidView or not. Default value is true.
* isRealClickFocus  
Set if need to real click the target view to dissmiss and it will callback the target view's OnClickListenter or onTouchEvent. Defaul is false

<strong>Attention:</strong>  
Set isRealClickFocus true required the value of isShouldClickFocus is true, otherwise it won't work

### Focus part on target view shape types support
* shape(shapeType) 
default shape type is CIRCLE.

<strong>ShapeTypes values</strong>

* SHAPE_CIRCLE 
* SHAPE_OVAL
* SHAPE_RECTANGLE (including round rectangle)  
You can set the <i>radian</i> attribute to make a round retangle.  
You can set the <i>roundRectOffset</i> attribute control the order offset.

### offset on x,y axis
This offset is for tips view relative to target view on x,y axis,default values is zero.
* offsetX(int)
* offsetY(int)

### layout gravity of tips view
This layout gravity is relative to target view.
* layoutGravity(gravityInt)  default value is Gravity.BOTTOM

<strong>Values<strong>
* Gravity.LEFT
* Gravity.TOP
* Gravity.RIGHT
* Gravity.BOTTOM

### background color
bgColor(colorInt) default color is #b0000000

### blurRadius

* blurRadius(float)  
Control to the blur effect on target view. Default is zero. It will work if bigger than zero.

### OnDismissListener
Callback for dismiss.
```java
onDismissListener(new GuideView.OnDismissListener() {
    @Override
    public void onDismiss() {
        //do your things
    }
})
```
or use GuidView
```java
guideView.setOnDismissListener(new GuideView.OnDismissListener() {
    @Override
    public void onDismiss() {
        //do your things
    }
});
```

### Example
```java
GuideView guideView = new GuideView.Builder(activitycontext)
                .bgColor(Color.parseColor("#b0239823"))
                .blurRadius(5)
                .targetView(tv2)
                .tipsView(createTextView())
//                .isShouldClickFocus(false)
//                .isRealClickFocus(true)
                .shape(GuideView.SHAPE_RECTANGLE)
                //.radian(10)
                //.roundRectOffset(30)
                .offsetY(20)
               .layoutGravity(Gravity.BOTTOM)
//                .onDismissListener(new GuideView.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        Log.e("setOnDismissListener","dismiss");
//                    }
//                })
//                
                .create();
```