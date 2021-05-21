package cn.lwd.expandabletext.expandable;

/**
 * 正则回调
 * Created by liweidong on 2021/4/26.
 */
public interface IExpandableRegularCallBack extends IExpandableOpenCallBack {

    /**
     * 正则点击输出
     * @param outData  内容逗号分隔，数组形式输出
     */
    void regularClick(String[] outData);

}
