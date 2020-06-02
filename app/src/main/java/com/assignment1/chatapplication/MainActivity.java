package com.assignment1.chatapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.assignment1.chatapplication.SignInOut.chatServer;


public class MainActivity extends AppCompatActivity {

    NavigationView settings_drawer;
    EditText textInput;
   // TextView txtOut;
    ImageButton sendButton, logOut, uploadButton;
    DrawerLayout mDrawerLayout;
    RecyclerView mMessageRecycler;

    boolean doubleBackToExitPressedOnce = false;
    public static final int PICK_IMAGE = 1;
    public static List<MessageAttr> msgList =  new ArrayList<>();
    //public String senderName = SignInOut.getUserUsername();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentUser = SignInOut.getUserUsername();

        settings_drawer = findViewById(R.id.settings_drawer);
        textInput = findViewById(R.id.TextInput);
        //txtOut = findViewById(R.id.txtOut);
        sendButton = findViewById(R.id.sendButton);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        logOut = findViewById(R.id.logOut_img);
        uploadButton = findViewById(R.id.uploadButton);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_name);
        actionbar.setTitle(currentUser);

        mMessageRecycler = findViewById(R.id.message_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMessageRecycler.setLayoutManager(linearLayoutManager);
        MessageListAdapter adapter = new MessageListAdapter(this, msgList);
        mMessageRecycler.setAdapter(adapter);

        textInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!textInput.getText().toString().isEmpty()){
                    /* send text */
                    MessageAttr message = new MessageAttr(1, textInput.getText().toString(), "tao");
                    try {
                        chatServer.getWorker().handleMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    msgList.add(message);
                    textInput.getText().clear();
                    return true;
                }
            }
            return false;
        });

        sendButton.setOnClickListener((View v) -> {
            if (!textInput.getText().toString().isEmpty()){
                MessageAttr message = new MessageAttr(1, textInput.getText().toString(), "tao");
                try {
                    chatServer.getWorker().handleMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msgList.add(message);
                textInput.getText().clear();
            }
        });

        logOut.setOnClickListener((View v) -> {
            try {
                chatServer.getWorker().logoffHandle();
            } catch (IOException e) {
                e.printStackTrace();
            }
                finish();
    });

        uploadButton.setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

    /* On message receive
     * do something here*/

    /* Create chat group*/

    /* View online status*/

    /* Create multiple CONCURRENT chat activities*/

    /* Send file */

    /*Add friend*/









    }

    public static List<MessageAttr> getMsgList() {
        return msgList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerLayout.openDrawer(settings_drawer);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
