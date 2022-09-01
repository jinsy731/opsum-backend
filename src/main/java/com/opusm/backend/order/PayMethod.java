package com.opusm.backend.order;

public enum PayMethod {

    ASSET("asset"),
    POINT("point");

    private final String value;

    PayMethod(String value) {
        this.value = value;
    }
}
