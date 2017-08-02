package com.arjinmc.guideview;

import java.util.ArrayList;
import java.util.List;

/**
 * A queue for all guideviews which has been added
 * Created by Eminem Lu on 2/8/17.
 * Email arjinmc@hotmail.com
 */

public final class GuideViewQueue {

    private static GuideViewQueue sGuideViewQueue;
    private static List<GuideView> sGuideViewList;
    private static OnFinallyDismissListener sOnFinallyDismissListener;

    public static synchronized GuideViewQueue getInstance() {

        if (sGuideViewQueue == null) {
            sGuideViewQueue = new GuideViewQueue();

        }
        return sGuideViewQueue;
    }

    /**
     * add guideview to the queue
     *
     * @param guideView
     */
    public static void add(GuideView guideView) {

        if (sGuideViewList == null)
            sGuideViewList = new ArrayList<>();
        sGuideViewList.add(guideView);

    }

    /**
     * set the finally dismissListenter callback
     *
     * @param onFinallyDismissListener
     */
    public static void setOnFinallyDismissListener(OnFinallyDismissListener onFinallyDismissListener) {
        sOnFinallyDismissListener = onFinallyDismissListener;
    }

    /**
     * start the queque for guideview that to show each guideview
     * then finally callback the OnFinallyDismissListener
     */
    public static void start() {
        if (sGuideViewList == null || sGuideViewList.isEmpty())
            return;
        final int size = sGuideViewList.size();
        for (int i = 0; i < size; i++) {
            final int currrentPosition = i;
            sGuideViewList.get(i).setOnDismissListener(new GuideView.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (currrentPosition != size - 1)
                        sGuideViewList.get(currrentPosition + 1).show();
                    else {
                        if (sOnFinallyDismissListener != null)
                            sOnFinallyDismissListener.onDismiss();

                        //GC
                        sGuideViewList.clear();
                        sGuideViewList = null;
                        sGuideViewQueue = null;
                        sOnFinallyDismissListener = null;
                    }

                }
            });
        }
        sGuideViewList.get(0).show();
    }

    public interface OnFinallyDismissListener {
        void onDismiss();
    }

}
