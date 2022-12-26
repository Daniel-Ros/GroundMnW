package com.rosenberg.uni.Models;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Tenant.TenantAddCarFragment;
import com.rosenberg.uni.Tenant.TenantCarViewDetailsFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.Tenant.TenantEditCarFragment;
import com.rosenberg.uni.Tenant.TenantViewRequestedRenterFragment;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

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
     * @param tenantFragment obj
     */
    public void pushCar(Car car, TenantAddCarFragment tenantFragment) {
        _fs.collection("cars").add(car).addOnSuccessListener(anything -> {
            tenantFragment.carPushSucceed();
        });
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
}
