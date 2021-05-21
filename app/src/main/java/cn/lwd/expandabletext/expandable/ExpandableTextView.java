package cn.lwd.expandabletext.expandable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lwd.expandabletext.R;

/**
 * 自定义展开收起textview控件
 * 宽度必须设置固定值或者match_parent，如果设置宽度为wrap_content默认修改为match_parent
 * Created by liweidong on 2021/4/13.
 */
public class ExpandableTextView extends androidx.appcompat.widget.AppCompatTextView {

    private String mOriText = "";
    private int mLayoutWidth;
    private TextPaint mTextPaint;
    private int mCurState = ExpandableState.STATE_OPEN;//默认展开
    private String mOpenText = "";//展开的显示文本
    private String mCloseText = "";//收缩的显示文本
    private Layout mLayout;
    private int mLimitLineCount;//收起的最大行数
    private ClickableSpan mExpandSpan;
    private int mLineCount;
    private IExpandableCloseCallBack mCallback;
    private static final String ELLIPSIS = "...";//省略号
    private String mEllipsisText = ELLIPSIS;//省略的文本，默认为...
    private int mOpenAndCloseTextColor = Color.parseColor("#3377ff");//收起展开默认颜色
    private String mRegularRule = "";//正则匹配规则
    private String mPrefix = "";//正则前缀替换
    private String mSuffix = "]";//正则后缀替换
    private boolean mIsSubComma;//正则是否截取逗号
    private ArrayList<ExpandableRegularItem> mRegularItem = new ArrayList<>();//正则匹配到的数据
    private int mRegularTextColor = Color.parseColor("#3377ff");//正则字体颜色
    private boolean mIsRegularTextBold;//正则字体是否加粗
    private String mRegularAddPrefix = "";//正则前缀添加的值
    private String mRegularAddSuffix = "";//正则后缀添加的值

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        int limitCount = typedArray.getInt(R.styleable.ExpandableTextView_base_common_limit_line_count, 0);
        if (limitCount > 0){
            this.mLimitLineCount = limitCount;
            this.mCurState = ExpandableState.STATE_CLOSE;
        }
        String openText = typedArray.getString(R.styleable.ExpandableTextView_base_common_open_text);
        if (!TextUtils.isEmpty(openText)){
            this.mOpenText = openText;
        }
        String closeText = typedArray.getString(R.styleable.ExpandableTextView_base_common_close_text);
        if (!TextUtils.isEmpty(closeText)){
            this.mCloseText = closeText;
        }
        String ellipsisText = typedArray.getString(R.styleable.ExpandableTextView_base_common_ellipsis_text);
        if (!TextUtils.isEmpty(ellipsisText)){
            this.mEllipsisText = ellipsisText;
        }
        int state = typedArray.getInt(R.styleable.ExpandableTextView_base_common_default_state, 0);
        if (state == ExpandableState.STATE_OPEN || state == ExpandableState.STATE_CLOSE){
            mCurState = state;
        }
        int color = typedArray.getColor(R.styleable.ExpandableTextView_base_common_open_and_close_textcolor, 0);
        if (color != 0){
            mOpenAndCloseTextColor = color;
        }
        typedArray.recycle();

        mExpandSpan = new ExpandableClickSpan(this);
        /*getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    obs.removeOnGlobalLayoutListener(this);
                }else{
                    obs.removeGlobalOnLayoutListener(this);
                }
                setText(getNewTextByConfig());
            }
        });*/
        post(new Runnable() {
            @Override
            public void run() {
                setText(getNewTextByConfig());
            }
        });
    }

    /**
     * 设置配置，配置必须在settext之前
     * @param builder
     */
    public void setConfig(ExpandableBuilder builder){
        if (builder != null){
            if (builder.getLimitLineCount() > 0){
                this.mLimitLineCount = builder.getLimitLineCount();
                this.mCurState = ExpandableState.STATE_CLOSE;
            }
            if (!TextUtils.isEmpty(builder.getOpenText())){
                this.mOpenText = builder.getOpenText();
            }
            if (!TextUtils.isEmpty(builder.getCloseText())){
                this.mCloseText = builder.getCloseText();
            }
            if (builder.getState() == ExpandableState.STATE_OPEN || builder.getState() == ExpandableState.STATE_CLOSE){
                this.mCurState = builder.getState();
            }
            if (!TextUtils.isEmpty(builder.getEllipsisText())){
                this.mEllipsisText = builder.getEllipsisText();
            }
            if (builder.getCallback() != null){
                this.mCallback = builder.getCallback();
            }
            if (builder.getOpenAndCloseTextColor() != 0){
                this.mOpenAndCloseTextColor = builder.getOpenAndCloseTextColor();
            }
            if (builder.getSpan() != null){
                this.mExpandSpan = builder.getSpan();
            }
            if (!TextUtils.isEmpty(builder.getRegularRule())){
                this.mRegularRule = builder.getRegularRule();
            }
            if (!TextUtils.isEmpty(builder.getPrefix())){
                this.mPrefix = builder.getPrefix();
            }
            if (!TextUtils.isEmpty(builder.getSuffix())){
                this.mSuffix = builder.getSuffix();
            }
            this.mIsSubComma = builder.isSubComma();
            if (builder.getRegularTextColor() > 0){
                this.mRegularTextColor = builder.getRegularTextColor();
            }
            this.mIsRegularTextBold = builder.isBold();
            if (!TextUtils.isEmpty(builder.getPrefixAdd())){
                this.mRegularAddPrefix = builder.getPrefixAdd();
            }
            if (!TextUtils.isEmpty(builder.getSuffixAdd())){
                this.mRegularAddSuffix = builder.getSuffixAdd();
            }
        }
    }

    /**
     * 获取展开收起字体颜色
     * @return
     */
    protected int getOpenAndCloseTextColor(){
        return mOpenAndCloseTextColor;
    }

    /**
     * 获取正则文字颜色
     * @return
     */
    protected int getRegularTextColor(){
        return mRegularTextColor;
    }

    /**
     * 正则文字是否加粗
     * @return
     */
    protected boolean isRegularTextBold(){
        return mIsRegularTextBold;
    }

    /**
     * 展开
     */
    public void open(){
        if (mCurState == ExpandableState.STATE_CLOSE){
            mCurState = ExpandableState.STATE_OPEN;
            setText(getNewTextByConfig());
        }
    }

    /**
     * 收起
     */
    public void close(){
        if (mCurState == ExpandableState.STATE_OPEN){
            mCurState = ExpandableState.STATE_CLOSE;
            setText(getNewTextByConfig());
        }
    }

    /**
     * 获取展开收起状态
     * @return
     */
    protected int getState(){
        return mCurState;
    }

    /**
     * 获取展开收起回调
     * @return
     */
    protected IExpandableCloseCallBack getExpandableCallback(){
        return mCallback;
    }

    /**
     * 设置数据，必须用这个方法设置值
     * @param text
     */
    public void setData(String text){
        mOriText = text;
        //清空正则缓存
        mRegularItem.clear();
        //处理正则
        handleRegular();
        setMovementMethod(ClickableMovementMethod.getInstance());
        setText(getNewTextByConfig());
        setFocusable(false);
        setClickable(false);
    }

    /**
     * 获取文本
     * @return
     */
    private CharSequence getNewTextByConfig(){
        if (TextUtils.isEmpty(mOriText)){
            return "";
        }
        //获取layout对象
        mLayout = getLayout();
        if (mLayout != null){
            //获取控件宽度
            mLayoutWidth = mLayout.getWidth();
        }
        //计算宽度
        if (mLayoutWidth <= 0){
            //如果控件宽度为0，则不支持展开收起
            if (getWidth() == 0){
                return setRegularSpan(new SpannableString(mOriText));
            }else{
                mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            }
        }else{
            mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        }
        mTextPaint = getPaint();
        switch (mCurState){
            case ExpandableState.STATE_CLOSE://收起
                //如果有图片
                /*if (mDrawableLeftWidth > 0){
                    float spaceWidth = mTextPaint.measureText(" ");
                    float spaceCount = mDrawableLeftWidth / spaceWidth;
                    int spaceSize = (int) (spaceCount + 1);
                    mOriText = getSpaceCountStr(spaceSize) + mOriText;
                }*/
                mLayout = new DynamicLayout(mOriText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
                mLineCount = mLayout.getLineCount();
                //如果行数大于限制行数，并且收缩的文本不为空
                SpannableStringBuilder closeSpannableString;
                if (mLineCount > mLimitLineCount){
                    //获取最后显示字符的下标
                    int lineEnd = getValidLayout().getLineEnd(mLimitLineCount - 1);
                    int lineStart = getValidLayout().getLineStart(mLimitLineCount - 1);
                    int indexEnd = lineEnd - getLengthOfString(mCloseText) - getLengthOfString(mEllipsisText);
                    if (indexEnd <= lineStart){
                        indexEnd = lineEnd;
                    }
                    //计算留下的的宽度
                    int remainWidth = getValidLayout().getWidth() - (int)(mTextPaint.measureText(mOriText.subSequence(lineStart, indexEnd).toString()));
                    //需要的宽度
                    int needWidth = (int) mTextPaint.measureText(mCloseText) + (int)mTextPaint.measureText(mEllipsisText) + (int)(mTextPaint.measureText("中") / 2);//添加半个【中】的中文字符占位
                    int indexEndRevised = indexEnd;
                    //留下的宽度大于需要的宽度
                    if (remainWidth > needWidth){
                        //判断上一个字符是否为空行
                        try{
                            int extraOffset = 0;
                            int extraWidth = 0;
                            //微调字符，减少多余空间
                            while (remainWidth > needWidth + extraWidth){
                                char c = mOriText.charAt(indexEnd + extraOffset - 1);
                                if (c == '\n'){
                                    //如果是空行，则不进行缩进计算
                                    break;
                                }else{
                                    extraOffset++;
                                    if (indexEnd + extraOffset <= mOriText.length()){
                                        extraWidth = (int) mTextPaint.measureText(mOriText.subSequence(indexEnd, indexEnd + extraOffset).toString());
                                    }else{
                                        break;
                                    }
                                }
                            }
                            indexEndRevised += extraOffset - 1;
                        }catch (Exception e){
                            e.printStackTrace();
                            int extraOffset = 0;
                            int extraWidth = 0;
                            //微调字符，减少多余空间
                            while (remainWidth > needWidth + extraWidth){
                                extraOffset++;
                                if (indexEnd + extraOffset <= mOriText.length()){
                                    extraWidth = (int) mTextPaint.measureText(mOriText.subSequence(indexEnd, indexEnd + extraOffset).toString());
                                }else{
                                    break;
                                }
                            }
                            indexEndRevised += extraOffset - 1;
                        }
                    }else{
                        //留下的宽度不够需要的宽度
                        int extraOffset = 0;
                        int extraWidth = 0;
                        //微调字符，补足不够的空间
                        while (remainWidth + extraWidth < needWidth){
                            extraOffset--;
                            if (indexEnd + extraOffset > lineStart){
                                extraWidth = (int) mTextPaint.measureText(mOriText.subSequence(indexEnd + extraOffset, indexEnd).toString());
                            }else{
                                break;
                            }
                        }
                        indexEndRevised += extraOffset;
                    }
                    //获取到需要展示的text
                    String fixText = removeEndLineBreak(mOriText.subSequence(0, indexEndRevised));
                    closeSpannableString = new SpannableStringBuilder(fixText);
                    closeSpannableString.append(mEllipsisText);
                    closeSpannableString.append(mCloseText);
                    closeSpannableString.setSpan(mExpandSpan, closeSpannableString.length() - getLengthOfString(mCloseText), closeSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    closeSpannableString = new SpannableStringBuilder(mOriText);
                }
                return setRegularSpan(new SpannableString(closeSpannableString));
            case ExpandableState.STATE_OPEN://展开
                mLayout = new DynamicLayout(mOriText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
                mLineCount = mLayout.getLineCount();
                //如果行数大于最大行数的话，则判断是否结尾要添加收起之类的文案
                SpannableStringBuilder spannableString = new SpannableStringBuilder(mOriText);
                if (mLineCount > mLimitLineCount && !TextUtils.isEmpty(mOpenText)){
                    spannableString = new SpannableStringBuilder(mOriText);
                    spannableString.append(mOpenText);
                    spannableString.setSpan(mExpandSpan, spannableString.length() - getLengthOfString(mOpenText), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return setRegularSpan(new SpannableString(spannableString));
                }else{
                    return setRegularSpan(new SpannableString(spannableString));
                }
        }
        return setRegularSpan(new SpannableString(mOriText));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int specSize = View.MeasureSpec.getSize(widthMeasureSpec);//单位是像素，不包括补距离padding
        if (specMode == View.MeasureSpec.AT_MOST){
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(specSize, View.MeasureSpec.EXACTLY);
        }
        //设置view的大小,方法内部会给measuredWidth和measuredHeight赋值
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取字符的长度
     * @param str
     * @return
     */
    private int getLengthOfString(String str){
        if (TextUtils.isEmpty(str)){
            return 0;
        }
        return str.length();
    }

    /**
     * 移除最后的空行
     * @param text
     * @return
     */
    private String removeEndLineBreak(CharSequence text) {
        String str = text.toString();
        while (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 获取layout对象
     * @return
     */
    private Layout getValidLayout(){
        if (mLayout != null){
            return mLayout;
        }else{
            return getLayout();
        }
    }

    /**
     * 是否是收起状态
     * @return
     */
    public boolean isClose(){
        if (mCurState == ExpandableState.STATE_CLOSE){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 处理正则
     */
    private void handleRegular() {
        if (TextUtils.isEmpty(mRegularRule) || TextUtils.isEmpty(mOriText)){
            return;
        }
        Pattern pattern = Pattern.compile(mRegularRule);
        Matcher matcher = pattern.matcher(mOriText);
        while (matcher.find()) {
            String group = matcher.group();
            int index = mOriText.indexOf(group);
            if (index != -1) {
                String matchText = group.replace(mPrefix, "").replace(mSuffix, "");
                //判断是否截取逗号，输出id
                if (mIsSubComma){
                    if (!TextUtils.isEmpty(matchText)){
                        String[] split = matchText.split(",");
                        if (split.length >= 1){
                            String content = mRegularAddPrefix + split[0] + mRegularAddSuffix;
                            ExpandableRegularItem item = new ExpandableRegularItem();
                            item.content = content;
                            item.outData = split;
                            item.start = index;
                            mRegularItem.add(item);
                            mOriText = mOriText.replaceFirst(mRegularRule, content);
                        }
                    }
                }else{
                    ExpandableRegularItem item = new ExpandableRegularItem();
                    item.content = matchText;
                    item.outData = null;
                    item.start = index;
                    mRegularItem.add(item);
                    mOriText = mOriText.replaceFirst(mRegularRule, matchText);
                }
            }
        }
    }

    /**
     * 设置正则点击span
     * @param spannableString
     * @return
     */
    private SpannableString setRegularSpan(SpannableString spannableString){
        //判断是否包含正则
        if (isContainRegular()){
            for (ExpandableRegularItem item : mRegularItem){
                if (item != null && !TextUtils.isEmpty(item.content) && item.start <= spannableString.length() && (item.start + item.content.length()) <= spannableString.length())
                spannableString.setSpan(new ExpandableRegularSpan(this, item), item.start,
                        item.start + item.content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * 是否需要处理正则
     * @return
     */
    private boolean isContainRegular(){
        if (!TextUtils.isEmpty(mRegularRule) && !TextUtils.isEmpty(mOriText) && mRegularItem != null && mRegularItem.size() != 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取spacesize个空格的string
     * @param spaceSize
     * @return
     */
    private String getSpaceCountStr(int spaceSize){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < spaceSize; i++){
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 展开收起状态
     */
    public interface ExpandableState{
        int STATE_CLOSE = 1;//收起
        int STATE_OPEN = 2;//展开
    }

}

