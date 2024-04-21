# spring-boot-elaticsearch

`spring-boot` 操作`elaticsearch`，增删改查，封装常用操作

```java
/**
 * 检测索引是否存在
 *
 * @param index 索引名称
 * @return
 */
public static Boolean existsIndex(String index) throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest(index);
    return thisUtil.client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
}

/**
 * 创建 Es 索引
 *
 * @param index   索引名称
 * @param mapping 自定义字段类型映射
 * @return
 * @throws IOException
 */
public static Boolean createEsIndex(String index, Map mapping) throws IOException {
    CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
    if (!mapping.isEmpty()) {
        createIndexRequest.mapping(new HashMap<String, Map>(0) {
            { put("properties", combineIndexMapping(mapping)); }
        });
    }
    CreateIndexResponse createIndexResponse = thisUtil.client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    return createIndexResponse.isAcknowledged();
}

/**
 * 多条件查询
 *
 * @param index   索引名称
 * @param complex 各种查询条件的聚合
 * @param size    需要返回的记录数量
 * @return
 * @throws IOException
 */
public static List<?> query(String index, Map<String, Object> complex, int size) throws IOException {
    SearchSourceBuilder builder = new SearchSourceBuilder().sort((String) complex.get("sort"), SortOrder.DESC).size(size);
    SearchRequest request = new SearchRequest().indices(index).source(builder);
    builder.fetchSource((String[]) complex.get("field"), new String[]{});    // 过滤字段显示字段
    builder.query(combineWhere((Map<String, Object>) complex.get("where"))); // where 条件
    if (complex.containsKey("agg")) {
        Map<String, Object> aggParam = (Map<String, Object>) complex.get("agg");
        builder.size(0).aggregation(combineAggWhere(aggParam, size)); // 组装聚合查询与分组的条件
        return searchAggResult(request, aggParam);
    }
    return searchResult(request, complex);
}

/**
 * 组装普通组合查询结果
 *
 * @param request
 * @param complex
 * @return
 * @throws IOException
 */
private static @NotNull List<?> searchResult(SearchRequest request, Map<String, Object> complex) throws IOException {
    SearchResponse response = thisUtil.client.search(request, RequestOptions.DEFAULT);
    List<JSONObject> result = new ArrayList<>();
    SearchHits hits = response.getHits();
    for (SearchHit hit : hits) {
        result.add(JSON.parseObject(hit.getSourceAsString()));
    }
    return result;
}

/**
 * 查询聚合结果
 *
 * @param request
 * @param complex
 * @return
 * @throws IOException
 */
private static List<?> searchAggResult(SearchRequest request, Map<String, Object> aggParam) throws IOException {
    SearchResponse response = thisUtil.client.search(request, RequestOptions.DEFAULT);

    Aggregations aggregations = response.getAggregations(); // 获取桶（group by）聚合查询结果
    String termsField = (String) aggParam.get("group");     // 桶（group by）聚合的字段
    String[] sumFields = (String[]) aggParam.get("sum");    // 求和的字段集合
    Terms aggResult = (Terms) aggregations.asMap().get(termsField);
    List<? extends Terms.Bucket> buckets = aggResult.getBuckets();

    List<Map<String, Object>> result = new ArrayList<>();
    for (Terms.Bucket bucket : buckets) {
        Map<String, Object> item = new HashMap<>(0);
        item.put(termsField, bucket.getKey()); // 写入 group by 的字段
        // sum 求和的字段可能是多个，所以这里要再做一次循环
        Aggregations bucketAggregations = bucket.getAggregations();
        for (String field : sumFields) {
            ParsedSum sumAmount = bucketAggregations.get(field);
            item.put(field, sumAmount.value());
        }
        result.add(item);
    }
    return result;
}

/**
 * 根据聚合类型 获取对应的聚合builder
 *
 * @param aggParam 聚合参数
 * @return AggregationBuilder
 */
private static AggregationBuilder combineAggWhere(Map<String, Object> aggParam, int size) {
    String termsField = (String) aggParam.get("group");  // 桶（group by）聚合的字段
    String[] sumFields = (String[]) aggParam.get("sum"); // 求和的字段集合
    TermsAggregationBuilder aggregation = AggregationBuilders.terms(termsField).field(termsField).size(size);
    for (String field : sumFields) {
        aggregation.subAggregation(AggregationBuilders.sum(field).field(field));
    }
    return aggregation;
}

/**
 * 组装索引字段映射
 *
 * @param mapping
 * @return
 */
private static Map<String, ?> combineIndexMapping(Map<String, ?> mappData) {
    Map mapping = new HashMap<>(0);
    for (Map.Entry<String, ?> entry : mappData.entrySet()) {
        String field = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof String) {
            mapping.put(field, new HashMap<String, String>(0) {
                { put("type", (String) value); }
            });
        } else if (value instanceof String[] && "created_at".equals(field)) {
            String[] dateValue = (String[]) entry.getValue();
            mapping.put(field, new HashMap<String, String>(0) {{
                put("type", dateValue[0]);
                put("format", dateValue[1]);
            }});
        }
    }
    return mapping;
}

/**
 * 组装复杂的查询条件
 *
 * @param where
 * @return
 */
private static BoolQueryBuilder combineWhere(Map<String, Object> where) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    for (Map.Entry<String, ?> entry : where.entrySet()) {
        String field = entry.getKey();
        Object value = entry.getValue();
        if ("range".equals(field)) {
            boolQueryBuilder.must(combineRangeWhere(value));
        } else if ("in".equals(field)) {
            boolQueryBuilder.must(combineInRangeWhere(value));
        } else if ("not_in".equals(field)) {
            boolQueryBuilder.mustNot(combineInRangeWhere(value));
        } else {
            boolQueryBuilder.must(QueryBuilders.termQuery(field, value));
        }
    }
    return boolQueryBuilder;
}

/**
 * 组装区间查询条件 - range 只允许传一个条件，暂不支持多条件，因为我不知道怎么写
 *
 * @param where
 * @return
 */
private static RangeQueryBuilder combineRangeWhere(Object value) {
    Map<String, String[]> rangeValue = (Map<String, String[]>) value;
    Map.Entry<String, String[]> stringEntry = rangeValue.entrySet().stream().findFirst().get();

    String rangeKey = stringEntry.getKey();
    String[] rangeV = stringEntry.getValue();
    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(rangeKey);
    if (rangeV.length == 1) {
        rangeQueryBuilder.gte(rangeV[0]);
    } else {
        rangeQueryBuilder.gte(rangeV[0]);
        rangeQueryBuilder.lte(rangeV[1]);
    }
    return rangeQueryBuilder;
}

/**
 * 组装 in、not_in 查询条件
 *
 * @param value
 * @return
 */
private static TermsQueryBuilder combineInRangeWhere(Object value) {
    Map<String, String[]> rangeValue = (Map<String, String[]>) value;
    Map.Entry<String, String[]> stringEntry = rangeValue.entrySet().stream().findFirst().get();

    String rangeKey = stringEntry.getKey();
    String[] rangeV = stringEntry.getValue();
    TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(rangeKey, rangeV);
    return termsQueryBuilder;
}
```
