#### Elasticsearch6.3整合，与JavaRestClient API介绍

#### 整合
##### 概述
Rest Client 分成两部分：
- Java Low Level REST Client
- Java High Level REST Client
- [ES官方文档地址](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/index.html)

##### High Level Client
High Client 基于Low Client，主要目的是暴露一些API

##### Maven 配置
```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>6.3.2</version>
</dependency>
```

##### 初始化
```java
RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(
                new HttpHosst("localhost", 9200, "http"), new HttpHost("localhost", 9210), "http"
        ));

// Do 
client.close();
```

#### [Document API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-supported-apis.html)
##### API
- 单文档API
    - index API
    - Get API
    - Delete API
    - Update API
- 多文档API
    - Bulk API
    - Multi-Get API
    
##### Index API
```java
// Json源
IndexRequest request = new IndexRequest("index", "type", "documentId");
String jsonString = "{" +
        "\"user\":\"kimchy\"," +
        "\"postDate\":\"2013-01-30\"," +
        "\"message\":\"trying out Elasticsearch\"" +
        "}";
request.source(jsonString, XContentType.json);
```
```java
// 文档源
Map<String, Object> jsonMap = new HashMap<>();
jsonMap.put("user", "kimchy");
jsonMap.put("postDate", new Date());
jsonMap.put("message", "trying out Elasticsearch");
IndexRequest request = new IndexRequest("index", "type", "documentId").source(jsonMap);
```
```java
// XContentBuilder构建
XContentBuilder builder = XContentBuilder.jsonBuilder();
builder.startObject();
builder.field("user", "kimchy");
builder.timeField("postDate", new Date());
builder.field("message", "trying out Elasticsearch");
builder.endObject();
IndexRequest request = new IndexRequest("index", "type", "documentId").source(builder);
```
```java
// Object键对
IndexRequest request = new IndexRequest("index", "type", "documentId")
            .source("suer", "Kimchy",
        "postDate", new Date(),
        "message", "trying out Elasticsearch");
```

##### 同步索引
```java
IndexResponse response = client.index(request);
```
##### 异步索引
```java
IndexResponse response = client.indexAsync(request, new ActionListener<IndexResponse>(){
    @Override
    public void onResponse(IndexResponse indexResponse) {
        // success do something
        }
    @Override
    public void onFailure(Exception exception) {
        // failure do something
        }
        });
```
##### IndexResponse
```java
// response 为IndexRequest请求后的返回对象
String index = response.getIndex();
String type = response.getType();
String id = response.getId();
long version = response.getVersion();
if (response.getResult == DocWriteResponse.Result.CREATED) {
    // 文档第一次创建
} else if (response.getResult == DocWriteResponse.Result.UPDATED) {
    // 文档覆盖    
}
// 分片信息
ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
    // 成功的分片数量少于总分片数量
}
if (shardInfo.getFailed() > 0) {
    for (ReplicationResponse.ShardInfo.Failure failure: shardInfo.getFailures()) {
        String reason = failure.reason();        
    }
}
```
```java
// 加入版本号时，如果有冲突的情况
IndexRequest request = new IndexRequest("index", "type", "id").source("xx", "xxx").version(1);

try {
    IndexResponse response = client.index(request);
} catch(Exception e) {
    if (e.status() == RestStatus.CONFLICT) {
    // 冲突了    
    }
}
```
```java
// 可以通过设置opType:来判断文档时创建还是更新
IndexRequest request = new IndexRequest("posts", "doc", "1")
    .source("field", "value")
    .opType(DocWriteRequest.OpType.CREATE);
    try {
    IndexResponse response = client.index(request);
    } catch(ElasticsearchException e) {
    if (e.status() == RestStatus.CONFLICT) {

    }
}
```

##### Get API
Get请求的三个参数：（7以后没有type了）
- Index
- Type
- Document Id
```java
GetRequest request = new GetRequest("index", "type", "id");
```
- [可选参数](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-get.html)
- 可选参数
    - 不获取源数据
    ```java
    request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
    ```
    - 配置返回数据中指定字段
    ```java
    String[] includes = new String[]{"xx", "xxx"};
    String[] excludes = Strings.EMPTY_ARRAY;
    FetchSourceContext fetch = new FetchSourceContext(true, includes, excludes);
    request.fetchSourceContext(fetch);
    ```
    - 配置返回数据中排除指定字段
    ```java
    String[] includes = Strings.EMPTY_ARRAY;
    String[] excludes = new String[]{"xx", "xxx"};
    FetchSourceContext fetch = new FetchSourceContext(true, includes, excludes);
    request.fetchSourceContext(fetch);
    ```
    - 实时 默认为true
    ```java
    request.realtime(true);
    ```
    - 版本
    ```java
    request.version(versionId);
    ```
    - 版本类型
    ```java
    request.versionType(VersionType.EXTERNAL);
    ```
##### 同步与异步执行
与Index类型，不再重复

##### Get Response
```java
if(response.isExists()) {
    String index = response.getIndex();
    String type = response.getType();
    String id = response.getId();
    long version = response.getVersion();
    String sourceAsString = response.getSourceAsString();
    Map<String, Object> sourceAsMap = response.getSourceAsMap();
    byte[] sourceAsBytes = response.getSourceAsBytes(); 
}
```

##### [Exists API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-exists.html#java-rest-high-document-exists-request)
使用GetRequest进行的，调用client的exists()方法判断是否存在

相应的也有判断Index是否存在的exists()方法
```java
GetRequest getRequest = new GetRequest(
    "posts",  // Index
    "doc",    // Type
    "1");     // Document id
getRequest.fetchSourceContext(new FetchSourceContext(false));  // 禁用 _source 字段
getRequest.storedFields("_none_"); // 禁止存储任何字段 
boolean exists = client.exists(getRequest);
```

##### [Delete API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-delete.html)
```java
DeleteRequest request = new DeleteRequest(
        "posts",   // index 
        "doc",     // doc
        "1");      // document id
// 配置超时时间（2选1）
request.timeout(TimeValue.timeValueMinutes(1));
request.timeout("2m");

// 刷新策略（2选1）
request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
request.setRefreshPolicy("wait_for");

// 版本
request.version(versionId);

// 版本类型
request.versionType(VersionType.EXTERNAL);

// 同步执行
DeleteResponse response = client.delete(request);
// 异步执行
response = client.deleteAsync(request, new ActionListener<>(){});

// 返回内容
if (response.getResult == DocWriteResponse.Result_NOT_FOUND) {
    // 文档不存在
    return ;
}
String index = response.getIndex();
String type = response.getType();
String id = response.getId();
long version = response.getVersion();
ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
// 成功分片数目小于总分片
}
if (shardInfo.getFailed() > 0) {
for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
String reason = failure.reason(); // 处理潜在失败
    }
}
```
##### [Update API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-update.html)
```java
// 部分文档更新时，已存在文档与部分文档会合并
// 文档构建格式见IndexAPI
UpdateRequest request = new UpdateRequest("posts", "doc", "1")
        .doc("updated", new Date(),"reason", "daily update");

// 可选参数
// 配置超时时间（2选1）
request.timeout(TimeValue.timeValueMinutes(1));
request.timeout("2m");

// 刷新策略（2选1）
request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
request.setRefreshPolicy("wait_for");

// 冲突后重试次数
request.retryOnConflict(3);

// 获取数据源（默认开启）
request.fetchSource(true);

// 配置返回的数据内容
request.fetchSourceContext(new FetchSourceContext(true, includes, excludes));

// 版本
request.version(versionId);

// 版本类型
request.versionType(VersionType.EXTERNAL);

// 设置文档不存在时更新
request.docAsUpsert(true);

UpdateResponse updateResponse = client.update(request);

// 返回
String index = updateResponse.getIndex();
String type = updateResponse.getType();
String id = updateResponse.getId();
long version = updateResponse.getVersion();
if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
// 已创建
} else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
// 已更新
} else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
// 已删除
} else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
// 不受影响
}
```

##### [Bulk API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-bulk.html)
```java
BulkRequest request = new BulkRequest();
request.add(new DeleteRequest("posts", "doc", "3")); 
request.add(new UpdateRequest("posts", "doc", "2") 
        .doc(XContentType.JSON,"other", "test"));
request.add(new IndexRequest("posts", "doc", "4")  
        .source(XContentType.JSON,"field", "baz"));
// 可选参数见官方文档

BulkResponse bulkResponse = client.bulk(request);

for (BulkItemResponse bulkItemResponse : bulkResponse) { // 遍历所有的操作结果

    if (bulkItemResponse.isFailed()) { // 检测给定的操作是否失败
        BulkItemResponse.Failure failure = bulkItemResponse.getFailure(); // 获取失败信息
        continue;
    }
    DocWriteResponse itemResponse = bulkItemResponse.getResponse(); // 获取操作结果的响应，可以是  IndexResponse, UpdateResponse or DeleteResponse, 它们都可以惭怍是 DocWriteResponse 实例
    
    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
    || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
    IndexResponse indexResponse = (IndexResponse) itemResponse; // index 操作后的响应结果
    
    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
    UpdateResponse updateResponse = (UpdateResponse) itemResponse; // update 操作后的响应结果
    
    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
    DeleteResponse deleteResponse = (DeleteResponse) itemResponse; // delete 操作后的响应结果
    }
}
```

##### [Bulk Processor API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-bulk.html#java-rest-high-document-bulk-processor)
```java
// Bulk Processor是简化Bulk API的一个工具类
// 使用BulkProcessor.builder().build;构建BulkProcessor
// BulkProcessor.Listener是添加request的的异步返回处理
BulkProcessor.Listener listener = new BulkProcessor.Listener() {
@Override
public void beforeBulk(long executionId, BulkRequest request) {

        }

@Override
public void afterBulk(long executionId, BulkRequest request,
        BulkResponse response) {

        }

@Override
public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

        }
        };

// 一旦build()的了，就会一直在跑着这个操作，只需往里添加request即可，通过listener进行处理
BulkProcessor bulkProcessor =
BulkProcessor.builder(client::bulkAsync, listener).build();

// 多大数量操作一次
builder.setBulkActions(500);
// 知识一次执行的大小
builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
// 设置并发执行请求数，默认是1，设置为0表示只允许单个请求执行
builder.setConcurrentRequests(0);
// 设置请求刷新间隔
builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
// 设置backOff策略，一秒三次
builder.setBackoffPolicy(BackoffPolicy
.constantBackoff(TimeValue.timeValueSeconds(1L), 3));

// 增加Request
IndexRequest one = new IndexRequest("posts", "doc", "1").
source(XContentType.JSON, "title",
"In which order are my Elasticsearch queries executed?");
IndexRequest two = new IndexRequest("posts", "doc", "2")
.source(XContentType.JSON, "title",
"Current status and upcoming changes in Elasticsearch");
IndexRequest three = new IndexRequest("posts", "doc", "3")
.source(XContentType.JSON, "title",
"The Future of Federated Search in Elasticsearch");

bulkProcessor.add(one);
bulkProcessor.add(two);
bulkProcessor.add(three);

// 关闭
boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS); 
```

##### [Multi Get API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/java-rest-high-document-multi-get.html)

#### [Search API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.3/_search_apis.html)

#### [Building Queries](https://www.cnblogs.com/reycg-blog/p/9993094.html)