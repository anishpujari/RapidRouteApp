package com.example.rapidroute;


import java.util.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;


public class DeliveryAgentActivity extends AppCompatActivity {

//    private GoogleMap mMap;
//    private EditText startLocation, endLocation;
//    private Button getDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliveryagent_activity);

        EditText startLocationEditText = findViewById(R.id.startLocationEditText);
        EditText endLocationEditText = findViewById(R.id.endLocationEditText);
        Button getDirectionsButton = findViewById(R.id.getDirectionsButton);
        TextView orderIdTextView = findViewById(R.id.order_id);
        TextView weightTextView = findViewById(R.id.weight);
        TextView volumeTextView = findViewById(R.id.volume);
        TextView typeTextView = findViewById(R.id.type);
        TextView natureTextView = findViewById(R.id.nature);
        TextView addressTextView = findViewById(R.id.address);

        fetchParcelDetails("12345", orderIdTextView, weightTextView, volumeTextView, typeTextView,
                natureTextView, addressTextView, startLocationEditText, endLocationEditText);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }

        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLocation = startLocationEditText.getText().toString();
                String endLocation = endLocationEditText.getText().toString();

                if(startLocation.isEmpty()||endLocation.isEmpty()){
                    Toast.makeText(DeliveryAgentActivity.this, "Please enter both start and end locations", Toast.LENGTH_SHORT).show();
                    return;
                }
                String uri="https://www.google.com/maps/dir/?api=1&origin=" + Uri.encode(startLocation) +
                        "&destination=" + Uri.encode(endLocation) + "&travelmode=driving";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(DeliveryAgentActivity.this, "Google Maps app not found", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void fetchParcelDetails(String parcelId, TextView orderId, TextView weight, TextView volume,
                                    TextView type, TextView nature, TextView address,
                                    EditText startLocation, EditText endLocation) {

        String url = "http://172.16.58.71:3001/fetch"; // Replace with your backend URL

        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("id", parcelId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestParams,
                response -> {
                    try {
                        JSONObject parcel = response.getJSONArray("t").getJSONObject(0);

                        // Update UI Elements
                        orderId.setText(parcel.optString("id"));
                        weight.setText(parcel.optString("weight"));
                        volume.setText(parcel.optString("dimensions"));
                        type.setText(parcel.optString("type", "N/A"));
                        nature.setText(parcel.optString("nature"));
                        address.setText(parcel.optJSONObject("destination").toString());

                        // Autofill Locations
                        startLocation.setText(parcel.optJSONObject("source").toString());
                        endLocation.setText(parcel.optJSONObject("destination").toString());

                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing parcel details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch parcel details", Toast.LENGTH_SHORT).show()
        );

        // Add Request to Queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//    }

//    private void fetchDirections() {
//        String start = startLocation.getText().toString();
//        String end = endLocation.getText().toString();
//
//        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
//            // Handle error: Show a Toast or dialog
//            return;
//        }
//
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
//                + start + "&destination=" + end + "&key=AIzaSyCj7kSflMq3Qfk4p5kYQBDBipDHIl-k1OA";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray routes = jsonObject.getJSONArray("routes");
//                        if (routes.length() > 0) {
//                            JSONObject route = routes.getJSONObject(0);
//                            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
//                            String encodedPolyline = overviewPolyline.getString("points");
//                            drawPolyline(encodedPolyline);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                },
//                error -> error.printStackTrace());
//
//        queue.add(request);
//    }

//    private void drawPolyline(String encodedPolyline) {
//        List<LatLng> points = decodePolyline(encodedPolyline);
//        mMap.clear();
//        mMap.addPolyline(new PolylineOptions().addAll(points));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 10));
//    }

//    private List<LatLng> decodePolyline(String encoded) {
//        List<LatLng> polyline = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            polyline.add(new LatLng(lat / 1E5, lng / 1E5));
//        }
//        return polyline;
//    }
}
