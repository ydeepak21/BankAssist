package com.example.bankassist;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class categoryitemlayout extends AppCompatActivity {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    Toolbar toolbar_category;
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryitemlayout);

        messageList = new ArrayList<>();
        httpClient = new OkHttpClient();

        toolbar_category = findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar_category);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        // Setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            System.out.println("Debug-Deepak:Send button press");
            String question = messageEditText.getText().toString().trim();
            System.out.println("Debug-Deepak:Question is equals to"+question);
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            welcomeTextView.setVisibility(View.GONE);

            // Send the user's message to Rasa
            sendToRasa(question);
        });
    }

    void addToChat(String message, String sentBy) {
        messageList.add(new Message(message, sentBy));
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        System.out.println("Debug-Deepak:Added to chat");
    }

    void sendToRasa(String message) {
        // Replace <rasa-server-ip> with the actual IP address or hostname of your Rasa server
        String rasaServerURL = "  https://8eaf-2400-1a00-b050-17f-359f-567c-5376-c9af.ngrok-free.app/webhooks/rest/webhook";

        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("sender", "user");
            jsonInput.put("message", message);
            System.out.println("Debug-Deepak:Inside Try catch");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Debug-Deepak:Going to send to rasa");

        RequestBody requestBody = RequestBody.create(JSON, jsonInput.toString());
        Request request = new Request.Builder()
                .url(rasaServerURL)
                .post(requestBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(categoryitemlayout.this, "Failed to communicate with Rasa", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    System.out.println("Debug-Deepak: Response="+responseBody);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonResponse = new JSONArray(responseBody);
                                for (int i = 0; i < jsonResponse.length(); i++) {
                                    JSONObject messageObj = jsonResponse.getJSONObject(i);
                                    String botResponse = messageObj.getString("text");
                                    addToChat(botResponse, Message.SENT_BY_BOT);
                                    System.out.println("Debug-Deepak:Send by bot");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(categoryitemlayout.this, "Failed to get response from Rasa", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
