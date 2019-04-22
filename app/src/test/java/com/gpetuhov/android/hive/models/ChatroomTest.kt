package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.utils.Constants
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChatroomTest {

    private lateinit var chatroom: Chatroom

    @Before
    fun initUser() {
        chatroom = Constants.DUMMY_CHATROOM
    }

    @Test
    fun lastMessageTimestampToString() {
        chatroom.lastMessageTimestamp = 1555937269
        Assert.assertEquals("4/22/19 3:47 PM", chatroom.getLastMessageTime())
    }

    @Test
    fun newMessageCountToString() {
        chatroom.newMessageCount = 123
        Assert.assertEquals("123", chatroom.getNewMessageCountString())
    }
}