package cn.lwd.expandabletext;

/**
 * 正则常量类
 * Created by liweidong on 2021/4/26.
 */
public interface RegularConstant {

    /**
     * 动态话题正则
     */
    interface DynamicTopic{
        String RULE = "\\[_LinkTopic:.*?,.*?,.*?]";//正则
        String RULE_PREFIX = "[_LinkTopic:";//前缀
        String RULE_SUFFIX = "]";//后缀
        String RULE_ADD = "#";//前后缀添加
    }

}
