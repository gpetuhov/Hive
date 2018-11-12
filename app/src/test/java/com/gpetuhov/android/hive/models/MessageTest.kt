package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Message
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MessageTest {

    private lateinit var message: Message

    @Before
    fun initUser() {
        message = Message(
            uid = "834ghih",
            senderUid = "9j82qho",
            timestamp = System.currentTimeMillis(),
            text = "Message text",
            isFromCurrentUser = true
        )
    }

    @Test
    fun notEmptyTime() = assertNotEquals("", message.getMessageTime())
}