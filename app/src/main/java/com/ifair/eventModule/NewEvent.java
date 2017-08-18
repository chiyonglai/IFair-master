package com.ifair.eventModule;

/**
 * 最新消息的事件傳遞
 */

public class NewEvent {
    private boolean isLike;

    public NewEvent(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean isLike() {
        return isLike;
    }
}
