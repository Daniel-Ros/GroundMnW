package com.rosenberg.uni.Models;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.History;
import com.rosenberg.uni.Entities.Review;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Tenant.TenantAddCarFragment;
import com.rosenberg.uni.Tenant.TenantCarViewDetailsFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.Tenant.TenantEditCarFragment;
import com.rosenberg.uni.Tenant.TenantViewRequestedRenterFragment;
import com.rosenberg.uni.utils.DataPart;
import com.rosenberg.uni.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenantFunctions {

    private Car car;
    private final FirebaseFirestore _fs;
    private List<Car> cars;
    List<User> users;

    public TenantFunctions() {_fs = FirebaseFirestore.getInstance();    }


    /**
     * would like to show the details of the car that the tenant clicked on
     * so get the car data from fs
     * @param carDocId document string unique
     * @param tenantFragment obj
     */
    public void getSpecificCar(String carDocId, TenantEditCarFragment tenantFragment) {
        _fs.collection("cars")
                .document(carDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    car = queryDocumentSnapshots.toObject(Car.class);
                    tenantFragment.show(car);
                }).addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problme loading car info" + carDocId);
                });;
    }

    /**
     * would like to show the details of the car that the tenant clicked on
     * so get the car data from fs
     * @param carDocId document string unique
     * @param tenantFragment obj
     */
    public void getSpecificCar(String carDocId, TenantCarViewDetailsFragment tenantFragment) {
        _fs.collection("cars")
                .document(carDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    car = queryDocumentSnapshots.toObject(Car.class);
                    tenantFragment.show(car);
                }).addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problme loading car info" + carDocId);
                });
    }

    /**
     * as named
     * @param carDocId document string unique
     * @param tenantFragment obj     * @param new_car
     * @param new_car obj to upload
     */
    public void uploadMyCarData(String carDocId, Car new_car, TenantEditCarFragment tenantFragment) {
        _fs.collection("cars").document(carDocId).set(new_car).addOnCompleteListener(task -> {
            tenantFragment.uploadSucceed();
        }).addOnFailureListener( fail -> {
            Log.d("RenterCarViewDetails","problme loading car info" + carDocId);
        });;
    }

    /**
     * delete the car from fs
     * @param carDocId UNIQUE id
     * @param tenantFragment obj
     */
    public void deleteCar(String carDocId, TenantCarViewDetailsFragment tenantFragment) {
        _fs.collection("cars").document(carDocId).delete().addOnSuccessListener(anything -> {
            tenantFragment.carRemoveSucceed();
        });
    }

    /**
     * as named, to screen it
     * @param carDocId UNIQUE car id
     * @param tenantFragment obj
     */
    public void getAllRequests(String carDocId, TenantCarViewDetailsFragment tenantFragment) {
        _fs.collection("cars")
                .document(carDocId)
                .collection("request")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    users = queryDocumentSnapshots.toObjects(User.class);
                    tenantFragment.uploadUsers(users);
                });
    }

    /**
     * push car to data base (adding)
     * @param car obj to push with all his details
     * @param bitmap
     * @param tenantFragment obj
     */
    public void pushCar(Car car, Bitmap bitmap, TenantAddCarFragment tenantFragment) {
        if(bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] bytes = byteArrayOutputStream.toByteArray();
            String base64Image;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                base64Image = Base64.getEncoder().encodeToString(bytes);
            }else{
                return;
            }

            RequestQueue queue = Volley.newRequestQueue(tenantFragment.getActivity().getApplicationContext());
            String url = "http://10.0.2.2:3000/add";

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                JSONObject obj = new JSONObject(new String(response.data));
                                String carid = obj.getString("message");
                                car.setPicid(carid);
                                _fs.collection("cars").add(car).addOnSuccessListener(anything -> {
                                    tenantFragment.carPushSucceed();
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("GotError",""+error.getMessage());
                        }
                    }){
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("image", new DataPart(imagename + ".png", bytes));
                    return params;
                }
            };
            queue.add(volleyMultipartRequest);
        }
        else{
            _fs.collection("cars").add(car).addOnSuccessListener(anything -> {
                tenantFragment.carPushSucceed();
            });
        }
    }

    /**
     * getSpecificUser from fs via uid
     * @param userDocId UNIQUE uid
     * @param tenantFragment obj
     */
    public void getSpecificUser(String userDocId, TenantViewRequestedRenterFragment tenantFragment) {
        _fs.collection("users").whereEqualTo("id", userDocId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> userList = queryDocumentSnapshots.toObjects(User.class); // returns list of items
                    User user = userList.get(0);
                    if (userList.size() == 0) {
                        Log.e("TenantViewRenter",
                                "Where is my user? its connected to app but cant see its own details from db " + userDocId);
                    }
                    tenantFragment.show(user);
            });
    }

    /**
     * update on fs that car id rented by user id
     * @param carDocId UNIQUE car id
     * @param userDocId UNIQUE user id
     * @param tenantFragment obj
     */
    public void acceptRenter(String carDocId, String userDocId, TenantViewRequestedRenterFragment tenantFragment) {
        _fs.collection("cars")
                .document(carDocId).update("renterID", userDocId).addOnSuccessListener(anything -> {
                    tenantFragment.acceptRenterSucceed();
                });
    }

    /**
     * update on fs that car id wont rent by user id
     * @param carDocId UNIQUE car id
     * @param userDocId UNIQUE user id
     * @param tenantFragment  obj
     */
    public void rejectRenter(String carDocId, String userDocId, TenantViewRequestedRenterFragment tenantFragment) {
        _fs.collection("cars")
                .document(carDocId).update("renterID", null).addOnSuccessListener(anything -> {
                    tenantFragment.rejectRentedSucceed();
                });
    }

    /**
     * as named, for screening the data
     * @param uid UNIQUE user id
     * @param tenantFragment obj
     */
    public void getAllCarsOfOwner(String uid, TenantCarViewFragment tenantFragment) {
        _fs.collection("cars").whereEqualTo("ownerID", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Car> cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("CAR_VIEW", "ADDING CARS" + cars.size());
                    tenantFragment.show(cars);
                });
    }

    public void rateUser(String userId, String comment, float rating) {
        _fs.collection("users").whereEqualTo("id", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                DocumentReference dr = queryDocumentSnapshot.getReference();

                User u = queryDocumentSnapshot.toObject(User.class);
                List<Review> reviews;
                if (u.getReviews() == null) {
                    reviews = new ArrayList<>();
                } else {
                    reviews = u.getReviews();
                }

                reviews.add(new Review(comment, rating));
                dr.update("reviews", reviews);
            }
        });
    }

    public void setHistReviewed(String carDocId, String renterID) {
        DocumentReference cr = _fs.collection("cars")
                .document(carDocId);
        cr.get().addOnSuccessListener(documentSnapshot -> {
            Car c = documentSnapshot.toObject(Car.class);
            List<History> histories = c.getPreviousRentersID();
            for (History h :
                    histories) {
                h.setReviewed(true);
            }

            cr.update("previousRentersID",histories);
        });
    }
}
