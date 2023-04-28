package com.example.bubble;

import com.example.bubble.JSON.MessageListItem;


import java.util.Comparator;

public class MessageListComparator implements Comparator<MessageListItem> {
    @Override
   public int compare(MessageListItem o1, MessageListItem o2) {

        return (int) (o2.message.date-o1.message.date);
    }
}
