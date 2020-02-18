package top.dustone.cilicili.bean;

import java.util.Map;

public class RuntimeParams {
    private Map<String, String> baseRequestHeaders;
    private String uid;
    private String dynamicType;

    public String getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(String dynamicType) {
        this.dynamicType = dynamicType;
    }

    public Map<String, String> getBaseRequestHeaders() {
        return baseRequestHeaders;
    }

    public void setBaseRequestHeaders(Map<String, String> baseRequestHeaders) {
        this.baseRequestHeaders = baseRequestHeaders;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
