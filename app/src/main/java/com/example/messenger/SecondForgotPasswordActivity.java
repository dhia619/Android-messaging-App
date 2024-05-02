package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SecondForgotPasswordActivity extends AppCompatActivity {

    boolean all_filled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_forgot_password);

        ImageButton backbtn = findViewById(R.id.backButton);
        Button verifybtn = findViewById(R.id.verifyButton);
        TextView resendbtn = findViewById(R.id.resend);

        TextInputLayout digits_inputs[] = {
                findViewById(R.id.digit1EditLayout),
                findViewById(R.id.digit2EditLayout),
                findViewById(R.id.digit3EditLayout),
                findViewById(R.id.digit4EditLayout)
        };
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        verifybtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                all_filled = true;
                for(TextInputLayout digit_input : digits_inputs) {
                    if(digit_input.getEditText().getText().toString().isEmpty()){
                        all_filled = false;
                        digit_input.setError("Fill");
                        break;
                    }
                }
                if(all_filled) {
                    //verify code logic
                    Intent intent = new Intent(SecondForgotPasswordActivity.this, ThirdForgotPasswordActivity.class);
                    startActivity(intent);
                }
            }
        });

        resendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resend verification logic
            }
        });

        for(TextInputLayout digit_input : digits_inputs) {
            Objects.requireNonNull(digit_input.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String digit = s.toString().trim();
                    if (digit.isEmpty()) {
                        digit_input.setError("Fill");
                    } else {
                        digit_input.setError(null);
                        //send email logic
                    }
                }
            });
        }

    }
}