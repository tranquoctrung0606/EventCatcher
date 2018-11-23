package com.linh.wiinav.ui;

import android.annotation.SuppressLint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.linh.wiinav.R;
import com.linh.wiinav.models.AskHelp;

import java.util.Calendar;

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
    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {
        btnAskHelp.setOnClickListener(v -> {
            //Getting data for asking help object
            AskHelp askHelp = new AskHelp();
            askHelp.setTitle(etAskHelpTitle.getText().toString());
            askHelp.setContent(etAskHelpContent.getText().toString());
            askHelp.setPostDate(Calendar.getInstance().getTime().toString());
            //Generate id for asking help object
            //Set poster for asking help object
            //Get current location and set it for asking help object

            //
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
