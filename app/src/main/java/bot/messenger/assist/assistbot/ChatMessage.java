package bot.messenger.assist.assistbot;

/**
 * Created by sindhya on 12/2/16.
 */
public class ChatMessage {

    private boolean me_flag;
    private String message;
    private String dateTime;
    private Long userId;


    public ChatMessage(String msg_text,boolean me_flag){

        this.message=msg_text;
        this.me_flag=me_flag;
    }

    public boolean isMe_flag() {
        return me_flag;
    }

    public void setMe_flag(boolean me_flag) {
        this.me_flag = me_flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
