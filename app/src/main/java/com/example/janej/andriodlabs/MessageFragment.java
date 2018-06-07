package com.example.janej.andriodlabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class
MessageFragment extends Fragment{
    private View view;
    private TextView messageTV;
    private TextView idTV;
    private Button deleteButton;
    private Bundle bundle;
    protected ChatWindowActivity chatWindow  ;

    public MessageFragment(){
        super();
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(ChatWindowActivity window){
        chatWindow =window;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        bundle = getArguments();
        view = inflater.inflate(R.layout.fragment_message,container,false);
        messageTV = (TextView) view.findViewById(R.id.TextViewMessage);
        messageTV.setText(bundle.getString("message"));
        idTV = (TextView) view.findViewById(R.id.TextViewId);
        idTV.setText("ID: "+ bundle.getLong("id"));
        deleteButton = (Button) view.findViewById(R.id.messageButton);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatWindow==null){
                    Intent intent = new Intent(getActivity(),MessageFragment.class);
                    intent.putExtra("id",bundle.getLong("id"));
                    getActivity().setResult(100,intent);
                    getActivity().finish();
                }else{
                    getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                    chatWindow.deleteMessage(bundle.getLong("id"));

                }
            }
        });
        return view;
    }
}

