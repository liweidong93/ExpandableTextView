package cn.lwd.expandabletext.expandable;

import android.text.TextPaint;

/**
 * 展开收起所有回调
 * Created by liweidong on 2021/4/22.
 */
public interface IExpandableCallBack extends IExpandableRegularCallBack {

    /**
     * 正则分段颜色
     * @param ds
     * @param outData
     */
    void updateDrawState(TextPaint ds, String[] outData);

}
