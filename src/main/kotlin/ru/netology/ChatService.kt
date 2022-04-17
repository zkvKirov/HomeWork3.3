package ru.netology

object ChatService {

    private val chats = mutableListOf<Chat>()
    private val messages = mutableListOf<Message>()
    private var originalMessageID = -1
    private var originalChatID = -1

    fun addToMessageOriginalID (message: Message): Message {
        originalMessageID++
        val newMessage = message.copy(id = originalMessageID)
        messages.add(newMessage)
        return newMessage
    }

    fun addToChatOriginalID (chat: Chat): Int {
        originalChatID++
        val newChat = chat.copy(id = originalChatID)
        chats.add(newChat)
        return originalChatID
    }

    fun createMessage (senderUserID: Int, recipientUserID: Int, message: String) {
        val newMessage = Message(
            userID = senderUserID,
            text = message
        )
        addToMessageOriginalID(newMessage)
        val newChat = chats.firstOrNull {
            it.users.containsAll(listOf(senderUserID, recipientUserID))
        } ?.let {
                it.copy(
                    messages = it.messages + newMessage,
                    countMessage = it.countMessage + 1
                )
            } ?: Chat(
                users = listOf(senderUserID, recipientUserID),
                title = "Чат с пользователем $recipientUserID ",
                messages = listOf(newMessage),
                countMessage = 1
            )
        if (newChat.id == -1) {
            addToChatOriginalID(newChat)
            val newMessageID = newMessage.copy(chatID = originalChatID)
            messages.add(newMessageID)
        }

        chats.forEachIndexed { index, chat ->
            if (newChat.id == chat.id) {
                chats[index] = newChat
                return@forEachIndexed
            }
        }
    }

    fun deleteMessage (messageID: Int, chatID: Int) {
        messages.removeIf { messageID == it.id && chatID == it.chatID }
        chats.removeIf {it.countMessage == 0}
//        for (chat in chats) {
//            if (chat.countMessage == 0) {
//                deleteChat(chat.id)
//            }
//        }
    }

    fun editMessage (newMessage: Message): Boolean {
        messages.forEachIndexed { index, message ->
            if (newMessage.id == message.id) {
                messages[index] = message.copy(text = newMessage.text)
                return true
            }
        }
        println("сообщения с ${newMessage.id} не существует")
        return false
    }

    fun deleteChat (chatID: Int) {
        chats.removeIf {chatID == it.id}
        messages.removeIf {chatID == it.chatID}
    }

    fun getAllChats (userID: Int) : List<String> {
        val listOfChats = emptyList<String>().toMutableList()
        chats.firstOrNull {
            it.users.contains(userID)
        } ?. let {
            listOfChats += it.title
        } ?: return emptyList()
        return listOfChats
    }

    fun getAllMessageInChat (chatID: Int) : List<String> {
        val listOfMessages = emptyList<String>().toMutableList()
        chats.forEach { chat ->
            if (chat.id == chatID) {
                chat.messages.forEach {message ->
                    listOfMessages += message.text
                    val updatedMessage = chat.messages.map { it.copy(isRead = true) }
                    val updatedChat = chat.copy(
                        messages = updatedMessage,
                        countOfUnreadMessages = 0
                    )
                    chats.removeIf {updatedChat.id == it.id}
                    chats.add(updatedChat)
                }
//                messages.forEach { message ->
//                    if (message.chatID == chatID) {
//                        listOfMessages += message.text
//                        val updatedMessage = chat.messages.map { it.copy(isRead = true) }
//                        val updatedChat = chat.copy(messages = updatedMessage)
//                        chats.removeIf {updatedChat.id == it.id}
//                        chats.add(updatedChat)
//                    }
//                }
                val countOfMessageInChat = chat.countMessage
                println("В чате $countOfMessageInChat сообщений")
            }
        }
        return listOfMessages
    }

    fun getUnreadMessageInChat (chatID: Int) : List<String> {
        val listOfUnreadMessages = emptyList<String>().toMutableList()
        chats.forEach {chat ->
            if (chat.id == chatID) {
                chat.messages.filter { it.isRead }
                messages.forEach {
                    listOfUnreadMessages += it.text
                }
            }
        }
        return listOfUnreadMessages
    }

    fun getUnreadChatsCount (userID: Int) : Int{
        var chatCount = 0
        chats.firstOrNull {
            it.users.contains(userID)
        } ?.let {
            it.messages.forEach { message ->
                if (message.isRead) {
                    chatCount++
                    return@forEach
                } else {
                    println("Все чаты прочитаны")
                }
            }
        } ?: println("У юзера $userID нет чатов")
        return chatCount
    }

    fun getUnreadCountMessageInChat (chatID: Int) : Int {
        var unreadCount = 0
        chats.forEach {chat ->
            if (chat.id == chatID) {
                chat.messages.filter { it.isRead }
                unreadCount = messages.size
            }
        }
        return unreadCount
    }

    fun readMessage (messageID: Int): Boolean {
        messages.forEachIndexed { index1, message ->
            if (messageID == message.id) {
                messages[index1] = message.copy(isRead = true)
                chats.forEachIndexed { index2, chat ->
                    if (message.chatID == chat.id) {
                        chats[index2] = chat.copy(countOfUnreadMessages = chat.countOfUnreadMessages - 1)
                    }
                }
                return true
            }
        }
        println("сообщения с $messageID не существует")
        return false
    }

    fun removeAll () {
        chats.clear()
        messages.clear()
        originalMessageID = -1
        originalChatID = -1
    }
}