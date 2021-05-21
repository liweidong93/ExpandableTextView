package cn.lwd.expandabletext.expandable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 收起展开textview点击
 * Created by liweidong on 2021/4/13.
 */
public class ExpandableClickSpan extends ClickableSpan {

    private ExpandableTextView mTextView;

    public ExpandableClickSpan(ExpandableTextView textView){
        this.mTextView = textView;
    }

    @Override
    public void onClick(View widget) {
        if (mTextView != null){
            if (mTextView.getState() == ExpandableTextView.ExpandableState.STATE_CLOSE){
                //收起状态，需要点击展开
                IExpandableCloseCallBack expandableCallback = mTextView.getExpandableCallback();
                if (expandableCallback != null){
                    expandableCallback.closeBack();
                }
            }else{
                //展开状态，需要点击收起
                IExpandableCloseCallBack expandableCallback = mTextView.getExpandableCallback();
                if (expandableCallback != null && expandableCallback instanceof IExpandableOpenCallBack){
                    ((IExpandableOpenCallBack)expandableCallback).openBack();
                }
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (mTextView != null){
            ds.setColor(mTextView.getOpenAndCloseTextColor());
        }
        ds.setUnderlineText(false);
    }
}
