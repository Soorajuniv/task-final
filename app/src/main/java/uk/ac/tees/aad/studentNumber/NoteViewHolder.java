package uk.ac.tees.aad.studentNumber;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView hotelNameTextView;
    TextView guestsAndPriceTextView;
    TextView checkInOutTextView;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        hotelNameTextView = itemView.findViewById(R.id.hotel_name_text_view);
        guestsAndPriceTextView = itemView.findViewById(R.id.guests_price_text_view);
        checkInOutTextView = itemView.findViewById(R.id.check_in_out_text_view);
    }


}
