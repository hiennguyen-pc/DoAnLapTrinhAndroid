package com.example.chatfirebase_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registation extends AppCompatActivity {
    EditText userName,email,pass,confirmPass;
    FirebaseAuth Auth;
    FirebaseFirestore fstore;
    FloatingActionButton btnDangki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        userName=findViewById(R.id.userName);
        email=findViewById(R.id.Email);
        pass=findViewById(R.id.PassWord);
        confirmPass=findViewById(R.id.ConfirmPassWord);
        btnDangki=findViewById(R.id.btnDangki);
        Auth=FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_userName=userName.getText().toString().trim();
                String txt_email=email.getText().toString().trim();
                String txt_pass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(txt_userName)||TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(Registation.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                }else if(!txt_pass.equals(confirmPass.getText().toString())){
                    Toast.makeText(Registation.this,"Mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show();
                }
                else if(txt_pass.length()<6){
                    Toast.makeText(Registation.this,"Mật khẩu phải lớn hơn 6 kí tự",Toast.LENGTH_SHORT).show();
                }else {
                    Auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user=Auth.getCurrentUser();
                            Toast.makeText(Registation.this,"Đăng kí thành công",Toast.LENGTH_SHORT).show();
                            DocumentReference df= fstore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo=new HashMap<>();
                            userInfo.put("user",txt_userName);
                            userInfo.put("email",txt_email);
                            userInfo.put("pass",txt_pass);
                            userInfo.put("imageInfo","default");
                            df.set(userInfo);
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registation.this,"Tạo tài khoản không thành công",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}