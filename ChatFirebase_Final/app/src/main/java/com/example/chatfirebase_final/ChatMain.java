package com.example.chatfirebase_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.example.chatfirebase_final.Adapter.MessageAdapter;
import com.example.chatfirebase_final.Model.Chat;
import com.example.chatfirebase_final.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMain extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView username1;
    Intent intent;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    ImageButton btn_send;
    EditText txt_text;
    DatabaseReference databaseReference;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        circleImageView=findViewById(R.id.profile_imageChat);
        username1=findViewById(R.id.userNameMainChat);
        toolbar=findViewById(R.id.toolbarChat);
        btn_send=findViewById(R.id.btn_send);
        txt_text=findViewById(R.id.textMess);
        recyclerView=findViewById(R.id.RecyclerviewChat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent=getIntent();
        String userName2=intent.getStringExtra("UserName");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
                String msg=txt_text.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userName2,msg);
                }else {
                    Toast.makeText(getApplicationContext(),"Không gửi tin nhắn rỗng",Toast.LENGTH_SHORT).show();
                }
                txt_text.setText("");
            }
        });

        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userName2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                User user=snapshot.getValue(User.class);
                username1.setText(user.getUser());
                circleImageView.setImageResource(R.mipmap.ic_launcher);
                User a=new User();
                a=snapshot.getValue(User.class);

                username1.setText(a.getUser().toString());
                circleImageView.setImageResource(R.mipmap.ic_launcher);

                readMessage(firebaseUser.getUid(),userName2,img);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }
    private void readMessage(String myid, String username, String ImageURL){
        mchat=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Chat chat=snapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid)&&chat.getSender().equals(username)||chat.getReceiver().equals(username)&&chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }
                    messageAdapter=new MessageAdapter(ChatMain.this,mchat,img);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}