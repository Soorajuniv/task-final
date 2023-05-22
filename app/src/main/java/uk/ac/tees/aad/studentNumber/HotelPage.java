package uk.ac.tees.aad.studentNumber;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HotelPage extends AppCompatActivity {
    Intent intent;
    Hotel hotel;
    private TextView checkInDate, checkOutDate;
    private TextView name,location, description, price, rating;
    Button bookNow;
    private double currentBalance = 0;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_page);

        intent = getIntent();
        hotel = (Hotel) intent.getSerializableExtra("hotel");

        // Set the hotel description text view
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        checkInDate = findViewById(R.id.check_in_date_edittext);
        checkOutDate = findViewById(R.id.check_out_date_edittext);
        price = findViewById(R.id.price);
        rating = findViewById(R.id.rating);
        bookNow=findViewById(R.id.bookNow);

        name.setText(hotel.getHotelName());
        location.setText(hotel.getLocation());
        description.setText(hotel.getDesc().trim());
        rating.setText(hotel.getRating());
        price.setText("Price: $" + hotel.getPrice());

        checkInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view,checkInDate);
            }
        });

        checkOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view,checkOutDate);
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        setData(user);
        fStore = FirebaseFirestore.getInstance();

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });

    }

    public void showDatePickerDialog(View v, TextView textView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Do something with the chosen date
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                textView.setText(date);
            }
        }, 2023, 3, 11); // Set the initial date (year, month, day) to show in the dialog

        datePickerDialog.show();
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to book the room?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes button
                        // Call your booking method here
                        bookHotel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No button
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void bookHotel(){
        String checkInDateString = checkInDate.getText().toString();
        String checkOutDateString = checkOutDate.getText().toString();

        // Validate that both dates are not empty
        if (TextUtils.isEmpty(checkInDateString)) {
            Toast.makeText(HotelPage.this, "Please select a check-in date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(checkOutDateString)) {
            Toast.makeText(HotelPage.this, "Please select a check-out date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date checkInDateFormat = dateFormat.parse(checkInDateString);
            Date checkOutDateFormat = dateFormat.parse(checkOutDateString);

            // Validate that check-out date is after check-in date
            if (checkOutDateFormat.before(checkInDateFormat)) {
                Toast.makeText(HotelPage.this,
                        "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate the difference in days between check-in and check-out
            long diffInMs = checkOutDateFormat.getTime() - checkInDateFormat.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)+1;

            // Get the number of rooms
            EditText numOfRoomsEditText = findViewById(R.id.num_of_rooms);
            String numOfRoomsString = numOfRoomsEditText.getText().toString();

            // Validate that number of rooms is not empty
            if (TextUtils.isEmpty(numOfRoomsString)) {
                numOfRoomsEditText.setError("Please enter the number of rooms");
                return;
            }
            int numOfRooms = Integer.parseInt(numOfRoomsString);
            double totalPrice = numOfRooms * diffInDays * hotel.getPrice(); // assuming price is 100 per day
            if(totalPrice>currentBalance){
                Toast.makeText(getApplicationContext(), "Insufficient balance", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, Object> booking = new HashMap<>();
            booking.put("hotelName", hotel.getHotelName());
            booking.put("number_of_rooms", numOfRooms);
            booking.put("price",totalPrice);
            booking.put("checkInDate",checkInDateString);
            booking.put("checkOutDate",checkOutDateString);
            add_booking(booking,totalPrice);
        } catch (Exception e) {
            Toast.makeText(HotelPage.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void setData(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(user!=null){
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        if (snapshot.getDouble("Balance") != null) {
                            currentBalance = snapshot.getDouble("Balance");
                        }
                    }
                }
            });
        }
    }

    public void updateBalance(double amountToPay){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Map<String, Object> map=new HashMap<>();
        map.put("Balance",currentBalance-amountToPay);
        userRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                currentBalance-=amountToPay;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void add_booking(Map<String, Object> booking, double amount){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference doc_ref = fStore.collection("booking").document(user.getUid()).collection("myBookings").document();
        doc_ref.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HotelPage.this, "Booked Successfully!", Toast.LENGTH_LONG).show();
                updateBalance(amount);
                startActivity(new Intent(HotelPage.this, MainActivity.class));
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Booking: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}