package com.traincon.modelleisenbahn_controller.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.traincon.modelleisenbahn_controller.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LauncherActivity extends AppCompatActivity {
    private EditText ipEntry;
    private EditText portEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar();

        ipEntry = findViewById(R.id.ipEntry);
        portEntry = findViewById(R.id.portEntry);

        loadLastConnectedBoard(ipEntry, portEntry);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            try {
                String host = Objects.requireNonNull(ipEntry.getText()).toString();
                int port = Integer.parseInt(Objects.requireNonNull(portEntry.getText()).toString());

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("host", host);
                intent.putExtra("port", port);
                saveLastConnectedBoard(host, port);
                startActivity(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                portEntry.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.animation_shake));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_locoList) {
            startActivity(new Intent(getBaseContext(), LocoConfigActivity.class));
        }

        if (item.getItemId() == R.id.action_console) {
            try {
                String host = Objects.requireNonNull(ipEntry.getText()).toString();
                int port = Integer.parseInt(Objects.requireNonNull(portEntry.getText()).toString());
                Intent intent = new Intent(getBaseContext(), ConsoleActivity.class);
                intent.putExtra("host", host);
                intent.putExtra("port", port);
                saveLastConnectedBoard(host, port);
                startActivity(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                portEntry.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.animation_shake));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveLastConnectedBoard(String host, int port) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("lastConnectedBoard", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastConnectedHost", host);
        editor.putString("lastUsedPort", Integer.toString(port));
        editor.apply();
    }

    private void loadLastConnectedBoard(EditText ipEntry, EditText portEntry) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("lastConnectedBoard", MODE_PRIVATE);
        ipEntry.setText(sharedPreferences.getString("lastConnectedHost", null));
        portEntry.setText(sharedPreferences.getString("lastUsedPort", null));
    }
}