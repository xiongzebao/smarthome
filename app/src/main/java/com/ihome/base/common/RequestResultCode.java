package com.ihome.base.common;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2016/12/7 下午4:03
 * <p>
 * Description: 跳转回掉code类
 */
public class RequestResultCode {

    /** 重新绑定银行卡 */
    public static int REQ_AGAIN_BIND               = 1002;
    public static int RES_AGAIN_BIND               = 1002;
    /**
     * 手机运营商认证
     */
    public static int RES_PHONE = 1004;
    public static int REQ_PHONE                    = 1004;

    /**
     * 芝麻信用认证
     */
    public static int RES_ZMXY = 1006;


    /** 找回密码 */
    public static int REQ_FORGOT                   = 1005;
    public static int RES_FORGOT                   = 1005;

    public static int LF_SCAN_ID_CARD_FRONT_REQUEST = 1007;
    public static int LF_SCAN_ID_CARD_BACK_REQUEST = 1008;

    public static int GO_TO_LIVE_MAP = 1009;
    public static int GO_TO_WORK_MAP = 1010;
    public static int HOME_REQ = 1011;

    public static int REQ_PAY_REC = 1012;
    public static int RES_PAY_REC = 1012;

    /**
     * 附带资料认证
     */
    public static int RES_CHOOSE_PICTURE_HOME = 2001;
    public static int RES_CHOOSE_PICTURE_ACCOUNT = 2002;
    public static int RES_CHOOSE_PICTURE_PRODUCT = 2003;

    public static int RES_TAKE_PICTURE_HOME = 3001;
    public static int RES_TAKE_PICTURE_ACCOUNT = 3002;
    public static int RES_TAKE_PICTURE_PRODUCT = 3003;

}
