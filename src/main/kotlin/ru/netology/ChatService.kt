package ru.netology

object ChatService {

    private val chats = mutableListOf<Chat>()
    private val messages = mutableListOf<Message>()
    private var originalMessageID = -1
    private var originalChatID = -1

    fun createMessage (senderUserID: Int, recipientUserID: Int, message: String) {
        val newMessage = Message(
            id = ++originalMessageID,
            userID = senderUserID,
            text = message
        )
        messages += newMessage
        val newChat = chats.firstOrNull {
            it.users.containsAll(listOf(senderUserID, recipientUserID))
        } ?.let {
                it.copy(
                    messages = it.messages + newMessage,
                    countMessage = it.countMessage + 1,
                    countOfUnreadMessages = it.countOfUnreadMessages + 1
                )
            } ?: Chat(
                id = ++originalChatID,
                users = listOf(senderUserID, recipientUserID),
                title = "Чат с пользователем $recipientUserID ",
                messages = listOf(newMessage),
                countMessage = 1
            )

        if (chats.size == originalChatID) {
            chats +=newChat
        } else {
            chats.forEachIndexed { index, chat ->
                if (newChat.id == chat.id) {
                    chats[index] = newChat
                    return@forEachIndexed
                }
            }
        }
    }

    fun deleteMessage (messageID: Int, chatID: Int) {
        val deletedMessage = messages.filter { it.id == messageID }
        val newListOfMessage = chats
            .first {
                it.id == chatID }
            .messages.minus(deletedMessage.toSet())

        chats.forEachIndexed { index, chat ->
            if (chatID == chat.id) {
                chats[index] = chat.copy(
                    messages = newListOfMessage,
                    countMessage = chat.countMessage - 1,
                    countOfUnreadMessages = chat.countOfUnreadMessages - 1
                )
                return@forEachIndexed
            }
        }
        chats.removeIf {it.countMessage == 0}
        messages.removeIf { messageID == it.id }
    }

    fun deleteChat (chatID: Int) {
        val deletedMessage = chats
            .first() {
            it.id == chatID
        }
            .messages
        messages.removeAll(deletedMessage)
        chats.removeIf {chatID == it.id}

    }

    fun editMessage (newMessage: Message, chatID: Int): Boolean {
        var listWithEditedMessage = listOf<Message>()
        messages.forEachIndexed { index, message1 ->
            if (newMessage.id == message1.id
                && newMessage.userID == message1.userID
            ) {
                messages[index] = message1.copy(text = newMessage.text)
                chats
                    .first {
                        it.id == chatID }
                    .messages.forEach { message2 ->
                        if (newMessage.id == message2.id) {
                            val editedMessage = message2.copy (text = newMessage.text)
                        }
                    }
                // TODO доделать метод

            } else {
                println("Сообщения с указанными параметрами не существует")
                return false
            }
        }
        chats.forEachIndexed { index2, chat ->
            if (chatID == chat.id) {
                chats[index2] = chat.copy( messages = listWithEditedMessage )
                return@forEachIndexed
            }
        }
        return true
    }

    fun getAllChats (userID: Int) : List<String> {
        val listOfChats = emptyList<String>().toMutableList()
        chats
            .filter { it.users.contains(userID) }
            .forEach { listOfChats += it.title }
        return listOfChats
    }

    fun getAllMessageInChat (chatID: Int) : List<String> {
        val listOfMessages = emptyList<String>().toMutableList()
        val chat = chats.firstOrNull { it.id == chatID } ?: return emptyList()
        chat.messages.forEach { listOfMessages += it.text }
        val updatedMessage = chat.messages.map { it.copy(isRead = true) }
        val updatedChat = chat.copy(
            messages = updatedMessage,
            countOfUnreadMessages = 0
        )
        chats.removeIf { it.id == updatedChat.id }
        chats.add(updatedChat)
        println("В чате ${chat.countMessage} сообщений")
        return listOfMessages
    }

    fun getUnreadMessageInChat(chatID: Int): List<String> {
        val chat = chats.firstOrNull { it.id == chatID } ?: return emptyList()
        return chat.messages
            .filter { it.isRead }
            .map { it.text }
    }

    fun getUnreadChatsCount (userID: Int) : Int{
        var unreadChatsCount = 0
        chats
            .filter { it.users.contains(userID) }
            .forEach {
                it.messages.first { message -> message.isRead }
                unreadChatsCount++
            }
        return unreadChatsCount
    }

    fun getUnreadCountMessageInChat (chatID: Int) : Int {
        chats
            .filter {
                it.id == chatID }
            .forEach {
                it.messages.filter { message ->
                    message.isRead }
                return messages.size
            }
        return 0
    }

    fun readMessage (messageID: Int, chatID: Int): Boolean {
        messages.forEachIndexed { index1, message ->
            if (message.id == messageID) {
                messages[index1] = message.copy(isRead = true)
                val updatedListOfMessage = messages
                chats.forEachIndexed { index2, chat ->
                    if (chatID == chat.id) {
                        chats[index2] = chat.copy(
                            messages = updatedListOfMessage,
                            countOfUnreadMessages = chat.countOfUnreadMessages - 1
                        )
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