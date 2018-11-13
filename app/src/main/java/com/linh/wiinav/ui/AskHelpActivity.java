package com.linh.wiinav.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.linh.wiinav.R;
import com.linh.wiinav.models.AskHelp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AskHelpActivity extends AppCompatActivity {
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
    private void addEvents() {
        btnAskHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting data for asking help object
                AskHelp askHelp = new AskHelp();
                askHelp.setTitle(etAskHelpTitle.getText().toString());
                askHelp.setContent(etAskHelpContent.getText().toString());
                askHelp.setPostDate(Calendar.getInstance().getTime().toString());
                //Generate id for asking help object
                //Set poster for asking help object
                //Get current location and set it for asking help object

                //
                sendAskHelp(askHelp);
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
    }

    /* This method adds controls into this activity
     *
     * Version: 1.0
     *
     * Date: 13/11/2018
     *
     * Author: Nghiên
     */
    private void addControls() {
        etAskHelpTitle = findViewById(R.id.etAskHelpTitle);
        etAskHelpContent = findViewById(R.id.etAskHelpContent);
        btnAskHelp = findViewById(R.id.btnAskHelp);
    }
}
