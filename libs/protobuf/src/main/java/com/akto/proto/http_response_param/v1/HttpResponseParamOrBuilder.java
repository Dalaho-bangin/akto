// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: threat_detection/message/http_response_param/v1/http_response_param.proto
// Protobuf Java Version: 4.29.2

package com.akto.proto.http_response_param.v1;

public interface HttpResponseParamOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:threat_detection.message.http_response_param.v1.HttpResponseParam)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string method = 1 [json_name = "method"];</code>
   *
   * @return The method.
   */
  java.lang.String getMethod();

  /**
   * <code>string method = 1 [json_name = "method"];</code>
   *
   * @return The bytes for method.
   */
  com.google.protobuf.ByteString getMethodBytes();

  /**
   * <code>string path = 2 [json_name = "path"];</code>
   *
   * @return The path.
   */
  java.lang.String getPath();

  /**
   * <code>string path = 2 [json_name = "path"];</code>
   *
   * @return The bytes for path.
   */
  com.google.protobuf.ByteString getPathBytes();

  /**
   * <code>string type = 3 [json_name = "type"];</code>
   *
   * @return The type.
   */
  java.lang.String getType();

  /**
   * <code>string type = 3 [json_name = "type"];</code>
   *
   * @return The bytes for type.
   */
  com.google.protobuf.ByteString getTypeBytes();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; request_headers = 4 [json_name = "requestHeaders"];
   * </code>
   */
  int getRequestHeadersCount();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; request_headers = 4 [json_name = "requestHeaders"];
   * </code>
   */
  boolean containsRequestHeaders(java.lang.String key);

  /** Use {@link #getRequestHeadersMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.akto.proto.http_response_param.v1.StringList>
      getRequestHeaders();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; request_headers = 4 [json_name = "requestHeaders"];
   * </code>
   */
  java.util.Map<java.lang.String, com.akto.proto.http_response_param.v1.StringList>
      getRequestHeadersMap();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; request_headers = 4 [json_name = "requestHeaders"];
   * </code>
   */
  /* nullable */
  com.akto.proto.http_response_param.v1.StringList getRequestHeadersOrDefault(
      java.lang.String key,
      /* nullable */
      com.akto.proto.http_response_param.v1.StringList defaultValue);

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; request_headers = 4 [json_name = "requestHeaders"];
   * </code>
   */
  com.akto.proto.http_response_param.v1.StringList getRequestHeadersOrThrow(java.lang.String key);

  /**
   * <code>string request_payload = 5 [json_name = "requestPayload"];</code>
   *
   * @return The requestPayload.
   */
  java.lang.String getRequestPayload();

  /**
   * <code>string request_payload = 5 [json_name = "requestPayload"];</code>
   *
   * @return The bytes for requestPayload.
   */
  com.google.protobuf.ByteString getRequestPayloadBytes();

  /**
   * <code>int32 api_collection_id = 6 [json_name = "apiCollectionId"];</code>
   *
   * @return The apiCollectionId.
   */
  int getApiCollectionId();

  /**
   * <code>int32 status_code = 7 [json_name = "statusCode"];</code>
   *
   * @return The statusCode.
   */
  int getStatusCode();

  /**
   * <code>string status = 8 [json_name = "status"];</code>
   *
   * @return The status.
   */
  java.lang.String getStatus();

  /**
   * <code>string status = 8 [json_name = "status"];</code>
   *
   * @return The bytes for status.
   */
  com.google.protobuf.ByteString getStatusBytes();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; response_headers = 9 [json_name = "responseHeaders"];
   * </code>
   */
  int getResponseHeadersCount();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; response_headers = 9 [json_name = "responseHeaders"];
   * </code>
   */
  boolean containsResponseHeaders(java.lang.String key);

  /** Use {@link #getResponseHeadersMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.akto.proto.http_response_param.v1.StringList>
      getResponseHeaders();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; response_headers = 9 [json_name = "responseHeaders"];
   * </code>
   */
  java.util.Map<java.lang.String, com.akto.proto.http_response_param.v1.StringList>
      getResponseHeadersMap();

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; response_headers = 9 [json_name = "responseHeaders"];
   * </code>
   */
  /* nullable */
  com.akto.proto.http_response_param.v1.StringList getResponseHeadersOrDefault(
      java.lang.String key,
      /* nullable */
      com.akto.proto.http_response_param.v1.StringList defaultValue);

  /**
   * <code>
   * map&lt;string, .threat_detection.message.http_response_param.v1.StringList&gt; response_headers = 9 [json_name = "responseHeaders"];
   * </code>
   */
  com.akto.proto.http_response_param.v1.StringList getResponseHeadersOrThrow(java.lang.String key);

  /**
   * <code>string response_payload = 10 [json_name = "responsePayload"];</code>
   *
   * @return The responsePayload.
   */
  java.lang.String getResponsePayload();

  /**
   * <code>string response_payload = 10 [json_name = "responsePayload"];</code>
   *
   * @return The bytes for responsePayload.
   */
  com.google.protobuf.ByteString getResponsePayloadBytes();

  /**
   * <code>int32 time = 11 [json_name = "time"];</code>
   *
   * @return The time.
   */
  int getTime();

  /**
   * <code>string akto_account_id = 12 [json_name = "aktoAccountId"];</code>
   *
   * @return The aktoAccountId.
   */
  java.lang.String getAktoAccountId();

  /**
   * <code>string akto_account_id = 12 [json_name = "aktoAccountId"];</code>
   *
   * @return The bytes for aktoAccountId.
   */
  com.google.protobuf.ByteString getAktoAccountIdBytes();

  /**
   * <code>string ip = 13 [json_name = "ip"];</code>
   *
   * @return The ip.
   */
  java.lang.String getIp();

  /**
   * <code>string ip = 13 [json_name = "ip"];</code>
   *
   * @return The bytes for ip.
   */
  com.google.protobuf.ByteString getIpBytes();

  /**
   * <code>string dest_ip = 14 [json_name = "destIp"];</code>
   *
   * @return The destIp.
   */
  java.lang.String getDestIp();

  /**
   * <code>string dest_ip = 14 [json_name = "destIp"];</code>
   *
   * @return The bytes for destIp.
   */
  com.google.protobuf.ByteString getDestIpBytes();

  /**
   * <code>string direction = 15 [json_name = "direction"];</code>
   *
   * @return The direction.
   */
  java.lang.String getDirection();

  /**
   * <code>string direction = 15 [json_name = "direction"];</code>
   *
   * @return The bytes for direction.
   */
  com.google.protobuf.ByteString getDirectionBytes();

  /**
   * <code>bool is_pending = 16 [json_name = "isPending"];</code>
   *
   * @return The isPending.
   */
  boolean getIsPending();

  /**
   * <code>string source = 17 [json_name = "source"];</code>
   *
   * @return The source.
   */
  java.lang.String getSource();

  /**
   * <code>string source = 17 [json_name = "source"];</code>
   *
   * @return The bytes for source.
   */
  com.google.protobuf.ByteString getSourceBytes();

  /**
   * <code>string akto_vxlan_id = 18 [json_name = "aktoVxlanId"];</code>
   *
   * @return The aktoVxlanId.
   */
  java.lang.String getAktoVxlanId();

  /**
   * <code>string akto_vxlan_id = 18 [json_name = "aktoVxlanId"];</code>
   *
   * @return The bytes for aktoVxlanId.
   */
  com.google.protobuf.ByteString getAktoVxlanIdBytes();
}
