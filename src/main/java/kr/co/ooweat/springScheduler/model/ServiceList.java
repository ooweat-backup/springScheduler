package kr.co.ooweat.springScheduler.model;

import lombok.Getter;

public enum ServiceList {
    SERVICE_KAKAO_ALERT("172.29.100.149", 8081, "알림톡 전송");

    @Getter
    private String host;
    @Getter
    private int port;
    @Getter
    private String description;

    ServiceList(String host, int port, String description){
        this.host = host;
        this.port = port;
        this.description = description;
    }
}
