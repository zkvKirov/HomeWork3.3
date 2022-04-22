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
    fun clearPosts() {
        ChatService.removeAll()
    }

    @Test
    fun createMessage() {
        ChatService.createMessage(1, 2, "Приветствую!!!")
        ChatService.createMessage(3, 4, "Как дела?")
        ChatService.createMessage(2,1, "Hi")
    }

    @Test
    fun deleteMessage() {
        ChatService.createMessage(1,2, "Привет!")
        ChatService.createMessage(3, 4, "Как дела?")
        ChatService.createMessage(2,1, "Hi")
        ChatService.deleteMessage(1, 1)
    }

    @Test
    fun deleteChat() {
        ChatService.createMessage(1,2, "Привет!")
        ChatService.createMessage(2,1, "Hi")
        ChatService.createMessage(3, 4, "Как дела?")
        ChatService.createMessage(4, 3, "Как сажа бела")
        ChatService.deleteChat(1)
    }

    @Test
    fun editMessage() {
        ChatService.createMessage(1,2, "Привет!")
        ChatService.createMessage(2,1, "Hi")
        ChatService.createMessage(1, 2, "Как дела?")
        val message = Message(id = 1, userID = 2, text = "Приветствую!!!")
        val result = ChatService.editMessage(message, 0)
        assertTrue(result)
    }

    @Test
    fun getAllMessageInChat() {
        ChatService.createMessage(1,2, "Привет!")
        ChatService.createMessage(2,1, "Hi")
        ChatService.createMessage(1, 2, "Как дела?")
        ChatService.createMessage(2, 1, "Как сажа бела")
        val expectedList = listOf("Привет!", "Hi", "Как дела?", "Как сажа бела")
        val actualList = ChatService.getAllMessageInChat(0)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun getUnreadMessageInChat() {
        ChatService.createMessage(1,2, "Привет!")
        ChatService.createMessage(2,1, "Hi")
        ChatService.createMessage(1, 2, "Как дела?")
        ChatService.createMessage(2, 1, "Как сажа бела")

        val expectedList = listOf("Привет!", "Как дела?")
        val actualList = ChatService.getAllMessageInChat(0)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun readMessage() {
        ChatService.createMessage(1,2, "Привет!")
        ChatService.createMessage(2,1, "Hi")
        val result = ChatService.readMessage(0, 0)
        assertTrue(result)
    }
}