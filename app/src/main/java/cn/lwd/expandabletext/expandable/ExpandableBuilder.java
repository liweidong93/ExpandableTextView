package cn.lwd.expandabletext.expandable;

import android.text.style.ClickableSpan;

/**
 * 展开收起控件builder
 * Created by liweidong on 2021/4/16.
 */
public class ExpandableBuilder {

    private int limitLineCount;//限制收缩行数
    private String openText;//展开的显示文案
    private String closeText;//收起的显示文案
    private int state;//默认状态
    private String ellipsisText;//...文本，默认...
    private IExpandableCloseCallBack callback;
    private int openAndCloseTextColor;
    private ClickableSpan span;
    private String regularRule;
    private String prefix;
    private String suffix;
    private boolean isSubComma;
    private int regularTextColor;//正则颜色
    private boolean isBold;//是否加粗
    private String prefixAdd;//正则前缀添加的值，默认不添加
    private String suffixAdd;//正则后缀添加的值，默认不添加

    /**
     * 设置收起的限定行数
     * @param limitLineCount
     * @return
     */
    public ExpandableBuilder limitLineCount(int limitLineCount){
        this.limitLineCount = limitLineCount;
        return this;
    }

    /**
     * 展开时显示的文本
     * @param openText
     * @return
     */
    public ExpandableBuilder openText(String openText){
        this.openText = openText;
        return this;
    }

    /**
     * 收起时展示的文本
     * @param closeText
     * @return
     */
    public ExpandableBuilder closeText(String closeText){
        this.closeText = closeText;
        return this;
    }

    /**
     * 默认的状态，收起或者展开，控件默认为展开
     * @param state
     * @return
     */
    public ExpandableBuilder state(int state){
        this.state = state;
        return this;
    }

    /**
     * 结尾...
     * @param ellipsisText
     * @return
     */
    public ExpandableBuilder ellipsisText(String ellipsisText){
        this.ellipsisText = ellipsisText;
        return this;
    }

    /**
     * 展开收缩文案颜色
     * @param color
     * @return
     */
    public ExpandableBuilder openAndCloseTextColor(int color){
        this.openAndCloseTextColor = color;
        return this;
    }

    /**
     * 展开收起回调
     * @param callback
     * @return
     */
    public ExpandableBuilder callBack(IExpandableCloseCallBack callback){
        this.callback = callback;
        return this;
    }

    /**
     * 支持自定义收缩文本的clickspan
     * @param span
     * @return
     */
    public ExpandableBuilder expandableClickSpan(ClickableSpan span){
        this.span = span;
        return this;
    }

    /**
     * 正则匹配
     * @param regularRule
     * @param prefix  前缀替换，默认]号之前的
     * @param suffix  后缀替换，默认]号
     * @param regularTextColor  正则字体颜色设置
     * @param isSubComma  是否截取逗号
     * @return
     */
    public ExpandableBuilder regularMatch(String regularRule, String prefix, String suffix, int regularTextColor, boolean isSubComma){
        this.regularRule = regularRule;
        this.prefix = prefix;
        this.suffix = suffix;
        this.regularTextColor = regularTextColor;
        this.isSubComma = isSubComma;
        return this;
    }

    /**
     * 是否加粗
     * @param isBold
     * @return
     */
    public ExpandableBuilder regularTextBold(boolean isBold){
        this.isBold = isBold;
        return this;
    }

    /**
     * 正则前缀添加值
     * @param prefixAdd
     * @return
     */
    public ExpandableBuilder regularPreAdd(String prefixAdd){
        this.prefixAdd = prefixAdd;
        return this;
    }

    /**
     * 正则后缀添加值
     * @param suffixAdd
     * @return
     */
    public ExpandableBuilder regularSuffixAdd(String suffixAdd){
        this.suffixAdd = suffixAdd;
        return this;
    }

    //====================  get方法 start  =====================
    protected int getLimitLineCount() {
        return limitLineCount;
    }

    protected String getOpenText() {
        return openText;
    }

    protected String getCloseText() {
        return closeText;
    }

    protected int getState() {
        return state;
    }

    protected String getEllipsisText() {
        return ellipsisText;
    }

    protected int getOpenAndCloseTextColor() {
        return openAndCloseTextColor;
    }

    protected IExpandableCloseCallBack getCallback() {
        return callback;
    }

    protected ClickableSpan getSpan() {
        return span;
    }

    protected String getRegularRule() {
        return regularRule;
    }

    protected String getPrefix() {
        return prefix;
    }

    protected String getSuffix() {
        return suffix;
    }

    protected boolean isSubComma() {
        return isSubComma;
    }

    protected int getRegularTextColor() {
        return regularTextColor;
    }

    protected boolean isBold() {
        return isBold;
    }

    protected String getPrefixAdd() {
        return prefixAdd;
    }

    protected String getSuffixAdd() {
        return suffixAdd;
    }

    //====================  get方法 start  =====================
}
