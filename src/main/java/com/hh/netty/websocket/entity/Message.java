package com.hh.netty.websocket.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Message {
    private Integer sendId;
    private Integer revId;
    private String message;
    private Date date;
}
