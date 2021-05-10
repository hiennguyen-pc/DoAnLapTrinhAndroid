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

import com.bumptech.glide.Glide;
import com.example.chatfirebase_final.Adapter.MessageAdapter;
import com.example.chatfirebase_final.Fragment.APIService;
import com.example.chatfirebase_final.Model.Chat;
import com.example.chatfirebase_final.Model.User;
import com.example.chatfirebase_final.Notifications.Client;
import com.example.chatfirebase_final.Notifications.Data;
import com.example.chatfirebase_final.Notifications.MyResponse;
import com.example.chatfirebase_final.Notifications.Sender;
import com.example.chatfirebase_final.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMain extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView username1;
    String userName2;
    Intent intent;
    FirebaseUser firebaseUser;
    Toolbar toolbar;
    ImageButton btn_send;
    EditText txt_text;
    DatabaseReference databaseReference;


    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    ValueEventListener seenlistener;

    String img;

    APIService apiService;
    boolean notify=false;

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

                startActivity(new Intent(ChatMain.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        intent=getIntent();
        userName2=intent.getStringExtra("UserName");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify=true;
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
                //circleImageView.setImageResource(R.mipmap.ic_launcher);
                if(user.getImageInfo().equals("default")){
                    circleImageView.setImageResource(R.mipmap.ic_launcher);

                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageInfo()).into(circleImageView);
                }


                readMessage(firebaseUser.getUid(),userName2,img);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        seenMessage(userName2);

    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

        DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid()).child(userName2);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userName2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(notify) {

                    sendNotifiaction(receiver, user.getUser(), msg);

                }
                    notify = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotifiaction(String receiver,final String username,final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");

        Query query =tokens.orderByKey().equalTo(receiver);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Token token=dataSnapshot.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+":"+message,"Tin nhắn mới",userName2);
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

//                            if(response.code()==200){
//
//                                if(response.body().success != 1){
//                                    Toast.makeText(ChatMain.this,"Failed",Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
                            Toast.makeText(ChatMain.this,""+response.message(),Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenMessage(String userid){
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenlistener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(userid)){
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    private void status(String status){
        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        Map<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenlistener);
        status("offline");
    }
}