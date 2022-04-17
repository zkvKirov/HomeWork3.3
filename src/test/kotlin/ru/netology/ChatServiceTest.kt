package ru.netology

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class ChatServiceTest {

    @Before
    fun prepare() {

    }

    @After
    fun clearPosts () {
        ChatService.removeAll()
    }

    @Test
    fun addToMessageOriginalID() {
        val message = Message(userID = 1, text = "Привет!")
        val expected = Message(id = 0, userID = 1, text = "Привет!")
        val actual = ChatService.addToMessageOriginalID(message)
        assertEquals(expected, actual)
    }

    @Test
    fun addToChatOriginalID() {
        val message = Message(userID = 1, text = "Привет!")
        val chat = Chat(users = listOf(1, 2), title = "Чат с пользователем 2", messages = listOf(message), countMessage = 1)
        val expectedID = 0
        val actualID = ChatService.addToChatOriginalID(chat)
        assertEquals(expectedID, actualID)
    }

    @Test
    fun createMessage() {
        ChatService.createMessage(1, 2, "Приветствую!!!")
    }

    @Test
    fun editMessage () {
        val messages = mutableListOf<Message>(
            Message(id = 0, userID = 1, text = "Привет!"),
            Message(id = 1, userID = 2, text = "Hi"),
            Message(id = 2, userID = 1, text = "Как дела?")
        )
        val message = Message(id = 1, userID = 2, text = "Приветствую!!!")
        val result = ChatService.editMessage(message)
        assertTrue(result)
    }
}