package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.messenger.Chat;
import com.example.messenger.ImageHandling;
import com.example.messenger.MessagingActivity;
import com.example.messenger.R;
import com.example.messenger.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;

    String last_msg;

    public UserAdapter(Context mContext, List<User> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getFull_name());
        lastMessage(user.getId(), holder.last_msg_view);
        if (user.getProfile_image().equals("")){
            holder.profile_img.setImageResource(R.mipmap.ic_default_avatar_round);
        } else {
            String base64image = user.getProfile_image();
            byte[] image_data = ImageHandling.getImageBytesFromBase64(base64image);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image_data, 0, image_data.length);
            holder.profile_img.setImageBitmap(bitmap);
            //Glide.with(mContext).load(user.getProfile_image()).into(holder.profile_img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessagingActivity.class);
                intent.putExtra("userId",user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_img;

        public TextView last_msg_view;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_img = itemView.findViewById(R.id.profile_img);
            last_msg_view = itemView.findViewById(R.id.last_msg);
        }
    }
    private void lastMessage(String userId, TextView last_msg_view){
        last_msg = "";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase
                .getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert firebaseUser != null;
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)
                    || chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){

                        last_msg = chat.getMessage();

                        if (Objects.equals(last_msg, "")){
                            last_msg_view.setText("new friend");
                        }else{
                            last_msg_view.setText(last_msg);
                        }
                    }
                    last_msg = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
