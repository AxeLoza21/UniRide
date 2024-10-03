import java.util.List;

public class Chat {
    private List<String> users;
    private String lastMessage;  // Último mensaje enviado en el chat
    private String chatId;       // El ID del chat

    // Constructor vacío para Firebase
    public Chat() {}

    public Chat(List<String> users, String lastMessage) {
        this.users = users;
        this.lastMessage = lastMessage;
    }

    // Getters y Setters
    public List<String> getUsers() { return users; }
    public void setUsers(List<String> users) { this.users = users; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }
}
