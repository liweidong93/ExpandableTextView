package cn.lwd.expandabletext;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;

import cn.lwd.expandabletext.expandable.ExpandableBuilder;
import cn.lwd.expandabletext.expandable.ExpandableTextView;
import cn.lwd.expandabletext.expandable.IExpandableCallBack;

public class MainActivity extends AppCompatActivity {

    private ExpandableTextView expandableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableTextView = findViewById(R.id.tv_main_expandable);
        setData();
    }

    private void setData(){
        ExpandableBuilder builder = new ExpandableBuilder()
                .closeText("查看全文")
                .openText("收起")
                .limitLineCount(2)
                .ellipsisText("...")
                .openAndCloseTextColor(Color.parseColor("#40609F"))
                .regularMatch(RegularConstant.DynamicTopic.RULE, RegularConstant.DynamicTopic.RULE_PREFIX, RegularConstant.DynamicTopic.RULE_SUFFIX, 0, true)
                .regularPreAdd(RegularConstant.DynamicTopic.RULE_ADD)
                .regularSuffixAdd(RegularConstant.DynamicTopic.RULE_ADD)
                .callBack(new IExpandableCallBack() {
                    @Override
                    public void updateDrawState(TextPaint ds, String[] outData) {
                        //可以给分段的正则匹配文字设置各自的颜色、字重等
                    }

                    @Override
                    public void regularClick(String[] outData) {

                    }

                    @Override
                    public void openBack() {
                        expandableTextView.close();
                    }

                    @Override
                    public void closeBack() {
                        expandableTextView.open();
                    }
                });
        expandableTextView.setConfig(builder);
        expandableTextView.setData("这个控件真的很好用[_LinkTopic:正则,id,9]，你学会了没，也可以不使用正则，看自己需要，可以设置正则文字的点击事件、颜色、并且支持在正则匹配到的文字前后加自定义符号");
    }
}