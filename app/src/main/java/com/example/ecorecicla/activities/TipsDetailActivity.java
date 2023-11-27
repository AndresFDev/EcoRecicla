package com.example.ecorecicla.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ecorecicla.R;
import com.example.ecorecicla.models.Message;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

public class TipsDetailActivity extends AppCompatActivity {

    private MaterialButton btnBack;
    private MaterialTextView tvTips, tvTip1, tvTip2, tvTip3, tvBodyTip1, tvBodyTip2, tvBodyTip3;
    private ImageView ivTip1, ivTip2, ivTip3;
    private ShimmerFrameLayout shimmerLayout1, shimmerLayout2, shimmerLayout3;
    private static final String PREFS_KEY = "MyPrefs";
    private static final String[] RANDOM_MESSAGE_KEYS = {"randomMessage1", "randomMessage2", "randomMessage3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_detail);

        btnBack = findViewById(R.id.btnBack);
        tvTips = findViewById(R.id.tvTips);
        tvBodyTip1 = findViewById(R.id.tvBodyTip1);
        tvBodyTip2 = findViewById(R.id.tvBodyTip2);
        tvBodyTip3 = findViewById(R.id.tvBodyTip3);
        tvTip1 = findViewById(R.id.tvTip1);
        tvTip2 = findViewById(R.id.tvTip2);
        tvTip3 = findViewById(R.id.tvTip3);
        ivTip1 = findViewById(R.id.ivTip1);
        ivTip2 = findViewById(R.id.ivTip2);
        ivTip3 = findViewById(R.id.ivTip3);
        shimmerLayout1 = findViewById(R.id.shimmerLayout1);
        shimmerLayout2 = findViewById(R.id.shimmerLayout2);
        shimmerLayout3 = findViewById(R.id.shimmerLayout3);

        importData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void importData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String title = intent.getStringExtra("title");
        ArrayList<Message> messages = intent.getParcelableArrayListExtra("messages");

        if (title == null || messages == null) {
            return;
        }

        tvTips.setText(title);

        ArrayList<String> storedRandomMessages = getStoredRandomMessages(title);

        boolean isMonday = isMonday();

        if (isMonday || storedRandomMessages.isEmpty()) {
            ArrayList<Message> randomMessages = generateRandomMessages(messages, 3);
            setStoredRandomMessages(title, randomMessages);

            tvTip1.setText(randomMessages.get(0).getMessageTitle());
            tvBodyTip1.setText(randomMessages.get(0).getMessageBody());
            loadImageWithShimmer(ivTip1, shimmerLayout1, randomMessages.get(0).getMessageImage());

            tvTip2.setText(randomMessages.get(1).getMessageTitle());
            tvBodyTip2.setText(randomMessages.get(1).getMessageBody());
            loadImageWithShimmer(ivTip2, shimmerLayout2, randomMessages.get(1).getMessageImage());

            tvTip3.setText(randomMessages.get(2).getMessageTitle());
            tvBodyTip3.setText(randomMessages.get(2).getMessageBody());
            loadImageWithShimmer(ivTip3, shimmerLayout3, randomMessages.get(2).getMessageImage());
        } else {
            tvTip1.setText(messages.get(0).getMessageTitle());
            tvBodyTip1.setText(messages.get(0).getMessageBody());
            loadImageWithShimmer(ivTip1, shimmerLayout1, messages.get(0).getMessageImage());

            tvTip2.setText(messages.get(1).getMessageTitle());
            tvBodyTip2.setText(messages.get(1).getMessageBody());
            loadImageWithShimmer(ivTip2, shimmerLayout2, messages.get(1).getMessageImage());

            tvTip3.setText(messages.get(2).getMessageTitle());
            tvBodyTip3.setText(messages.get(2).getMessageBody());
            loadImageWithShimmer(ivTip3, shimmerLayout3, messages.get(2).getMessageImage());
        }
    }

    private void loadImageWithShimmer(ImageView imageView, ShimmerFrameLayout shimmerLayout, String imageUrl) {
        shimmerLayout.startShimmer();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            imageUrl = imageUrl;
        } else {
            imageUrl = "";
        }


        if (!imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageView.setImageResource(R.drawable.img_not_available);
                            shimmerLayout.stopShimmer();
                            shimmerLayout.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            shimmerLayout.stopShimmer();
                            shimmerLayout.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.img_not_available);
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }
    }

    private ArrayList<String> getStoredRandomMessages(String title) {
        SharedPreferences prefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        ArrayList<String> randomMessages = new ArrayList<>();
        for (String key : RANDOM_MESSAGE_KEYS) {
            String message = prefs.getString(title + key, null);
            if (message != null) {
                randomMessages.add(message);
            }
        }
        return randomMessages;
    }

    private void setStoredRandomMessages(String title, ArrayList<Message> messages) {
        SharedPreferences prefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            String key = RANDOM_MESSAGE_KEYS[i];

            if (message != null) {
                editor.putString(title + key, formatMessage(message));
            }
        }

        editor.apply();
    }

    private boolean isMonday() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5:00"));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.MONDAY;
    }

    private ArrayList<Message> generateRandomMessages(ArrayList<Message> messages, int count) {
        ArrayList<Message> randomMessages = new ArrayList<>();
        Random random = new Random();
        Set<Integer> selectedIndices = new HashSet<>();

        int messagesSize = messages.size();

        if (messagesSize == 0) {
            return randomMessages;
        }

        while (randomMessages.size() < count) {
            int randomIndex = random.nextInt(messagesSize);

            // Verifica si el mensaje ya ha sido seleccionado previamente
            if (!selectedIndices.contains(randomIndex)) {
                Message randomMessage = messages.get(randomIndex);
                randomMessages.add(new Message(randomMessage.getMessageTitle(), randomMessage.getMessageBody(), randomMessage.getMessageImage()));

                // Registra el índice del mensaje seleccionado
                selectedIndices.add(randomIndex);
            }
        }

        return randomMessages;
    }

    private String formatMessage(Message message) {
        return message.getMessageTitle() + "\n" + message.getMessageBody();
    }

}