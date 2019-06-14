package com.doan.cookpad.Module.ConversationManager;

import com.doan.cookpad.Model.Conversation;

public interface GetMessageEndList {
    void onComplate(Conversation conversation);
    void onError(String error);
}
