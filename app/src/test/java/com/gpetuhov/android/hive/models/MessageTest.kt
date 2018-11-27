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
            timestamp = System.currentTimeMillis(),
            text = "Message text",
            isFromCurrentUser = true,
            isRead = false
        )
    }

    @Test
    fun notEmptyTime() = assertNotEquals("", message.getMessageTime())
}