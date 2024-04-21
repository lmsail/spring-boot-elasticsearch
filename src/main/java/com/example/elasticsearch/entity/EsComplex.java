package com.example.elasticsearch.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Es complex 参数封装
 *
 * EsComplex esComplex = new EsComplex();
 * esComplex.setField(new String[]{"user_id", "score_amount"})
 *          .setTerm("unique_key", "ABCDEFGHIJKL")
 *          .setRange("created_at", new String[]{"2022-05-03 18:30:34", "2022-05-06 18:35:34"})
 *          .setIn("user_id", new String[]{"22", "23", "24"})
 *          //.setAgg("user_id", new String[]{"score_amount", "score_up_amount"})
 *          .setSort("score_amount");
 * List<?> result1 = EsUtil.query(Constant.USER_ACCOUNT, esComplex.combineComplex(), 100);
 *
 * @author 特工007
 * @date 2022/5/6 1:37 PM
 */
@Data
public class EsComplex {

    String[] field = new String[0]; // 查询结果中要显示的字段

    Map<String, Object> where = new HashMap<>(); // where 条件

    Map<String, String[]> term = new HashMap<>(); // where -> term 精准查询条件

    Map<String, String[]> range = new HashMap<>(); // where -> range 条件

    Map<String, String[]> in = new HashMap<>();    // where -> in 条件

    Map<String, String[]> notIn = new HashMap<>(); // where -> not_in 条件

    Map<String, Object> agg = new HashMap<>(); // 聚合查询的条件

    String sort = "created_at"; // 排序字段 - 聚合查询下，此排序将不起作用

    /**
     * 组装查询结果中要显示的字段
     *
     * @param range
     * @return
     */
    public EsComplex setField(String[] fields) {
        this.field = fields;
        return this;
    }

    /**
     * 组装精准查询字段
     *
     * @param term
     * @return
     */
    public EsComplex setTerm(String key, Object value) {
        this.where.put(key, value);
        return this;
    }

    /**
     * 组装区间查询条件
     *
     * @param range
     * @return
     */
    public EsComplex setRange(String key, String[] range) {
        Map<String, String[]> rangeWhere = new HashMap<>(1);
        rangeWhere.put(key, range);
        this.where.put("range", rangeWhere);
        return this;
    }

    /**
     * 组装 in 查询条件
     *
     * @param range
     * @return
     */
    public EsComplex setIn(String key, String[] inList) {
        Map<String, String[]> inWhere = new HashMap<>(1);
        inWhere.put(key, inList);
        this.where.put("in", inWhere);
        return this;
    }

    /**
     * 组装 not_in 查询条件
     *
     * @param range
     * @return
     */
    public EsComplex setNotIn(String key, String[] notInList) {
        Map<String, String[]> notInWhere = new HashMap<>(1);
        notInWhere.put(key, notInList);
        this.where.put("not_in", notInWhere);
        return this;
    }

    /**
     * 组装桶聚合条件
     *
     * @param field
     * @return
     */
    public EsComplex setAgg(String groupField, String[] sumList) {
        if (!groupField.isEmpty()) {
            this.agg.put("group", groupField);
        }
        this.agg.put("sum", sumList);
        return this;
    }

    /**
     * 组装查询条件
     *
     * @return
     */
    public Map<String, Object> combineComplex() {
        Map<String, Object> complex = new HashMap<>(0);
        if  (this.field.length > 0) {
            complex.put("field", field);
        }
        complex.put("where", where);
        if (!this.agg.isEmpty()) {
            complex.put("agg", agg);
        }
        complex.put("sort", this.sort);
        return complex;
    }
}
