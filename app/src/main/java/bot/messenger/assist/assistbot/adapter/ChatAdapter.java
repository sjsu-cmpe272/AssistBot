package bot.messenger.assist.assistbot.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bot.messenger.assist.assistbot.ChatMessage;
import bot.messenger.assist.assistbot.R;

/**
 * Created by sindhya on 12/2/16.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private final List<ChatMessage> botMessages;
    private Activity contxt;

    public ChatAdapter(Activity context, int res,List<ChatMessage> chatMessages) {
        super(context,res,chatMessages);
        this.contxt = context;
        this.botMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (botMessages != null) {
            return botMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (botMessages != null) {
            return botMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage chatMessage = getItem(position);
        LayoutInflater layoutInflater = (LayoutInflater) contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int layoutRes=0;

        if(chatMessage.isMe_flag()){
            layoutRes=R.layout.chat_list_item_right;
        }else{
            layoutRes=R.layout.chat_list_item_left;
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutRes,parent,false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtMessage.setText(chatMessage.getMessage());

        return convertView;
    }



    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.chat_msg);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;

    }

}
