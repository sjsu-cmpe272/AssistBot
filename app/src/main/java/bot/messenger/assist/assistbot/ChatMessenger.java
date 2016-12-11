package bot.messenger.assist.assistbot;

import android.content.Context;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bot.messenger.assist.assistbot.adapter.ChatAdapter;
import bot.messenger.assist.assistbot.extras.GpsTracker;
import bot.messenger.assist.assistbot.utility.ApiException;
import bot.messenger.assist.assistbot.utility.BaseAsyncRequest;

/**
 * Created by sindhya on 12/1/16.
 */
public class ChatMessenger extends AppCompatActivity {

    private EditText msgEditText;
    private ListView msgListView;
    private ImageView sendBtn;
    private List<ChatMessage> messageList;
    private ArrayAdapter<ChatMessage> chatAdapter;




    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        setContentView(R.layout.messenger_chat_layout);
        setListeners();
        getLocation();

    }

    private void setListeners(){

        msgEditText=(EditText)findViewById(R.id.message_edit_txt);
        msgListView=(ListView)findViewById(R.id.msg_list_view);
        sendBtn=(ImageView)findViewById(R.id.send_btn);

        messageList=new ArrayList<>();
        chatAdapter=new ChatAdapter(this,R.layout.chat_list_item_left,messageList);
        msgListView.setAdapter(chatAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("hi","hello");
                String msgText=msgEditText.getText().toString();
                if(TextUtils.isEmpty(msgText)){
                    return;
                }

                ChatMessage chatMessage=new ChatMessage(msgEditText.getText().toString(),true);
                messageList.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
                msgEditText.setText("");

                botService(chatMessage.getMessage());
            }
        });
    }

    public void botService(String client_message){

        GetMessage getMessage = new GetMessage(client_message,getLocation());
        getMessage.execute((Void) null);


    }

    public Location getLocation() {
        GpsTracker gpsTracker=new GpsTracker(this);
        Location location=null;
        if(gpsTracker.locationFlag()){
            location = gpsTracker.getLocation();
            //Log.d("location",String.valueOf(location.getLatitude()));
            //Log.d("latitude",String.valueOf(location.getLongitude()));
        }
        return location;
    }




    public class GetMessage extends BaseAsyncRequest {

        protected final String message;
        Location gps_location=null;


        GetMessage(String msg,Location location) {

            this.message = msg;
            this.gps_location=location;
        }

        @Override
        protected void doSetup() throws ApiException, JSONException {
            serviceName = "bot";
            endPoint = "message";
            verb = "POST";
            requestBody = new JSONObject();
            requestBody.put("message", message);
            if(gps_location!=null) {
                JSONObject location = new JSONObject();
                location.put("latitude", gps_location.getLatitude());
                location.put("longitude", gps_location.getLongitude());
                requestBody.put("location", location);
            }
        }

        @Override
        protected void processResponse(final String response) throws ApiException, JSONException {
            Log.e("response", response);
            JSONObject jsonObject = new JSONObject(response);

            Log.d("response", response);

            Thread myThread = new Thread() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (response != "null") {


                                    StringBuilder chatMsgList = new StringBuilder();
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("hi",jsonObject.toString());
                                    String type = jsonObject.getString("type");
                                    if (type.equals("yelp")) {
                                        JSONArray jsonArray = (jsonObject).getJSONArray("data");

                                        for (int i = 0; i < 10; i++) {
                                            chatMsgList=new StringBuilder();
                                            String rest_name = jsonArray.getJSONObject(i).getString("name");
                                            String rest_rating = jsonArray.getJSONObject(i).getString("rating");
                                            String rest_addr = "";
                                            JSONArray addrArray = jsonArray.getJSONObject(i).getJSONObject("location").getJSONArray("display_address");
                                            for (int j = 0; j < addrArray.length(); j++) {
                                                rest_addr += addrArray.get(j);
                                            }

                                            chatMsgList.append(rest_name + "\n " +"Rating:"+ rest_rating + "\n " +"Address:"+ rest_addr + "\n\n");
                                            //ChatMessage chatMessage=new ChatMessage(rest_name+"\n "+rest_rating+"\n "+rest_addr,false);
                                            //messageList.add(chatMessage);
                                            ChatMessage chatMessage = new ChatMessage(chatMsgList.toString(), false);
                                            messageList.add(chatMessage);
                                            chatAdapter.notifyDataSetChanged();

                                        }

                                    }else if(type.equals("cars")){
                                        //weather

                                        JSONArray jsonArray=jsonObject.getJSONArray("cars");
                                        for(int i=0;i<5;i++) {

                                            chatMsgList=new StringBuilder();
                                            String vehicle = jsonArray.getJSONObject(i).getString("vehicle");
                                            String price=jsonArray.getJSONObject(i).getString("price");
                                            String seat=jsonArray.getJSONObject(i).getString("seat");
                                            String pickupAddr=jsonArray.getJSONObject(i).getString("pickupAddress");

                                            chatMsgList.append(vehicle+"\n"+price+"\n"+seat+"\n"+pickupAddr);
                                            ChatMessage chatMessage=new ChatMessage(chatMsgList.toString(),false);
                                            messageList.add(chatMessage);
                                            chatAdapter.notifyDataSetChanged();
                                        }
                                    }else if(type.equals("hotel") && jsonObject!=null && jsonObject.getJSONArray("hotels")!=null){
                                        JSONArray jsonArray=jsonObject.getJSONArray("hotels");
                                        for(int i=0;i<5;i++){
                                            chatMsgList=new StringBuilder();
                                            String name = jsonArray.getJSONObject(i).getString("name");
                                            String hotelId=jsonArray.getJSONObject(i).getString("hotelId");
                                            String rating=jsonArray.getJSONObject(i).getString("starRating");
                                            String price=jsonArray.getJSONObject(i).getString("price");

                                            chatMsgList.append(name+"\n"+"hotel id:"+hotelId+"\n"+"rating:"+rating+"\n"+"price:"+price+"\n\n");
                                            ChatMessage chatMessage=new ChatMessage(chatMsgList.toString(),false);
                                            messageList.add(chatMessage);
                                            chatAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    else if(type.equals("weather")){
                                        chatMsgList=new StringBuilder();
                                        JSONObject jsonObj = (jsonObject).getJSONObject("data");
                                        String location= jsonObj.getString("location");
                                        String temperate=jsonObj.getString("temperature");
                                        String pressure=jsonObj.getString("pressure");
                                        String humidity=jsonObj.getString("humidity");

                                        chatMsgList.append(location+"\n"+"temperature:"+temperate+" Kelvis"+"\n"+"pressure:"+pressure+"\n"+"humidity:"+humidity);
                                        ChatMessage chatMessage=new ChatMessage(chatMsgList.toString(),false);
                                        messageList.add(chatMessage);
                                        chatAdapter.notifyDataSetChanged();

                                    }
                                }
                                }catch(JSONException e1){
                                    e1.printStackTrace();
                                }

                        }
                    });
                    super.run();
                }
            };
            myThread.start();

        }

        @Override
        protected void onCompletion(boolean success) {
            if (!success) {
                //Show_Message_uithread("Failure");
            }
            else
            {

            }
        }
    }

}
