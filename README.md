# Android-GuideView
An Android GuideView for step by step to lead users.

Refer to [repertory:GuideView](https://github.com/laxian/GuideView) of [laxian](https://github.com/laxian)
By the way, thanks laxin who has share this awesome GuideView.

This repertory is an enhanced edition.

## Effect Images

![img](https://github.com/arjinmc/Android-GuideView/blob/master/images/sample.gif)

## How to Use
```java
GuideView guideView = new GuideView.Builder(activitycontext)
                //set background color
                .bgColor(Color.parseColor("#b0239823"))
                //set the target view
                .targetView(tv2)
//                .targetView(R.id.ll3)
                //set the tips view 
                .tipsView(createTextView())
//                .tipsView(R.layout.layout_linearlayout)
                 //set if need to click the target view to dismiss the guidview
                 //default is true
//                .isShouldClickFocus(false)
                //set if need to real click the target view to dissmiss
                //and it will callback the target view onclickListenter
                //defual is false
                //
                //attention:
                // set this true required the value of isShouldClickFocus is true
                // or it won't work
//                .isRealClickFocus(true)
                //set the shape of focus effect
                //default is circle
                .shape(GuideView.SHAPE_RECTANGLE)
                
                //if shape is rectangle,
                // you can set the radian to make a round retangle
                // .radian(10)
                //you can set the roundRectOffset control the order offset
                //.roundRectOffset(30)
               
                //set offset of x axis,default is 0
                .offsetX(0)
                //set offset of y axis,default is 0
                .offsetY(20)
                //set the layout gravity of tips view
                //values is Gravity.LEFT/TOP/RIGHT/BOTTOM
                //default is Gravity.BOTTOM
               .layoutGravity(Gravity.BOTTOM)
               //set ondimiss listener
//                .onDismissListener(new GuideView.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        Log.e("setOnDismissListener","dismiss");
//                    }
//                })
//                
                .create();
```

You also can set OnDismissListener like this:
```java
guideView.setOnDismissListener(new GuideView.OnDismissListener() {
    @Override
    public void onDismiss() {
        //do your things
    }
});
```