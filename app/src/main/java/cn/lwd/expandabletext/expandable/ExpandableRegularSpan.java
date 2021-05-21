package cn.lwd.expandabletext.expandable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 收起展开textview点击
 * Created by liweidong on 2021/4/13.
 */
public class ExpandableRegularSpan extends ClickableSpan {

    private ExpandableTextView mTextView;
    private ExpandableRegularItem mItem;

    public ExpandableRegularSpan(ExpandableTextView textView, ExpandableRegularItem item){
        this.mTextView = textView;
        this.mItem = item;
    }

    @Override
    public void onClick(View widget) {
        if (mTextView != null){
            IExpandableCloseCallBack expandableCallback = mTextView.getExpandableCallback();
            if (expandableCallback != null && expandableCallback instanceof IExpandableRegularCallBack){
                ((IExpandableRegularCallBack)expandableCallback).regularClick(mItem != null ? mItem.outData : null);
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (mTextView != null && mTextView.getRegularTextColor() > 0){
            ds.setColor(mTextView.getRegularTextColor());
        }
        ds.setUnderlineText(false);
        ds.setFakeBoldText(mTextView.isRegularTextBold());
        //自定义颜色
        if (mTextView != null){
            IExpandableCloseCallBack expandableCallback = mTextView.getExpandableCallback();
            if (expandableCallback != null && expandableCallback instanceof IExpandableCallBack){
                ((IExpandableCallBack)expandableCallback).updateDrawState(ds, mItem != null ? mItem.outData : null);
            }
        }
    }
}
