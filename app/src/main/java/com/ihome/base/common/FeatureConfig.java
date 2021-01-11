package com.ihome.base.common;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/3/31 下午5:02
 * <p/>
 * Description:
 */
public class FeatureConfig {
    /**
     * 手机运营商
     */
    public static final int enablemobileServerFeature = 1;
    /**
     * 芝麻信用
     */
    public static final int enablesesameFeature = 1;
    /**
     * 个人信息 - 商汤扫描
     */
    public static final int enablescanFeature = 1;
    /**
     * 支付宝认证
     */
    public static final int enablealipayIdentifyFeature     = 0;
    /**
     * 还款模块
     */
    public static final int enablerepaymentFeature = 1;
    /**
     * 主动还款
     */
    public static final int enableactiveRepaymentFeature = 1;
    /**
     * 自动扣款
     */
    public static final int enableautomaticDeductionFeature = 1;
    /**
     * 支付宝扣款
     */
    public static final int enablealipayDeductionFeature = 1;
    /**
     * 同盾模块
     */
    public static final int enabletongdunModuleFeature = 1;
    /**
     * 公積金
     */
    public static final int enableaccumulationFundFeature   = 0;
    /**
     * 社保
     */
    public static final int enablesecurityFeature           = 0;
    /**
     * 邀请好友
     */
    public static final int enableinviteFeature             = 1;

    public static boolean enableFeature(int feature) {
        return feature == 1;
    }
}
