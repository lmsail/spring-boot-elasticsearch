package com.example.elasticsearch.constant;

/**
 * 一些常量
 *
 * @author 特工007
 * @date 2022/5/4 6:08 PM
 */
public interface Constant {
    String INDEX = "user_account";

    String USER_ACCOUNT = "user_account"; // 用户账户记录 - 记录用户相关信息

    String USER_SCORE = "user_score_log"; // 积分余额变动记录

    String USER_BANK_SCORE = "user_bank_score_log"; // 庄积分变动记录

    String GAME_SETTLE = "game_settle_log"; // 记录每局详细扣分、抽水

    String GAME_REALTIME = "game_realtime"; // 平台实时流水、输赢
}