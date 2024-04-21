package com.example.elasticsearch.service.impl;

import com.example.elasticsearch.service.IndexService;
import com.example.elasticsearch.utils.EsUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);

        Map<String, String> mappData = new HashMap(0) {{
            put("id", "keyword");
            put("name", "keyword");
            put("age", "integer");
            put("city", "keyword");
        }};

        Map properties = new HashMap<>(0);
        for (Map.Entry<String, String> entry : mappData.entrySet()) {
            properties.put(entry.getKey(), new HashMap<String, String>(0) {
                { put("type", entry.getValue()); }
            });
        }

        Map mapping = new HashMap<>(0);
        mapping.put("properties", properties);

        createIndexRequest.mapping(new HashMap<String, Map>(0) {
            { put("properties", properties); }
        });

        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    /**
     * 查询索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public String[] queryIndex(String index) throws IOException {
        GetIndexRequest queryIndexRequest = new GetIndexRequest(index);
        GetIndexResponse getIndexResponse = client.indices().get(queryIndexRequest, RequestOptions.DEFAULT);
        return getIndexResponse.getIndices();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return deleteIndexResponse.isAcknowledged();
    }

}
