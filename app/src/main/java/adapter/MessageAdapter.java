package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.messenger.Chat;
import com.example.messenger.MessagingActivity;
import com.example.messenger.R;
import com.example.messenger.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import kotlin.text.UStringsKt;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int LEFT_MSG = 0;
    public static final int RIGHT_MSG = 1;

    private Context mContext;
    private List<Chat> mChat;

    private String img;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat,String img ){
        this.mChat = mChat;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == RIGHT_MSG) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,  parent, false);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,  parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        // Check if profile_img is not null before setting image resource
        if (holder.profile_img != null) {
            if (img != null && !img.isEmpty()) {
                // Load image using Glide or any other image loading library
                // Glide.with(mContext).load(img).into(holder.profile_img);
            } else {
                // Set a default image if img is null or empty
                holder.profile_img.setImageResource(R.mipmap.ic_default_avatar_round);
            }
        } else {
            Log.e("MessageAdapter", "profile_img is null");
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_img;
        public ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_img = itemView.findViewById(R.id.profile_img);
        }
    }
    public byte[] getImageBytesFromBase64(String base64Image) {
        // Decode the Base64 string into a byte array
        byte[] imageData = Base64.decode(base64Image, Base64.DEFAULT);
        return imageData;
    }

    /*
    // Assuming you have a Base64-encoded image string stored in a variable called base64Image
    byte[] imageData = getImageBytesFromBase64(base64Image);

    // Convert the byte array to a Bitmap
    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

    // Display the bitmap in an ImageView
    ImageView imageView = findViewById(R.id.your_image_view_id);
    imageView.setImageBitmap(bitmap);
     */

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }
    }
}
