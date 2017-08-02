package com.arjinmc.guideview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * GuideView
 * Created by Eminem Lu on 26/7/17.
 * Email arjinmc@hotmail.com
 */

public class GuideView extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    //shape types
    public static final int SHAPE_CIRCLE = 0;
    public static final int SHAPE_OVAL = 1;
    public static final int SHAPE_RECTANGLE = 2;

    public static final int SHAPE_TYPE_COUNT = 3;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#b0000000");

    private int mBackgroudColor;
    /**
     * use blurmask
     */
    private float mBlurRadius;
    /**
     * the shape of focus on targetview,default is circle
     */
    private int mShape;
    // the angle of the shape
    private float mRadian;
    //offset of round rect->round rectangle
    private int mRoundRectOffset;
    /**
     * set if need to click the focus part to dismiss this view,default is true
     */
    private boolean isShouldClickFocus;
    /**
     * set if need to really click the focus part if there is a MotionEvent,default is false
     */
    private boolean isRealClickFocus;
    /**
     * the target view for focus guide
     */
    private View mTargetView;
    private int mTargetViewId;
    /**
     * tips view guide to the focus view
     */
    private View mTipsView;
    private int mTipsViewResId;
    /**
     * the layout gravity of tipsview
     * support:Gravity.LEFT/TOP/RIGHT/BOTTOM
     */
    private int mTipsViewLayoutGravity;
    /**
     * the offset of tipsview to targetview for x,y axis
     */
    private int mOffsetX, mOffsetY;

    private ViewGroup mParentView;
    private int mTargetViewLeft, mTargetViewTop, mTargetViewRight, mTargetViewBottom;
    private Point mTargetViewCenterPoint;
    private int[] mTargetViewLocation;
    private Point mTouchPoint;
    private int mRadius;
    private boolean isFirstShown = true;

    private Paint mBackgroundPaint, mFocusPaint;
    private Bitmap mBitmap;

    private Context mContext;
    private OnDismissListener mOnDismissListener;

    public GuideView(Context context) {
        super(context);
        mContext = context;
    }

    public GuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GuideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    public void setParams(Param params) throws GuideViewInitException {
        mBackgroudColor = params.backgroundColor;
        mBlurRadius = params.blurRadius;
        mShape = params.shape;
        mRadian = params.radian;
        mRoundRectOffset = params.roundRectOffset;
        isShouldClickFocus = params.isShouldClickFocus;
        isRealClickFocus = params.isRealClickFocus;
        mTargetView = params.targetView;
        mTargetViewId = params.targetViewId;
        mTipsViewResId = params.tipsViewResId;
        mTipsView = params.tipsView;
        mTipsViewLayoutGravity = params.layoutGravity;
        mOffsetX = params.offsetX;
        mOffsetY = params.offsetY;
        mOnDismissListener = params.onDismissListener;

        if (mContext instanceof Activity) {
            mParentView = (ViewGroup) (((Activity) mContext).getWindow().getDecorView()
                    .findViewById(android.R.id.content)).getParent().getParent();
        } else
            throw new GuideViewInitException("This context cannot use this view!");

        if (mTargetView == null && mTargetViewId != 0 && mParentView != null) {
            mTargetView = mParentView.findViewById(mTargetViewId);
        }
        if (mTargetView == null)
            throw new GuideViewInitException("TargetView cannot be null!");
        if (mTipsView == null && mTipsViewResId == 0)
            throw new GuideViewInitException("TipsView cannot be null!");
        init();
    }

    public void init() {

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setColor(mBackgroudColor);

        mFocusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mFocusPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (mBlurRadius > 0)
            mFocusPaint.setMaskFilter(new BlurMaskFilter(mBlurRadius, BlurMaskFilter.Blur.NORMAL));

        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (mTipsView == null && mTipsViewResId != 0)
            mTipsView = LayoutInflater.from(mContext).inflate(mTipsViewResId, this, false);

        addView(mTipsView);
        setVisibility(View.GONE);
        setBackgroundColor(Color.TRANSPARENT);

        mTargetView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * show this view
     */
    public void show() {

        if (mParentView == null)
            return;
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
            mParentView.addView(this);
        }
    }

    /**
     * dismiss this view
     */
    public void dismiss() {

        if (mParentView == null)
            return;
        if (getVisibility() == View.VISIBLE) {
            setVisibility(View.GONE);
            if (isFirstShown) {
                removeOnGlobalLayoutListener();
                isFirstShown = false;
            }
            mParentView.removeView(this);
            if (mOnDismissListener != null)
                mOnDismissListener.onDismiss();
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    /**
     * remove globallayout listener of targetView
     */
    private void removeOnGlobalLayoutListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            mTargetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /**
     * calculte the radius of target view
     *
     * @return
     */
    private int calculateTargetViewRadius() {
        int x = mTargetView.getMeasuredWidth();
        int y = mTargetView.getMeasuredHeight();
        return (int) (Math.sqrt(x * x + y * y) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(mBitmap);

        //drawbackground
        tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), mBackgroundPaint);

        // draw focus targetView
        switch (mShape) {
            //draw oval
            case SHAPE_OVAL:
                RectF ovalRectF = new RectF();
                ovalRectF.left = mTargetViewCenterPoint.x - mRadius / 2 - mTargetView.getWidth() / 2;
                ovalRectF.top = mTargetViewCenterPoint.y - mRadius / 3 - getStatusBarHeight(mContext) - mTargetView.getHeight() / 2;
                ovalRectF.right = mTargetViewCenterPoint.x + mRadius / 2 + mTargetView.getWidth() / 2;
                ovalRectF.bottom = mTargetViewCenterPoint.y + mRadius / 3 - getStatusBarHeight(mContext) + mTargetView.getHeight() / 2;
                tempCanvas.drawOval(ovalRectF, mFocusPaint);
                break;
            //draw rectangle
            case SHAPE_RECTANGLE:
                RectF rectRectF = new RectF();
                rectRectF.left = mTargetViewLeft - mRoundRectOffset;
                rectRectF.top = mTargetViewTop - getStatusBarHeight(mContext) - mRoundRectOffset;
                rectRectF.right = mTargetViewRight + mRoundRectOffset;
                rectRectF.bottom = mTargetViewBottom - getStatusBarHeight(mContext) + mRoundRectOffset;
                if (mRadian == 0) {
                    tempCanvas.drawRect(rectRectF, mFocusPaint);
                } else {
                    //draw round rectangle
                    tempCanvas.drawRoundRect(rectRectF, mRadian, mRadian, mFocusPaint);
                }
                break;
            //draw circle
            default:
                tempCanvas.drawCircle(mTargetViewCenterPoint.x
                        , mTargetViewCenterPoint.y - getStatusBarHeight(mContext)
                        , mRadius, mFocusPaint);
                break;
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBackgroundPaint);
        mBitmap.recycle();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mTargetViewCenterPoint != null) {
            //layout tips view
            mRadius = calculateTargetViewRadius();
            ViewGroup.LayoutParams layoutParams = mTargetView.getLayoutParams();

            int x, y;
            switch (mTipsViewLayoutGravity) {
                case Gravity.TOP:
                    x = mTargetViewCenterPoint.x + mOffsetX;
                    y = mTargetViewCenterPoint.y
                            - (mShape == SHAPE_OVAL ? mRadius / 3 + mTargetView.getHeight() / 2 : mRadius)
                            - mTipsView.getMeasuredHeight() / 2
                            + mOffsetY - getStatusBarHeight(mContext) - (int) mBlurRadius
                            - (mShape == SHAPE_RECTANGLE ? mRoundRectOffset : 0);
                    mTipsView.layout(
                            x - mTipsView.getMeasuredWidth() / 2
                            , y - mTipsView.getMeasuredHeight() / 2
                            , x + mTipsView.getMeasuredWidth() / 2
                            , y + mTipsView.getMeasuredHeight() / 2);
                    break;
                case Gravity.LEFT:
                    x = mTargetViewCenterPoint.x
                            - (mShape == SHAPE_OVAL ? (mRadius + mTargetView.getWidth()) / 2 : mRadius)
                            - (int) mBlurRadius + mOffsetX
                            - (mShape == SHAPE_RECTANGLE ? mRoundRectOffset : 0);
                    y = mTargetViewCenterPoint.y + mOffsetY
                            - getStatusBarHeight(mContext) - (int) mBlurRadius;
                    mTipsView.layout(
                            x - mTipsView.getMeasuredWidth()
                            , y - mTipsView.getHeight() / 2
                            , x
                            , y + mTipsView.getHeight() / 2);
                    break;
                case Gravity.RIGHT:
                    x = mTargetViewCenterPoint.x
                            + (mShape == SHAPE_OVAL ? (mRadius + mTargetView.getWidth()) / 2 : mRadius)
                            + (int) mBlurRadius + mOffsetX
                            + (mShape == SHAPE_RECTANGLE ? mRoundRectOffset : 0);
                    y = mTargetViewCenterPoint.y + mOffsetY
                            - getStatusBarHeight(mContext) - (int) mBlurRadius;
                    mTipsView.layout(
                            x
                            , y - mTipsView.getMeasuredHeight() / 2
                            , x + mTipsView.getMeasuredWidth()
                            , y + mTipsView.getMeasuredHeight() / 2);
                    break;
                case Gravity.BOTTOM:
                default:
                    x = mTargetViewCenterPoint.x + mOffsetX;
                    y = mTargetViewCenterPoint.y
                            + (mShape == SHAPE_OVAL ? mRadius / 3 + mTargetView.getHeight() / 2 : mRadius)
                            + mTipsView.getMeasuredHeight() / 2 + mOffsetY
                            - getStatusBarHeight(mContext) + (int) mBlurRadius
                            + (mShape == SHAPE_RECTANGLE ? mRoundRectOffset : 0);
                    mTipsView.layout(
                            x - mTipsView.getMeasuredWidth() / 2
                            , y - mTipsView.getMeasuredHeight() / 2
                            , x + mTipsView.getMeasuredWidth() / 2
                            , y + mTipsView.getMeasuredHeight() / 2);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchPoint == null)
                    mTouchPoint = new Point();
                mTouchPoint.x = (int) event.getX();
                mTouchPoint.y = (int) event.getY();
                if (mTouchPoint.x >= mTargetViewLeft && mTouchPoint.x <= mTargetViewRight
                        && mTouchPoint.y >= mTargetViewTop && mTouchPoint.y <= mTargetViewBottom) {
                    if (isShouldClickFocus && isRealClickFocus)
                        return false;
                    return true;
                } else
                    return true;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x >= mTargetViewLeft && x <= mTargetViewRight
                        && y >= mTargetViewTop && y <= mTargetViewBottom) {
                    if (isShouldClickFocus && isRealClickFocus)
                        return true;
                    else if (isShouldClickFocus) {
                        dismiss();
                        return false;
                    }
                    dismiss();
                    return false;
                } else {
                    if (!isShouldClickFocus)
                        dismiss();
                    return false;
                }
            default:
                return false;
        }

    }

    @Override
    public void onGlobalLayout() {

        if (mTargetViewLocation == null) {
            mTargetViewLocation = new int[2];
            mTargetView.getLocationInWindow(mTargetViewLocation);
            mTargetViewLeft = mTargetViewLocation[0];
            mTargetViewTop = mTargetViewLocation[1];
            mTargetViewRight = mTargetViewLocation[0] + mTargetView.getWidth();
            mTargetViewBottom = mTargetViewLocation[1] + mTargetView.getHeight();

            mTargetViewCenterPoint = new Point(
                    mTargetViewLocation[0] + mTargetView.getWidth() / 2
                    , mTargetViewLocation[1] + mTargetView.getHeight() / 2);
        }
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static class Builder {

        public Param param;
        public Context context;

        public Builder(Context context) {
            param = new Param();
            this.context = context;
        }

        public GuideView create() {
            GuideView guideView = new GuideView(context);
            try {
                guideView.setParams(param);
            } catch (GuideViewInitException e) {
                e.printStackTrace();
            }
            return guideView;
        }

        public Builder bgColor(@ColorInt int backgroundColor) {
            param.backgroundColor = backgroundColor;
            return this;
        }

        public Builder blurRadius(float blurRadius) {
            param.blurRadius = blurRadius;
            return this;
        }

        public Builder shape(int shape) {
            if (shape > GuideView.SHAPE_TYPE_COUNT - 1 || shape < 0)
                shape = GuideView.SHAPE_CIRCLE;
            param.shape = shape;
            return this;
        }

        public Builder radian(float radian) {
            param.radian = radian;
            return this;
        }

        public Builder roundRectOffset(int roundRectOffset) {
            param.roundRectOffset = roundRectOffset;
            return this;
        }

        /**
         * set if need to click the focus part to dismiss this view
         *
         * @param isShouldClickFocus
         * @return
         */
        public Builder isShouldClickFocus(boolean isShouldClickFocus) {
            param.isShouldClickFocus = isShouldClickFocus;
            return this;
        }

        /**
         * set if need to really click the focus part if there is a MotionEvent
         *
         * @param isRealClickFocus
         * @return
         */
        public Builder isRealClickFocus(boolean isRealClickFocus) {
            param.isRealClickFocus = isRealClickFocus;
            return this;
        }

        public Builder targetView(View view) {
            param.targetView = view;
            return this;
        }

        public Builder targetView(@IdRes int targetViewId) {
            param.targetViewId = targetViewId;
            return this;
        }

        public Builder tipsView(View view) {
            param.tipsView = view;
            return this;
        }

        public Builder tipsView(@LayoutRes int tipsViewLayoutResId) {
            param.tipsViewResId = tipsViewLayoutResId;
            return this;
        }

        public Builder offsetX(int offsetX) {
            param.offsetX = offsetX;
            return this;
        }

        public Builder offsetY(int offsetY) {
            param.offsetY = offsetY;
            return this;
        }

        public Builder layoutGravity(int layoutGravity) {
            param.layoutGravity = layoutGravity;
            return this;
        }

        public Builder onDismissListener(OnDismissListener onDismissListener) {
            param.onDismissListener = onDismissListener;
            return this;
        }

    }

    public static class Param {
        public int backgroundColor = GuideView.DEFAULT_BACKGROUND_COLOR;
        public int shape = GuideView.SHAPE_CIRCLE;
        public float blurRadius;
        public float radian;
        public int roundRectOffset = 6;
        public boolean isShouldClickFocus = true;
        public boolean isRealClickFocus = false;
        public View targetView;
        public View tipsView;
        public int offsetX, offsetY;
        public int layoutGravity;
        public int targetViewId;
        public int tipsViewResId;
        public OnDismissListener onDismissListener;
    }

    public class GuideViewInitException extends Exception {

        public GuideViewInitException(String message) {
            throw new RuntimeException(message);
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
