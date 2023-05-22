package uk.ac.tees.aad.studentNumber;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<Booking> bookings;

    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.hotelNameTextView.setText(booking.getHotelName());
        holder.guestsAndPriceTextView.setText(holder.itemView.getContext().getString(R.string.booking_guests_and_price,
                String.valueOf(booking.getGuests()), String.valueOf(booking.getPrice())));
        holder.checkInOutTextView.setText(holder.itemView.getContext().getString(R.string.booking_check_in_out,
                booking.getCheckInDate(), booking.getCheckOutDate()));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hotelNameTextView;
        TextView guestsAndPriceTextView;
        TextView checkInOutTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.hotel_name_text_view);
            guestsAndPriceTextView = itemView.findViewById(R.id.guests_price_text_view);
            checkInOutTextView = itemView.findViewById(R.id.check_in_out_text_view);
        }
    }
}
