package com.gkalal.testproject;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class LoginResponse {

    @SerializedName("errocode")
    @Expose
    private String errorcode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("login_ID")
    @Expose
    private String loginId;

    @SerializedName("name")
    @Expose
    private String name;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
