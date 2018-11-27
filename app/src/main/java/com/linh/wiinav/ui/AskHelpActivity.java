package com.linh.wiinav.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.linh.wiinav.R;
import com.linh.wiinav.models.AskHelp;
import com.linh.wiinav.models.Comment;
import com.linh.wiinav.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.linh.wiinav.enums.User.BIRTHDAY;
import static com.linh.wiinav.enums.User.EMAIL;
import static com.linh.wiinav.enums.User.IDENTIFY_CARD;
import static com.linh.wiinav.enums.User.IS_BANNED;
import static com.linh.wiinav.enums.User.IS_VERIFIED_EMAIL;
import static com.linh.wiinav.enums.User.NUMBER_ASK;
import static com.linh.wiinav.enums.User.PHONE_NUMBER;
import static com.linh.wiinav.enums.User.USERNAME;

public class AskHelpActivity extends BaseActivity {
    private static final String TAG = "AskHelpActivity";

    EditText etAskHelpTitle, etAskHelpContent;
    ImageButton btnAskHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_help);
        addControls();
        addEvents();
    }

    /* This method adds events into this activitty
     *
     * Version: 1.0
     *
     * Date: 13/11/2018
     *
     * Author: Nghiên
     */
    @Override
    protected void addEvents() {
        btnAskHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting data for asking help object
                AskHelp askHelp = new AskHelp();
                askHelp.setId(UUID.randomUUID().toString());
                askHelp.setTitle(etAskHelpTitle.getText().toString());
                askHelp.setContent(etAskHelpContent.getText().toString());
                askHelp.setPostDate(Calendar.getInstance().getTime().toString());
                askHelp.setLatitude(Double.parseDouble(sharedPreferences.getString("LAT", "0")));
                askHelp.setLongitude(Double.parseDouble(sharedPreferences.getString("LONG", "0")));
                askHelp.setCompleted(false);
                ArrayList<Comment> comments = new ArrayList<>();
                comments.add(new Comment("ad", getUser(), "ádasdasdasd",new Date()));
                askHelp.setComments(comments);

                askHelp.setPoster(getUser());
                //Generate id for asking help object
                //Set poster for asking help object
                //Get current location and set it for asking help object

                //
                sendAskHelp(askHelp);

                backToMapsScreen();
            }
        });
    }

    /* This method saves asking help request to database
     *
     * Version: 1.0
     *
     * Date: 13/11/2018
     *
     * Author: Nghiên
     */
    private void sendAskHelp(AskHelp askHelp) {
        databaseReference.child("askHelps").child(askHelp.getId()).setValue(askHelp)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "onClick: insert a new ask help successfully");
                    showToastMessage("Posted");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onClick: insert a new ask help failure");
                    showToastMessage("Posted fail. Try later.");
                });
    }

    /* This method adds controls into this activity
     *
     * Version: 1.0
     *
     * Date: 13/11/2018
     *
     * Author: Nghiên
     */
    @Override
    protected void addControls() {
        sharedPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);

        etAskHelpTitle = findViewById(R.id.etAskHelpTitle);
        etAskHelpContent = findViewById(R.id.etAskHelpContent);
        btnAskHelp = findViewById(R.id.btnAskHelp);
    }
}