package com.example.janej.andriodlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.janej.andriodlabs.StartActivity.ACTIVITY_NAME;

public class ChatWindowActivity extends Activity {
    ListView listView;
    EditText editText;
    Button button;
    ArrayList<String> messages;
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ChatAdapter messageAdapter;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        messageAdapter = new ChatAdapter(this);//Create a object to contain message
        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        messages = new ArrayList<>();
        listView.setAdapter(messageAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.add(editText.getText().toString());
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }
        private class ChatAdapter extends ArrayAdapter<String> {
            public ChatAdapter(Context ctx) {
                super(ctx, 0);
            }

            public int getCount() {
                return messages.size();
            }

            public String getItem(int position) {
                return messages.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();

            View result = null;
                if (position % 2 == 0) {
                    result = inflater.inflate(R.layout.chat_row_incoming, null);
                    TextView messageIn = result.findViewById(R.id.messageTextIn);
                    messageIn.setText(getItem(position)); // get the string at position
                } else {
                    result = inflater.inflate(R.layout.chat_row_outgoing, null);
                    TextView messageOut =  result.findViewById(R.id.messageTextOut);
                    messageOut.setText(getItem(position)); // get the string at position
                }

                return result;
            }


        }


    }
