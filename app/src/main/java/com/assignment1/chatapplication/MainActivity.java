package com.assignment1.chatapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    NavigationView settings_drawer;
    EditText textInput;
    TextView txtOut;
    ImageButton sendButton, logOut, uploadButton;
    DrawerLayout mDrawerLayout;
    PopupWindow popUp;

    boolean doubleBackToExitPressedOnce = false;
    public static final int PICK_IMAGE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings_drawer = findViewById(R.id.settings_drawer);
        textInput = findViewById(R.id.TextInput);
        txtOut = findViewById(R.id.txtOut);
        sendButton = findViewById(R.id.sendButton);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        logOut = findViewById(R.id.logOut_img);
        uploadButton = findViewById(R.id.uploadButton);

        popUp = new PopupWindow(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_name);


        textInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (textInput.getText().toString().isEmpty()){
                    //do nothing
                }
                else {
                    /* send text */
                    txtOut.append( /* user name: + */ "\n" + textInput.getText());
                    textInput.getText().clear();
                    return true;
                }
            }
            return false;
        });

        sendButton.setOnClickListener((View v) -> {
            if (textInput.getText().toString().isEmpty()){
                //do nothing
            }
            else {
                /* send text */
                txtOut.append( /* user name: + */ "\n" + textInput.getText());
                textInput.getText().clear();
            }
        });

        logOut.setOnClickListener((View v) -> finish());

        uploadButton.setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });











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
