package com.xkglow.xkcommand.Helper;

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
        CHANGE_DEVICE,
        SET_CAMERA_PHOTO,
        TURN_ON_OFF
    }
}
