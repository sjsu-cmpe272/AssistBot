package bot.messenger.assist.assistbot.data;

import android.graphics.Bitmap;

/**
 * Created by sindhya on 12/1/16.
 */
public class BotList {

    String bot_name=null;
    String bot_desc=null;

    public BotList(String botName,String botDesc){
        this.bot_name=botName;
        this.bot_desc=botDesc;
    }

    public String getBot_name() {
        return bot_name;
    }

    public void setBot_name(String bot_name) {
        this.bot_name = bot_name;
    }

    public String getBot_desc() {
        return bot_desc;
    }

    public void setBot_desc(String bot_desc) {
        this.bot_desc = bot_desc;
    }

}
