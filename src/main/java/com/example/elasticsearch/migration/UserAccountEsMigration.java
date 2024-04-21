package com.example.elasticsearch.migration;

import com.example.elasticsearch.constant.Constant;
import com.example.elasticsearch.entity.EsComplex;
import com.example.elasticsearch.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Es 账户记录
 *
 * @author 特工007
 * @date 2022/5/5 12:12 PM
 */
public class UserAccountEsMigration {

    static final String ES_KEY = Constant.USER_ACCOUNT;

    static Logger logger = LoggerFactory.getLogger(UserAccountEsMigration.class);

    public static void indexInit() throws IOException {
        if (EsUtil.existsIndex(ES_KEY)) { return; }
        Map<String, ?> mappData = new HashMap(0) {{
            put("account", "keyword");
            put("user_id", "keyword");
            put("score", "double");
            put("created_at", new String[]{"date", "yyyy-MM-dd HH:mm:ss"});
        }};
        EsUtil.createEsIndex(ES_KEY, mappData);
        logger.info("账户明细[" + ES_KEY + "] - 索引不存在，已创建.");
    }
}
