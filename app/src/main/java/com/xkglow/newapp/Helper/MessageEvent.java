package com.xkglow.newapp.Helper;

public class MessageEvent {
    public MessageEventType type;
    public Object data;

    public MessageEvent(MessageEventType type) {
        this.type = type;
    }

    public MessageEvent(MessageEventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public enum MessageEventType {
        SET_STATUS_BAR
    }
}
