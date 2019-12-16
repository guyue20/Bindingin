package com.bdyx.entity;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/*
 * @head
 * @body   json.text
 * @Explain 格式:{"head":{@head},"body":{@body}}
 */
public class HisResponse {
    private String head;
    private String body;

    public HisResponse(Object head, Object body) {
//		String hd=JSONObject.parseObject(JSON.toJSONString(head)).toString();
        String by = JSONObject.parseObject(JSON.toJSONString(body)).toString();
        this.head = head.toString();
        this.body = by;
        //this.body=YuYueUtil.encode(by);
        if (head != null) {
            //System.out.println("返回给app的解密head字符串："+YuYueUtil.decode(getHead()));
        }
        if (body != null) {
            //System.out.println("返回给app的解密body字符串："+YuYueUtil.decode(getBody()));
        }
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HisResponse [head=" + head + ", body=" + body + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((head == null) ? 0 : head.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HisResponse other = (HisResponse) obj;
        if (body == null) {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        if (head == null) {
            if (other.head != null)
                return false;
        } else if (!head.equals(other.head))
            return false;
        return true;
    }

}