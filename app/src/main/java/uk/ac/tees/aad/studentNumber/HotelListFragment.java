package uk.ac.tees.aad.studentNumber;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotelListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HotelListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotelListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotelListFragment newInstance(String param1, String param2) {
        HotelListFragment fragment = new HotelListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private HotelListAdapter mAdapter;
    private List<Hotel> mHotels;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_list, container, false);

        mRecyclerView = view.findViewById(R.id.hotel_list_recycler_view);

        // set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // create hotel list
        mHotels = new ArrayList<Hotel>();
//        mHotels.add(new Hotel("The Ritz-Carlton", "Los Angeles", "$$$", "4.7", R.drawable.hotel1));
//        mHotels.add(new Hotel("The Plaza", "New York City", "$$$", "4.5", R.drawable.hotel1));
//        mHotels.add(new Hotel("The Langham", "Chicago", "$$$", "4.3", R.drawable.hotel1));
//        mHotels.add(new Hotel("Wynn Las Vegas", "Las Vegas", "$$$", "4.6", R.drawable.hotel1));
//        mHotels.add(new Hotel("The Savoy", "London", "$$$", "4.5", R.drawable.hotel1));
//        mHotels.add(new Hotel("Burj Al Arab", "Dubai", "$$$$", "4.9", R.drawable.hotel1));
//        mHotels.add(new Hotel("The Peninsula", "Paris", "$$$$", "4.7", R.drawable.hotel1));
//        mHotels.add(new Hotel("Park Hyatt", "Sydney", "$$$$", "4.8", R.drawable.hotel1));
        getData();
        // create adapter and set it to recycler view
        mAdapter = new HotelListAdapter(mHotels);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void getData(){
        ArrayList<Hotel> hotelList = new ArrayList<>();
        String url = "https://api.jsonbin.io/v3/b/6405bc9bc0e7653a05833b0b/latest";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            JSONObject obj = object.getJSONObject("record");

                            Iterator<String> temp = obj.keys();
//                            System.out.println(json.toString());
                            while (temp.hasNext()) {
                                String key = temp.next();

                                JSONObject hotelObj  = ((JSONObject) obj.get(key));
//                                Toast.makeText(getApplicationContext()," "+value.toString(),Toast.LENGTH_LONG).show();
                                String hotelName = hotelObj.getString("Hotel Name");
                                String location = hotelObj.getString("Location");
                                String rating = hotelObj.getString("Rating");
                                int price = hotelObj.getInt("Price");
                                double longitude = hotelObj.getDouble("longitude");
                                double latitude = hotelObj.getDouble("latitude");
                                int id = hotelObj.getInt("Id");
                                String desc = hotelObj.getString("desc");

                                // create a new Hotel instance with the extracted details
                                Hotel hotel = new Hotel(hotelName, location, rating, price, longitude, latitude, id, desc);

                                // add the new Hotel instance to the list
                                hotelList.add(hotel);
                            }

                            mHotels.clear();
                            mHotels.addAll(hotelList);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                            Intent i=new Intent(MainActivity.this,demo.class);
//                            i.putExtra("error", "Error: "+e.getLocalizedMessage());
//                            startActivity(i);
                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error

                Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Master-key", "$2b$10$7LZjZd8jOiBTb.mv6sJGU.Zu/OlGk8FX7rHaOUAdOB/PA3UCBkwp6");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

}