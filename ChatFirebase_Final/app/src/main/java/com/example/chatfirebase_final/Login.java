package com.example.chatfirebase_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText email,pass;
    FloatingActionButton btnDangnhap;
    FirebaseAuth auth;
    FirebaseFirestore fstone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        fstone=FirebaseFirestore.getInstance();
        email=findViewById(R.id.emaill);
        pass=findViewById(R.id.passWordd);
        btnDangnhap=findViewById(R.id.btnDangNhap);
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_Email=email.getText().toString().trim();
                String txt_pass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(txt_Email)||TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(Login.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                }

                else {
                    auth.signInWithEmailAndPassword(txt_Email,txt_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this,"Sai Email hoặc mật khẩu",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
    protected void OnStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}