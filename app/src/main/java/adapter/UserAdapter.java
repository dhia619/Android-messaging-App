package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.messenger.MessagingActivity;
import com.example.messenger.R;
import com.example.messenger.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;

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
        if (user.getProfile_image().equals("")){
            holder.profile_img.setImageResource(R.mipmap.ic_default_avatar_round);
        } else {
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
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
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

}
