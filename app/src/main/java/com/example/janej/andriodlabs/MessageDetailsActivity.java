package com.example.janej.andriodlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetailsActivity extends Activity {
    Bundle bundle;
    MessageFragment messageFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        bundle=getIntent().getExtras();//get bundle from another activity
        messageFragment=new MessageFragment();
        messageFragment.setArguments(bundle);
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameDetail,messageFragment);
        fragmentTransaction.commit();
    }
}
