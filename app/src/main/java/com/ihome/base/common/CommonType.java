package com.ihome.base.common;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/28 下午4:21
 * <p/>
 * Description: 备注类型
 */
public class CommonType {
    public static String BANKREMARK        = "remark_bank_card";
    public static String PROTOCOL_REGISTER = "protocol_register";       //注册协议
    public static String REPAY_TYPE        = "fq_h5_repay_type";       //还款方式
    public static String INVITE_RULE       = "h5_invite_rule";       //邀请规则
    public static String HELP_REPAY        = "fq_h5_repay_help";       //还款帮助
    public static String HELP              = "fq_h5_help";       //帮助中心
    public static String ABOUNT_US         = "fq_h5_about_us";       //关于我们
    public static String OFFICIAL_NOTICE         = "fq_h5_notice_list";       //官方公告

    //-------------验证码获取类型code-------------
    public static String REGISTER_CODE     = "register";//注册获取
    public static String FORGOT_CODE       = "findReg";//找回登陆密码获取
    public static String BIND_CARD_CODE    = "bindCard";//绑定银行卡
    public static String PAY_CODE          = "findPay";//找回交易密码
    public static String PROTOCOL_BORROW          = "protocol_borrow";//借款协议
    public static String WITHDRAW_CONFIRM          = "withdrawConfirm";//借款确认

    public static String getUrl(String url) {
        return BaseParams.URI_AUTHORITY + url;
    }
}
