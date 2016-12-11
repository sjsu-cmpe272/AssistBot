package bot.messenger.assist.assistbot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bot.messenger.assist.assistbot.R;
import bot.messenger.assist.assistbot.data.BotList;

/**
 * Created by sindhya on 12/1/16.
 */
public class BotListAdapter extends ArrayAdapter<BotList> {


    private ArrayList<BotList> botList;
    private static LayoutInflater inflater=null;

    public BotListAdapter(Context context, int textViewResourceId, ArrayList<BotList> bot_list){
        super(context,textViewResourceId,bot_list);
        this.botList=new ArrayList<BotList>();
        this.botList.addAll(bot_list);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder{
        TextView txt_bot_name;
        TextView txt_bot_desc;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.bot_list_item,null);
            holder = new ViewHolder();
            holder.txt_bot_name = (TextView) convertView.findViewById(R.id.bot_title);
            holder.txt_bot_desc = (TextView) convertView.findViewById(R.id.bot_desc);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        BotList bot=botList.get(position);
        holder.txt_bot_name.setText(bot.getBot_name());
        holder.txt_bot_desc.setText(bot.getBot_desc());
        return convertView;
    }

}
