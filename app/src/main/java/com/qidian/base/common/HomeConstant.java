package com.qidian.base.common;

/**
 * app首页描述
 * 
 * @version 2.0
 * @author yuanpei
 */
public class HomeConstant {


	//人民币标识
	public static final String MONEY_MARK = "￥";

	/**
	 * 卡片头部描述
	 */
	public static final String CARD_NO_BORROW_TITLE = "主力推荐";


	public static final String CARD_BORRROW_TITLE = "我的借款";



	/**
	 * 首页卡片借款进度title
	 */
	//借款进度 -审核中
	public static final String BORROW_PROGRESS_IN_REVIEW_TITLE = "已申请-审核中";

	//借款进度描述 - 审核中
	public static final String BORROW_PROGRESS_IN_REVIEW_DESC = "您的申请已提交,请耐心等待审核";

	//借款进度 -待放款
	public static final String BORROW_PROGRESS_PENDING_MONEY_TITLE = "审核通过-待放款";

	//借款进度描述 - 待放款
	public static final String BORROW_PROGRESS_PENDING_MONEY_DESC = "您的申请已通过审核,请耐心等待放款";


	//借款进度 -待提现
	public static final String BORROW_PROGRESS_WAIT_WITHDRAW_TITLE = "放款成功-待提现";

	//借款进度描述 - 待提现
	public static final String BORROW_PROGRESS_WAIT_WITHDRAW_DESC = "您的借款已打入电子账户,点击按钮提现至银行卡";

	//还款  -未还款
	public static final String REPAY_NO_TITLE = "未还款";

	//还款  -还款中
	public static final String REPAYMENTS_TITLE = "还款中";

	//还款  -已逾期
	public static final String REPAY_OVERDUE_TITLE = "已逾期";

	//机审被拒
	public static final String BORROW_REFUSE_MACHINE_DESC = "您的申请无法通过审核";

	//人工审被拒
	public static final String BORROW_REFUSE_PERSON_DESC = "我们有更适合您的借款产品,满足您的要求";

	/**
	 * 首页button
	 */
	public static final String APPLY_BUTTON_DESC = "立即申请";

	public static final String LOOK_PROGRESS_BUTTON_DESC = "查看进度";

	public static final String WITHDRAW_BUTTON_DESC = "提现";

	public static final String WITHDRAW_BUTTON_WAITING = "提现中";

	public static final String REPAY_BUTTON_DESC = "立即还款";

	public static final String MORE_BUTTON_DESC = "查看更多";

	public static final String BORROW_AGAIN_BUTTON_DESC = "再次借款";


	/**
	 * 首页卡片字段
	 */

	/**
	 * 借款
	 */
	//金额描述
	public static final String MONEY_DESC = "金额";

	//借款期限描述
	public static final String LOAN_LIMIT_DESC = "期限";

	//借款时间描述
	public static final String LOAN_TIME_DESC = "借款时间";

	/**
	 * 还款
	 */

	public static final String REPAY_DESC = "应还";

	public static final String REPAY_TIME_DESC = "应还时间";


	/**
	 * 首页code
	 */

	//未登录
	public static final String NO_login_In = "1000";

	//未实名认证
	public static final String NO_ID_AUTH = "2000";

	//未认证完
	public static final String NO_AUTH = "2500";

	//需要签约
	public static final String SIGN_UP = "2600";

	//借款进度
	public static final String BORROW_PROGRESS = "3000";

	//待放款
	public static final String WAIT_BORROW_LOAN_ = "3001";


	//提现
	public static final String WITHDRAW = "3002";

	//提现中
	public static final String WITHDRAW_WAIT = "3003";


	//立即还款
	public static final String DOREPAY = "4000";

	//还款中
	public static final String REPAY_WAIT = "4001";

	//机审被拒
	public static final String BORROW_REFUSE_MECHINE = "5000";

	//人工复审被拒
	public static final String BORROW_REFUSE_PERSON = "5001";

	//人工复审被拒(用户额度不满意)
	public static final String BORROW_REFUSE_PERSON_SPECIAL = "5002";

	//人工复审被驳回
	public static final String BORROW_RECALL = "5003";

	//重新借款
	public static final String BORROW_AGAIN = "6000";


    /**
     * app首页的借款进度中文状态
     * @param state
     * @return
     */
    //订单审核描述
    public static final String BORROW_AUDIT = "订单审核";

//    public static String apiBorrowProgressState(String state) {
//        String stateStr = state;
//        //'订单状态 10-待机审  11 - 机审拒绝 18- 待人工审核 19-人工审核不通过 20-待上标审核  21上标审核中  22待放款审核
//        //26放款审核通过 27放款审核不通过  30-待还款 31-放款失败 40-已还款 41减免还款 50已逾期 90 坏账',
//        if (BorrowModel.STATE_PRE.equals(state)) {
//            stateStr = "已申请,待审核";
//        } else if (BorrowModel.STATE_PRE_BID.equals(state)) {
//            stateStr = "订单审核";
//        } else if (BorrowModel.STATE_BID_AUDIT.equals(state)) {
//            stateStr = "订单审核";
//        } else if (BorrowModel.STATE_NEED_REVIEW.equals(state)) {
//            stateStr = "订单审核";
//        } else if (BorrowModel.STATE_AUDIT_PASS.equals(state)) {
//            stateStr = "订单审核";
//        } else if (BorrowModel.STATE_AUDIT_REFUSED.equals(state)) {
//            stateStr = "审核被拒";
//        } else if (BorrowModel.STATE_PASS.equals(state)){
//            stateStr = "已审核通过,待放款";
//        } else if (BorrowModel.STATE_REFUSED.equals(state)){
//            stateStr = "审核被拒";
//        }else if (BorrowModel.STATE_REMIT.equals(state)){
//            stateStr = "已放款,待提现";
//        } else if (BorrowModel.STATE_REPAY.equals(state)) {
//            stateStr = "已提现";
//        } else if (BorrowModel.STATE_MACHINE_FAILED.equals(state)) {
//            stateStr = "审核被拒";
//        }
//        return stateStr;
//    }

}

