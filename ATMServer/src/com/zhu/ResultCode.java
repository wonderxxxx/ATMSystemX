package com.zhu;

public enum  ResultCode {
        SUCCESS, // 操作成功
        NO_CARDNO, // 没有该卡号
        CARD_FREEZE, // 卡号被冻结
        PWD_ERROR, // 密码错误
        TRANSFER_ACCOUNT_FAILED,  // 转账失败
        NSF_CHECK,  // 余额不足
}
