/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.node;

import java.io.Serializable;

import com.lamfire.json.JSON;

/**
 * @author zxc Jan 5, 2016 5:17:15 PM
 */
public class SendSmsFootprint implements Serializable {

    private static final long serialVersionUID = -6481135699878360798L;

    private String            id;
    private String            appKey;
    private String            zone;
    private String            phone;

    /** 请求发送验证码，还未交付发送中心的时段 */
    private long              initAt;
    private int               initState        = 123;
    /** 验证码 */
    private String            code             = "";
    /** 验证码的类型，文本或语音 */
    private int               codeType         = 123;
    /** 请求验证码来自client或者server */
    private int               reqType          = 23;
    private String            initDesc         = "";

    /** 请求发送验证码，请求在发送中心中的阶段 */
    private long              senderAt;
    private int               senderState      = 22;
    /** 发送通道 */
    private String            channles         = "";
    /** 短信通道内部的id */
    private String            smsId;
    private String            senderDesc       = "";

    /** 验证阶段 */
    private long              verifyAt;
    private int               verifyState      = 22;
    /** 待验证的验证码 */
    private String            vcode            = "";
    /** 验证请求来自client还是server */
    private int               source           = 33;
    private String            verifyDesc       = "";

    /** 从运营商那里获取短信回执的阶段 */
    private long              callbackAt;
    /** 通道内部实际的发送时间 */
    private long              sendAt;
    private int               callbackState    = 33;
    private String            callbackDesc     = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getInitAt() {
        return initAt;
    }

    public void setInitAt(long initAt) {
        this.initAt = initAt;
    }

    public int getInitState() {
        return initState;
    }

    public void setInitState(int initState) {
        this.initState = initState;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInitDesc() {
        return initDesc;
    }

    public void setInitDesc(String initDesc) {
        this.initDesc = initDesc;
    }

    public long getSenderAt() {
        return senderAt;
    }

    public void setSenderAt(long senderAt) {
        this.senderAt = senderAt;
    }

    public int getSenderState() {
        return senderState;
    }

    public void setSenderState(int senderState) {
        this.senderState = senderState;
    }

    public String getSenderDesc() {
        return senderDesc;
    }

    public void setSenderDesc(String senderDesc) {
        this.senderDesc = senderDesc;
    }

    public long getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(long verifyAt) {
        this.verifyAt = verifyAt;
    }

    public int getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(int verifyState) {
        this.verifyState = verifyState;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getVerifyDesc() {
        return verifyDesc;
    }

    public void setVerifyDesc(String verifyDesc) {
        this.verifyDesc = verifyDesc;
    }

    public long getCallbackAt() {
        return callbackAt;
    }

    public void setCallbackAt(long callbackAt) {
        this.callbackAt = callbackAt;
    }

    public int getCallbackState() {
        return callbackState;
    }

    public void setCallbackState(int callbackState) {
        this.callbackState = callbackState;
    }

    public String getCallbackDesc() {
        return callbackDesc;
    }

    public void setCallbackDesc(String callbackDesc) {
        this.callbackDesc = callbackDesc;
    }

    public long getSendAt() {
        return sendAt;
    }

    public void setSendAt(long sendAt) {
        this.sendAt = sendAt;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public String getChannles() {
        return channles;
    }

    public void setChannles(String channles) {
        this.channles = channles;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
