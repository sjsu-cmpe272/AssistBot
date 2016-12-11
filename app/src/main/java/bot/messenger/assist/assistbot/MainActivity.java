package bot.messenger.assist.assistbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import bot.messenger.assist.assistbot.adapter.BotListAdapter;
import bot.messenger.assist.assistbot.data.BotList;

public class MainActivity extends AppCompatActivity {

    ListView botListView;
    BotListAdapter botListAdapter;
    ArrayList<BotList> botList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListeners();

    }

    private void setListeners(){

        BotList botListObj=new BotList("Assist Bot","Location assistance");
        botList.add(botListObj);

        botListView=(ListView)findViewById(R.id.bot_list_view);
        botListAdapter=new BotListAdapter(MainActivity.this, R.layout.bot_list_item, botList);
        botListView.setAdapter(botListAdapter);


        botListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent chat_intent=new Intent(MainActivity.this,ChatMessenger.class);
                startActivity(chat_intent);
            }
        });
    }
}
