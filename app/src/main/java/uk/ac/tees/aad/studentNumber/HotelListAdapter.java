package uk.ac.tees.aad.studentNumber;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {

    public static List<Hotel> mHotels;
    private OnItemClickListener mListener;

    public HotelListAdapter(List<Hotel> hotels) {
        mHotels = hotels;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public TextView mAddressTextView;
        public ImageView mImageView;
        public TextView mRatingTextView;
        public TextView mPriceTextView;
        private Context mContext;


        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mContext = itemView.getContext();
            mNameTextView = itemView.findViewById(R.id.hotel_name);
            mAddressTextView = itemView.findViewById(R.id.hotel_address);
            mImageView = itemView.findViewById(R.id.hotel_image);
            mRatingTextView=itemView.findViewById(R.id.hotel_rating);
            mPriceTextView=itemView.findViewById(R.id.hotel_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        int position = getAdapterPosition();
                            Intent intent = new Intent(mContext, HotelPage.class);
                            intent.putExtra("hotel",mHotels.get(position));
                            mContext.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel currentItem = mHotels.get(position);

        holder.mNameTextView.setText(currentItem.getHotelName());
        holder.mAddressTextView.setText(currentItem.getLocation());
        holder.mRatingTextView.setText(String.valueOf(currentItem.getRating()));
        holder.mPriceTextView.setText(currentItem.getPrice()+"$");

//        holder.mImageView.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return mHotels.size();
    }



}
