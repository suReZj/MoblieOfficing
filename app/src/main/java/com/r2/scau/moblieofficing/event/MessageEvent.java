package com.r2.scau.moblieofficing.event;

import com.r2.scau.moblieofficing.bean.ChatMessage;

/**
 * Created by dell88 on 2017/7/27 0027.
 */

public class MessageEvent {
    private ChatMessage chatMessage;

    public MessageEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
