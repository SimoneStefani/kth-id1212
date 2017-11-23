package common;

public class Message {
    private MessageType messageType;
    private String body;

    public Message(MessageType messageType, String body) {
        this.messageType = messageType;
        this.body = body;
    }

    /*
     * Transform message in string.
     */
    public static String serialize(Message message) {
        return message.messageType.toString() + "##" + message.body;
    }

    /*
     * Decode message from string.
     */
    public static Message deserialize(String message) {
        String[] parts = message.split("##");
        MessageType type = MessageType.valueOf(parts[0].toUpperCase());
        String body = parts.length > 1 ? parts[1] : "";
        return new Message(type, body);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getBody() {
        return body;
    }
}
