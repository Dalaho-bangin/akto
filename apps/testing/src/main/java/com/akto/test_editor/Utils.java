package com.akto.test_editor;

import java.util.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.jce.provider.JDKDSASigner.stdDSA;

import com.akto.dto.OriginalHttpRequest;
import com.akto.dto.RawApi;
import com.akto.dao.context.Context;
import com.akto.dto.ApiInfo.ApiAccessType;
import com.akto.dto.test_editor.ExecutorSingleOperationResp;
import com.akto.dto.testing.UrlModifierPayload;
import com.akto.util.Constants;
import com.akto.util.JSONUtils;
import com.akto.util.http_util.CoreHTTPClient;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import static com.akto.rules.TestPlugin.extractAllValuesFromPayload;
import okhttp3.*;

public class Utils {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonFactory factory = mapper.getFactory();
    private static final Gson gson = new Gson();

    private static final OkHttpClient client = CoreHTTPClient.client.newBuilder()
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .callTimeout(5, TimeUnit.SECONDS)
        .build();

    public static Boolean checkIfContainsMatch(String text, String keyword) {
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(text);
        String match = null;
        if (matcher.find()) {
            match = matcher.group(0);
        }

        return match != null;
    }

    public static boolean deleteKeyFromPayload(Object obj, String parentKey, String queryKey) {
        boolean res = false;
        if (obj instanceof BasicDBObject) {
            BasicDBObject basicDBObject = (BasicDBObject) obj;

            Set<String> keySet = basicDBObject.keySet();

            for(String key: keySet) {
                if (key == null) {
                    continue;
                }
                Object value = basicDBObject.get(key);
                if (!( (value instanceof BasicDBObject) || (value instanceof BasicDBList) )) {
                    if (key.equalsIgnoreCase(queryKey)) {
                        basicDBObject.remove(key);
                        return true;
                    }
                }
                res = deleteKeyFromPayload(value, key, queryKey);
                if (res) {
                    break;
                }
            }
        } else if (obj instanceof BasicDBList) {
            for(Object elem: (BasicDBList) obj) {
                res = deleteKeyFromPayload(elem, parentKey, queryKey);
                if (res) {
                    break;
                }
            }
        }

        return res;
    }

    public static boolean modifyValueInPayload(Object obj, String parentKey, String queryKey, Object queryVal) {
        boolean res = false;
        if (obj instanceof BasicDBObject) {
            BasicDBObject basicDBObject = (BasicDBObject) obj;

            Set<String> keySet = basicDBObject.keySet();

            for(String key: keySet) {
                if (key == null) {
                    continue;
                }
                Object value = basicDBObject.get(key);

                if (!( (value instanceof BasicDBObject) || (value instanceof BasicDBList) )) {
                    if (key.equalsIgnoreCase(queryKey)) {
                        basicDBObject.remove(key);
                        basicDBObject.put(queryKey, queryVal);
                        return true;
                    }
                }

                if (value instanceof BasicDBList) {
                    BasicDBList valList = (BasicDBList) value;
                    if (valList.size() == 0 && key.equalsIgnoreCase(queryKey)) {
                        List<Object> queryList = Collections.singletonList(queryVal);
                        basicDBObject.remove(key);
                        basicDBObject.put(queryKey, queryList);
                        return true;
                    } else if (valList.size() > 0 && !( (valList.get(0) instanceof BasicDBObject) || (valList.get(0) instanceof BasicDBList) ) && key.equalsIgnoreCase(queryKey)) {
                        List<Object> queryList = Collections.singletonList(queryVal);
                        basicDBObject.remove(key);
                        basicDBObject.put(queryKey, queryList);
                        return true;
                    }
                }

                res = modifyValueInPayload(value, key, queryKey, queryVal);
                if (res) {
                    break;
                }
            }
        } else if (obj instanceof BasicDBList) {
            for(Object elem: (BasicDBList) obj) {
                res = modifyValueInPayload(elem, parentKey, queryKey, queryVal);
                if (res) {
                    break;
                }
            }
        }

        return res;
    }

    public static String applyRegexModifier(String data, String regex, String replaceWith) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            data = data.replaceAll(regex, replaceWith);
        }
        return data;
    }

    public static Boolean applyIneqalityOperation(Object data, Object querySet, String operator) {
        Boolean result = false;
        try {
            if (data instanceof Integer) {
                List<Integer> queryList = (List) querySet;
                if (queryList == null || queryList.size() == 0) {
                    return false;
                }
                Integer dataInt = (Integer) data;
                Object query = queryList.get(0);

                if (query instanceof String) {
                    try {
                        int queryInt = Integer.parseInt((String) query);
                        result = compareIntegers(operator, dataInt, queryInt);
                    } catch (Exception e) {
                        Double queryDouble = Double.parseDouble(query.toString());
                        result = compareDoubles(operator, dataInt.doubleValue(), queryDouble);
                    }
                } else if (query instanceof Double) {
                    Double queryDouble = Double.parseDouble(query.toString());
                    result = compareDoubles(operator, dataInt.doubleValue(), queryDouble);
                } else {
                    result = compareIntegers(operator, (int) dataInt, (int) queryList.get(0));
                }
            } else if (data instanceof Double) {
                List<Integer> queryList = (List) querySet;
                if (queryList == null || queryList.size() == 0) {
                    return false;
                }
                Double dataDouble = (Double) data;
                Object query = queryList.get(0);

                if (query instanceof String) {
                    try {
                        int queryInt = Integer.parseInt((String) query);
                        result = compareDoubles(operator, dataDouble, Double.valueOf(queryInt));
                    } catch (Exception e) {
                        Double queryDouble = Double.parseDouble(query.toString());
                        result = compareDoubles(operator, dataDouble, queryDouble);
                    }
                } else if (query instanceof Double) {
                    Double queryDouble = Double.parseDouble(query.toString());
                    result = compareDoubles(operator, dataDouble, queryDouble);
                } else {
                    result = compareDoubles(operator, dataDouble, (Double.valueOf(queryList.get(0))));
                }
            }
            
        } catch (Exception e) {
            return false;
        }

        return result;
    }

    public static Boolean compareDoubles(String operator, double a, double b) {
        Boolean result = false;
        switch (operator) {
            case "gte":
                result = a >= b;
                break;
            case "gt":
                result = a > b;
                break;
            case "lte":
                result = a <= b;
                break;
            case "lt":
                result = a < b;
                break;
            default:
                return false;
        }
        return result;
    }

    public static Boolean compareIntegers(String operator, int a, int b) {
        Boolean result = false;
        switch (operator) {
            case "gte":
                result = (int) a >= b;
                break;
            case "gt":
                result = (int) a > b;
                break;
            case "lte":
                result = (int) a <= b;
                break;
            case "lt":
                result = (int) a < b;
                break;
            default:
                return false;
        }
        return result;
    }


    public static Set<String> headerValuesUnchanged(Map<String, List<String>> originalRequestHeaders, Map<String, List<String>> testRequestHeaders) {
        Set<String> diff = new HashSet<>();
        if (originalRequestHeaders == null) return diff;
        for (String key: testRequestHeaders.keySet()) {
            List<String> originalHeaderValues = originalRequestHeaders.get(key);
            List<String> testHeaderValues = testRequestHeaders.get(key);
            if (originalHeaderValues == null || testHeaderValues == null) continue;
            if (areListsEqual(originalHeaderValues, testHeaderValues)) {
                diff.add(key);
            }
        }

        return diff;
    }

    public static boolean areListsEqual(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        List<String> copyOfList1 = new ArrayList<>(list1);
        List<String> copyOfList2 = new ArrayList<>(list2);

        Collections.sort(copyOfList1);
        Collections.sort(copyOfList2);

        return copyOfList1.equals(copyOfList2);
    }

    public static Set<String> bodyValuesUnchanged(String originalPayload, String testPayload) {
        Set<String> diff = new HashSet<>();

        Map<String, Set<String>> originalRequestParamMap = new HashMap<>();
        Map<String, Set<String>> testRequestParamMap= new HashMap<>();
        try {
            extractAllValuesFromPayload(originalPayload, originalRequestParamMap);
            extractAllValuesFromPayload(testPayload, testRequestParamMap);
        } catch (Exception e) {
        }

        for (String key: testRequestParamMap.keySet()) {
            Set<String> testValues = testRequestParamMap.get(key);
            Set<String> originalValues = originalRequestParamMap.get(key);
            if (testValues == null) continue;
            String[] keySplit = key.split("\\.");
            String finalKey = keySplit[keySplit.length - 1];
            if (testValues.equals(originalValues)) diff.add(finalKey); // todo: check null
        }

        return diff;
    }

    public static BasicDBObject fetchJsonObjForString(Object val) {
        if (!(val instanceof String)) {
            return null;
        }
        try {
            BasicDBObject obj = BasicDBObject.parse(val.toString());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /*
        key = users
        payload = {data : {users : { __typename: "abc", email: "abc@abc.com", info : {id: "werasdf", token: "asdfa"} }}}

        returns  __typename, email

    */

    public static List<String> findAllTerminalKeys(String payload, String key) {

        JsonParser jp;
        JsonNode node;
        List<String> values = new ArrayList<>();
        try {
            jp = factory.createParser(payload);
            node = mapper.readTree(jp);
        } catch (IOException e) {
            return values;
        }

        findAllKeys(node, key, values, false);
        return values;
    }

    public static void findAllKeys(JsonNode node, String key, List<String> values, boolean found) {
        if (found) {
            if (node.isArray()) {
                ArrayNode arrayNode = (ArrayNode) node;
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode arrayElement = arrayNode.get(i);
                    findAllKeys(arrayElement, key, values, found);
                }
            } else {
                Iterator<String> fieldNames = node.fieldNames();
                while(fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    JsonNode jsonNode = node.get(fieldName);
                    if (jsonNode.isValueNode()) {
                        values.add(fieldName);
                    }
                }
            }
        }
        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                findAllKeys(arrayElement, key, values, found);
            }
        } else {
            Iterator<String> fieldNames = node.fieldNames();
            while(fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (key.equalsIgnoreCase(fieldName)) {
                    found = true;
                }
                JsonNode jsonNode = node.get(fieldName);
                findAllKeys(jsonNode, key, values, found);
                found = false;
            }
        }
    }

    public static List<String> findAllValuesForKey(String payload, String key, boolean isRegex) {
        JsonParser jp = null;
        JsonNode node;
        List<String> values = new ArrayList<>();
        try {
            jp = factory.createParser(payload);
            node = mapper.readTree(jp);
        } catch (IOException e) {
            return values;
        }
        if (node == null) {
            return values;
        }

        findAllValues(node, key, values, isRegex);
        return values;
    }

    public static void findAllValues(JsonNode node, String matchFieldName, List<String> values, boolean isRegex) {

        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                findAllValues(arrayElement, matchFieldName, values, isRegex);
            }
        } else {
            Iterator<String> fieldNames = node.fieldNames();
            while(fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (Utils.checkIfMatches(fieldName, matchFieldName, isRegex)) {
                    String val;
                    try {
                        TextNode n = (TextNode) node.get(fieldName);
                        val = n.asText();
                    } catch (Exception e) {
                        val = node.get(fieldName).toString();
                    }
                    values.add(val);
                }
                JsonNode fieldValue = node.get(fieldName);
                findAllValues(fieldValue, matchFieldName, values, isRegex);
            }
        }

    }

    public static boolean checkIfMatches(String data, String query, boolean isRegex) {
        if (!isRegex) {
            return data.equalsIgnoreCase(query);
        }

        return Utils.checkIfContainsMatch(data, query);
    }

    public static double structureMatch(RawApi orig, RawApi cur) {
        String payload = orig.getResponse().getBody().replaceAll("\\s+","");
        String compareWithPayload = cur.getResponse().getBody().replaceAll("\\s+","");
        return Utils.calcStructureMatchPercentage(payload, compareWithPayload);
    }

    public static double calcStructureMatchPercentage(String payload, String compareWithPayload) {

        boolean isOrigPAyloadJson = isJsonPayload(payload);
        boolean isCurPAyloadJson = isJsonPayload(compareWithPayload);
        if (!isOrigPAyloadJson && !isCurPAyloadJson) {
            return 100;
        }

        boolean areBothJson = isOrigPAyloadJson && isCurPAyloadJson;
        if (!areBothJson) {
            return 0;
        }

        BasicDBObject payloadObj = extractPayloadObj(payload);
        BasicDBObject comparePayloadObj = extractPayloadObj(compareWithPayload);

        payloadObj = JSONUtils.flattenWithDots(payloadObj);
        comparePayloadObj = JSONUtils.flattenWithDots(comparePayloadObj);

        if (payloadObj.size() == 0 && comparePayloadObj.size() == 0) {
            return 100;
        }

        if (payloadObj.size() == 0 || comparePayloadObj.size() == 0) {
            return 0;
        }

        Set<String> payloadObjSet = new HashSet<>();
        Set<String> comparePayloadObjSet = new HashSet<>();
        Set<String> intersectionSet = new HashSet<>();
        Set<String> unionSet = new HashSet<>();
        for (String k: payloadObj.keySet()) {
            payloadObjSet.add(k);
            intersectionSet.add(k);
            unionSet.add(k);
        }
        for (String k: comparePayloadObj.keySet()) {
            comparePayloadObjSet.add(k);
        }

        intersectionSet.retainAll(comparePayloadObjSet);
        unionSet.addAll(comparePayloadObjSet);

        return ((double) intersectionSet.size()/unionSet.size()) * 100;
    }

    public static BasicDBObject extractPayloadObj(String payload) {
        if (payload == null || payload.isEmpty()) {
            payload = "{}";
        }

        if(payload.startsWith("[")) {
            payload = "{\"json\": "+payload+"}";
        }

        BasicDBObject obj;
        try {
            obj = BasicDBObject.parse(payload);
        } catch (Exception e) {
            obj = BasicDBObject.parse("{}");
        }

        return obj;
    }

    public static boolean isJsonPayload(String payload) {
        try {
            Map<String, Object> m1 = (Map<String, Object>)(mapper.readValue(payload, Map.class));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static UrlModifierPayload fetchUrlModifyPayload(String payload) {
        UrlModifierPayload urlModifierPayload = null;
        try {
            payload = payload.replaceAll("=", ":");

            if (payload.contains("regex:")) {
                payload = payload.substring(0, 22) + "\"" + payload.substring(22, payload.lastIndexOf(",")) + "\"" + payload.substring(payload.lastIndexOf(","), payload.length());
            }

            String x[] = payload.split("replace_with:");
            if (x.length < 2) {
                return urlModifierPayload;
            }
            String y[] = x[1].split("}}");
            x[1] = y[0].toString() + "\"}}";
            payload = String.join("replace_with:\"", x);
            payload = payload.replace("\\", "\\\\");
            Map<String, Object> json = gson.fromJson(payload, Map.class);
            String operation = "regex_replace";
            Map<String, Object> operationMap = new HashMap<>();
            if (json.containsKey("regex_replace")) {
                operationMap = (Map) json.get("regex_replace");
            } else if (json.containsKey("token_insert")) {
                operationMap = (Map) json.get("token_insert");
                operation = "token_insert";
            } else if (json.containsKey("token_replace")) {
                operationMap = (Map) json.get("token_replace");
                operation = "token_replace";
            }
            String locStr = operationMap.getOrDefault("location", "0").toString();
            Double loc = Double.parseDouble(locStr);
            Integer location = loc.intValue();

            String replaceWith = operationMap.getOrDefault("replace_with", "").toString();
            try {
                Double replaceWithDouble = Double.parseDouble(replaceWith);
                Integer replaceWithInt = replaceWithDouble.intValue();
                replaceWith = replaceWithInt.toString();
            } catch (Exception e) {
                // TODO: handle exception
            }

            urlModifierPayload = new UrlModifierPayload(operationMap.getOrDefault("regex", "").toString(), 
                location, replaceWith, operation);
        } catch (Exception e) {
            return urlModifierPayload;
        }
        return urlModifierPayload;
    }

    public static String jsonifyIfArray(String payload) {
        if (payload != null && payload.startsWith("[")) {
            payload = "{\"json\": "+payload+"}";
        }
        return payload;
    }

    public static String buildNewUrl(UrlModifierPayload urlModifierPayload, String oldUrl) {
        String url = "";
        if (urlModifierPayload.getOperationType().equalsIgnoreCase("regex_replace") || urlModifierPayload.getOperationType().equalsIgnoreCase("token_replace")) {
            if (urlModifierPayload.getRegex() != null && !urlModifierPayload.getRegex().equals("")) {
                url = Utils.applyRegexModifier(oldUrl, urlModifierPayload.getRegex(), urlModifierPayload.getReplaceWith());
            } else {
                URI uri = fetchUri(oldUrl);
                oldUrl = fetchUrlPath(uri, oldUrl);

                String[] urlTokens = oldUrl.split("/");
                Integer position = urlModifierPayload.getPosition();
                if (position <= 0) {
                    // position is not valid
                    return fetchActualUrl(uri, oldUrl);
                }
                return replaceUrlWithToken(urlTokens, urlModifierPayload, position, uri);
            }
        } else {
            URI uri = fetchUri(oldUrl);
            oldUrl = fetchUrlPath(uri, oldUrl);

            String[] urlTokens = oldUrl.split("/");
            Integer position = urlModifierPayload.getPosition();
            if (position <= 0) {
                // position is not valid
                return fetchActualUrl(uri, oldUrl);
            }

            return insertUrlWithToken(urlTokens, urlModifierPayload, position, uri);
            
        }
        return url;
    }

    private static URI fetchUri(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return uri;
    }

    private static String fetchUrlPath(URI uri, String url) {
        if (uri != null) {
            return uri.getPath();
        }
        return url;
    }

    private static String fetchActualUrl(URI uri, String url) {
        if (uri != null && uri.getHost() != null) {
            return uri.getScheme() + "://" + uri.getHost() + url;
        } else {
            return url;
        }
    }

    private static String replaceUrlWithToken(String[] urlTokens, UrlModifierPayload urlModifierPayload, int position, URI uri) {
       
        String[] urlTokensCopy;

        if (position >= urlTokens.length) {
            urlTokensCopy = new String[position+1];
            for (int i=0; i < urlTokens.length; i++) {
                urlTokensCopy[i] = urlTokens[i];
            }
            for (int i=urlTokens.length; i <= position; i++) {
                urlTokensCopy[i] = "/";
            }
            urlTokensCopy[position] = urlModifierPayload.getReplaceWith();
            String url = "/";
            for (int i=1; i < urlTokensCopy.length; i++) {
                if (urlTokensCopy[i].equals("/") || i == urlTokensCopy.length - 1) {
                    url = url + urlTokensCopy[i];
                } else {
                    url = url + urlTokensCopy[i] + "/";
                }
            }
            return fetchActualUrl(uri, url);
        }
        urlTokens[position] = urlModifierPayload.getReplaceWith();
        String url = String.join( "/", urlTokens);
        return fetchActualUrl(uri, url);
    }

    private static String insertUrlWithToken(String[] urlTokens, UrlModifierPayload urlModifierPayload, int position, URI uri) {
        
        String[] urlTokensCopy;

        if (position > urlTokens.length) {
            urlTokensCopy = new String[position];
            for (int i=0; i < urlTokens.length; i++) {
                urlTokensCopy[i] = urlTokens[i];
            }
            for (int i=urlTokens.length; i < position; i++) {
                urlTokensCopy[i] = "/";
            }
            
            String[] newUrlTokens = new String[urlTokensCopy.length];
            for (int i = 1; i < position; i++) {
                newUrlTokens[i-1] = urlTokensCopy[i];
            }
            newUrlTokens[position - 1] = urlModifierPayload.getReplaceWith();
            for (int i = position; i < urlTokensCopy.length - 1; i++) {
                newUrlTokens[i] = urlTokensCopy[i];
            }
            String url = "/";
            for (int i=0; i < newUrlTokens.length; i++) {
                if (newUrlTokens[i].equals("/") || i == newUrlTokens.length - 1) {
                    url = url + newUrlTokens[i];
                } else {
                    url = url + newUrlTokens[i] + "/";
                }
            }
            return fetchActualUrl(uri, url);

        }

        String[] newUrlTokens = new String[urlTokens.length];
        for (int i = 1; i < position; i++) {
            newUrlTokens[i-1] = urlTokens[i];
        }
        newUrlTokens[position - 1] = urlModifierPayload.getReplaceWith();
        for (int i = position; i < urlTokens.length; i++) {
            newUrlTokens[i] = urlTokens[i];
        }
        String url = String.join( "/", newUrlTokens);
        url = "/" + url;
        return fetchActualUrl(uri, url);
    }
    
    public static boolean evaluateResult(String operation, boolean currentRes, boolean newVal) {

        if (operation == "and") {
            return currentRes && newVal;
        }
        return currentRes || newVal;
    }

    public static String convertToHarPayload(String message, int akto_account_id, int time, String type, String source) throws Exception {

        Map<String, Object> json = gson.fromJson(message, Map.class);

        Map<String, Object> req = (Map) json.get("request");
        Map<String, Object> resp = (Map) json.get("response");

        Map<String, Object> reqHeaders = new HashMap<>();
        try {
            reqHeaders = mapper.readValue((String) req.get("headers"), HashMap.class);
            reqHeaders.remove("x-akto-ignore");
        } catch (Exception e) {
            // TODO: handle exception
        }

        String requestHeaders = mapper.writeValueAsString(reqHeaders);
        String responseHeaders = (String) resp.get("headers");

        String path = OriginalHttpRequest.getFullUrlWithParams((String) req.get("url"), (String) req.get("queryParams"));
        String contentType = (String) reqHeaders.get("content-type");

        Map<String,String> result = new HashMap<>();
        result.put("akto_account_id", akto_account_id+"");
        result.put("path", path);
        result.put("requestHeaders", requestHeaders);
        result.put("responseHeaders", responseHeaders);
        result.put("method", (String) req.get("method"));
        result.put("requestPayload", (String) req.get("body"));
        result.put("responsePayload", (String) resp.get("method"));
        result.put("ip", "");
        result.put("time",time+"");
        result.put("statusCode", ((Double) resp.get("statusCode")).intValue()+"");
        result.put("type", type);
        result.put("status", null);
        result.put("contentType", contentType);
        result.put("source", source);

        return mapper.writeValueAsString(result);
    }

    public static String extractValue(String keyValue, String key) {
        String result = "";
        if (keyValue.contains(key)) {
            result = keyValue.split(key)[1].split("[,}]")[0];
            result = result.replaceAll("\\}$", "");
            result = result.trim();
        }
        return result;
    }

    public static ExecutorSingleOperationResp sendRequestToSsrfServer(String requestUrl, String redirectUrl, String tokenVal){
        RequestBody emptyBody = RequestBody.create(new byte[]{}, null);
        
        Request request = new Request.Builder()
            .url(requestUrl)
            .addHeader("x-akto-redirect-url", redirectUrl)
            .addHeader(Constants.AKTO_TOKEN_KEY, tokenVal)
            .post(emptyBody)
            .build();
        Response okResponse = null;
    
        try {
            okResponse = client.newCall(request).execute();
            if (!okResponse.isSuccessful()) {
                return new ExecutorSingleOperationResp(false,"Could not send request to the ssrf server.");
            }
            return new ExecutorSingleOperationResp(true, "");
        }catch (Exception e){
            return new ExecutorSingleOperationResp(false, e.getMessage());
        }finally {
            if (okResponse != null) {
                okResponse.close(); // Manually close the response body
            }
        }
    }

    public static Boolean sendRequestToSsrfServer(String url){
        String requestUrl = "";
        if(!(url.startsWith("http"))){
            String hostName ="https://test-services.akto.io/";
            if(System.getenv("SSRF_SERVICE_NAME") != null && System.getenv("SSRF_SERVICE_NAME").length() > 0){
                hostName = System.getenv("SSRF_SERVICE_NAME");
            }
            requestUrl = hostName + "validate/" + url;
        }

        Request request = new Request.Builder()
            .url(requestUrl)
            .get()
            .build();
            Response okResponse = null;
        
        try {
            okResponse = client.newCall(request).execute();
            if (!okResponse.isSuccessful()) {
                return false;
            }else{
                ResponseBody responseBody = okResponse.body();
                BasicDBObject bd = BasicDBObject.parse(responseBody.string());
                return bd.getBoolean("url-hit");
            }
        }catch (Exception e){
            return false;
        } finally {
            if (okResponse != null) {
                okResponse.close(); // Manually close the response body
            }
        }
    }

    public static ApiAccessType getApiAccessTypeFromString(String apiAccessType){
        switch (apiAccessType.toLowerCase()) {
            case "private":
                return ApiAccessType.PRIVATE;
            case "public":
                return ApiAccessType.PUBLIC;
            case "partner":
                return ApiAccessType.PARTNER;
            default:
                return null;
        }
    }

    public static Object getEpochTime(Object value) {
        Object val = null;
        if (value.equals("${current_epoch_seconds}")) {
            val = Context.now();
        }
        if (value.equals("${current_epoch_millis}")) {
            val = Context.epochInMillis();
        }
        return val;
    }

}
