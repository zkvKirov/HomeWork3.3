package ru.netology

data class Chat(
    val id: Int = -1,
    val users: List<Int>,
    val title: String,
    val messages: List<Message>,
    val countMessage: Int = 0,
    val countOfUnreadMessages: Int = countMessage
)



data class Message(
    val id: Int = -1,
    val userID: Int,
    val text: String,
    val isRead: Boolean = false
)

