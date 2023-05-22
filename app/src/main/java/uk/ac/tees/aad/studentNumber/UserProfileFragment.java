package uk.ac.tees.aad.studentNumber;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UserProfileFragment extends Fragment {
    private TextView userNameTextView;
    private TextView emailTextView;
    private ImageView walletIcon;
    private TextView currentBalanceTextView;
    private RecyclerView bookingHistoryRecyclerView;
//    private List<Booking> bookingList;
    private double currentBalance = 0;
//    private FirestoreRecyclerAdapter<Booking, BookingViewHolder> bookingAdapter;

    RecyclerView noteLists;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirestoreRecyclerAdapter<Booking, NoteViewHolder> noteAdapter;
    Query query;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        initViews(view);
        setData(user);
        setRecyclerView();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Initialize Firebase database reference
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Set click listener for "Add Amount" button
        walletIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a dialog box to ask for the amount to be added
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Amount");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int amountToAdd = Integer.parseInt(input.getText().toString());
//                        Toast.makeText(getContext(), " "+currentBalance,Toast.LENGTH_LONG).show();
                        Map<String, Object> map=new HashMap<>();
                        map.put("Balance",amountToAdd+currentBalance);
                        userRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                currentBalanceTextView.setText(getString(R.string.current_balance_label,
//                                        String.valueOf(amountToAdd+currentBalance)));
                                currentBalance+=amountToAdd;
                                Toast.makeText(getContext(), "Amount added successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return view;
    }
    private void initViews(View view) {
        userNameTextView = view.findViewById(R.id.user_name_textview);
        emailTextView = view.findViewById(R.id.email_textview);
        walletIcon = view.findViewById(R.id.wallet_icon);
        currentBalanceTextView = view.findViewById(R.id.current_balance_textview);
        bookingHistoryRecyclerView = view.findViewById(R.id.booking_history_recyclerview);
    }



    private void setData(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(user!=null){
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userNameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        if (snapshot.getDouble("Balance") != null) {
                            currentBalance = snapshot.getDouble("Balance");
                        }
                        currentBalanceTextView.setText(getString(R.string.current_balance_label,String.valueOf(currentBalance)));
                    }
                }
            });
        }
    }


//    private void setRecyclerView() {
//        // set up the booking history recycler view
//        List<Booking> bookingList = new ArrayList<>();
//        bookingList.add(new Booking("Hotel A", 2, 150.0, "2023-03-15", "2023-03-18"));
//        bookingList.add(new Booking("Hotel B", 1, 100.0, "2023-03-20", "2023-03-22"));
//        bookingList.add(new Booking("Hotel C", 3, 200.0, "2023-03-25", "2023-03-28"));
//        bookingList.add(new Booking("Hotel A", 2, 150.0, "2023-03-15", "2023-03-18"));
//        bookingList.add(new Booking("Hotel B", 1, 100.0, "2023-03-20", "2023-03-22"));
//        bookingList.add(new Booking("Hotel C", 3, 200.0, "2023-03-25", "2023-03-28"));
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        BookingAdapter bookingAdapter = new BookingAdapter(bookingList);
//        bookingHistoryRecyclerView.setLayoutManager(layoutManager);
//        bookingHistoryRecyclerView.setAdapter(bookingAdapter);
//    }

    private void setRecyclerView() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            CollectionReference bookingRef = db.collection("users").document(user.getUid()).collection("bookings");
//            Query query = bookingRef.orderBy("checkinDate", Query.Direction.DESCENDING);
//            FirestoreRecyclerOptions<Booking> options = new FirestoreRecyclerOptions.Builder<Booking>()
//                    .setQuery(query, Booking.class)
//                    .build();
//            bookingAdapter = new FirestoreRecyclerAdapter<Booking, BookingViewHolder>(options) {
//                @Override
//                protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking model) {
//                    holder.bind(model);
//                }
//
//                @NonNull
//                @Override
//                public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
//                    return new BookingViewHolder(view);
//                }
//            };
//            bookingHistoryRecyclerView.setAdapter(bookingAdapter);
//        }

        fStore= FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();

        // Query the Firestore database for the user's notes, ordered by title in descending order
        query= fStore.collection("booking").document(user.getUid()).collection("myBookings");
        // Create a FirestoreRecyclerOptions object to display the notes in the RecyclerView
        FirestoreRecyclerOptions<Booking> allNotes=new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query,Booking.class)
                .build();
//        noteAdapter=c;
        // Set the layout manager for the RecyclerView to a StaggeredGridLayoutManager with 2 columns
//        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        // Set the adapter for the RecyclerView to the FirestoreRecyclerAdapter
//        noteLists.setAdapter(noteAdapter);
//        Toast.makeText(getContext(),allNotes.,Toast.LENGTH_LONG).show();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        BookingAdapter bookingAdapter = new BookingAdapter(bookingList);
        bookingHistoryRecyclerView.setLayoutManager(layoutManager);
        bookingHistoryRecyclerView.setAdapter(create_adapter(allNotes));
//        AIzaSyAMe2ntaLKlYtoc_2Ie4A8Yu_nOQsQkKVY
    }

    private FirestoreRecyclerAdapter<Booking, NoteViewHolder> create_adapter(FirestoreRecyclerOptions<Booking> allNotes){
        // Create a new FirestoreRecyclerAdapter to display the notes in the RecyclerView
        noteAdapter=new FirestoreRecyclerAdapter<Booking, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull final Booking booking) {
//                Toast.makeText(getContext(),booking.getHotelName(),Toast.LENGTH_LONG).show();
                holder.hotelNameTextView.setText(booking.getHotelName());
                holder.guestsAndPriceTextView.setText(holder.itemView.getContext().getString(R.string.booking_guests_and_price,
                        String.valueOf(booking.getGuests()), String.valueOf(booking.getPrice())));
                holder.checkInOutTextView.setText(holder.itemView.getContext().getString(R.string.booking_check_in_out,
                        booking.getCheckInDate(), booking.getCheckOutDate()));
            }


            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the note_view_layout layout for each note in the RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
                // Return a new NoteViewHolder instance with the inflated view
                return new NoteViewHolder(view);
            }
        };
        return noteAdapter;
    }



}
