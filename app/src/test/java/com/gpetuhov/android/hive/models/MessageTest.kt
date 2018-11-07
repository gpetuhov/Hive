package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Message
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MessageTest {

    companion object {
        const val DUMMY_SENDER_UID = "9j82qho"
    }

    private lateinit var message: Message

    @Before
    fun initUser() {
        message = Message(
            senderUid = DUMMY_SENDER_UID,
            timestamp = System.currentTimeMillis(),
            text = "Message text"
        )
    }

    @Test
    fun isFromUser() {
        assertEquals(true, message.isFromUser(DUMMY_SENDER_UID))
        assertEquals(false, message.isFromUser("563h89ju"))
    }

    @Test
    fun notEmptyTime() = assertNotEquals("", message.getMessageTime())
}