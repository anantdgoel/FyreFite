package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    TextView mEmail, mName, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email);
        mName = findViewById(R.id.name);
        mPassword = findViewById(R.id.password);

        final SharedPreferences pref = getSharedPreferences("MYPREFS", MODE_PRIVATE);

        Button mRegisterButton = findViewById(R.id.register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                if(mEmail.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty() || mName.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(), "Fill out all the fields", Toast.LENGTH_LONG).show();

                }else{

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Email", mEmail.getText().toString());
                    editor.putString("Name", mName.getText().toString());
                    editor.putString("Password", mPassword.getText().toString());
                    editor.apply();
                    startActivity(intent);

                }

            }
        });



    }
}
