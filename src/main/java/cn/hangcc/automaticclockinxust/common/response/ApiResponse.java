/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.common.response;

import com.alibaba.fastjson.JSON;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String errMsg;
    private T data;

    public ApiResponse() {
    }

    public static <T> ApiResponse<T> buildSuccess(T date) {
        ApiResponse<T> response = new ApiResponse();
        response.setData(date);
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> buildSuccess() {
        ApiResponse<T> response = new ApiResponse();
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> buildFailure() {
        ApiResponse<T> response = new ApiResponse();
        response.setSuccess(false);
        return response;
    }

    public static <T> ApiResponse<T> buildFailure(String errorMsg) {
        ApiResponse<T> response = new ApiResponse();
        response.setSuccess(false);
        response.setCode(0);
        response.setErrMsg(errorMsg);
        return response;
    }

    public static <T> ApiResponse<T> buildFailure(int errorCode, String errorMsg) {
        ApiResponse<T> response = new ApiResponse();
        response.setSuccess(false);
        response.setCode(errorCode);
        response.setErrMsg(errorMsg);
        return response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return "ApiResponse{success=" + this.success + ", code=" + this.code + ", errMsg='" + this.errMsg + '\'' + ", data=" + this.data + '}';
    }
}
