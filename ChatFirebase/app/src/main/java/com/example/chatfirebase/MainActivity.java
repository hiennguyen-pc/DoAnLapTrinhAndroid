package com.example.chatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    FloatingActionButton btnDangnhap;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emaill);
        pass=findViewById(R.id.passWordd);
        btnDangnhap=findViewById(R.id.btnDangNhap);
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Email=email.getText().toString().trim();
                String txt_pass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(txt_Email)||TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(MainActivity.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                }
                else auth.signInWithEmailAndPassword(txt_Email,txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}