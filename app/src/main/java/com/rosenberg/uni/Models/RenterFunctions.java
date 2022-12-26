package com.rosenberg.uni.Models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Renter.RenterCarViewDetailsFragment;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.Renter.RenterMyAcceptedCarsFragment;
import com.rosenberg.uni.Renter.RenterMyCarDetailsViewFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RenterFunctions {

    private final FirebaseFirestore _fs;
    private List<Car> cars;

    public RenterFunctions() {
        _fs = FirebaseFirestore.getInstance();
    }

    /**
     * would like to show the details of the car that the renter clicked on
     * so get the car data from fs
     * @param carDocId document string unique
     * @param renterFragment obj
     */
    public void getSpecificCar(String carDocId, RenterMyCarDetailsViewFragment renterFragment){
        _fs.collection("cars")
                .document(carDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car car = queryDocumentSnapshots.toObject(Car.class);

                    renterFragment.show(car);
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterMyCarDetailsView","problem loading car info" + carDocId);
                });

    }

    /**
     * would like to show the details of car
     * so get the car data from fs
     * @param carDocId document string unique
     * @param renterFragment obj
     */
    public void getSpecificCar(String carDocId, RenterCarViewDetailsFragment renterFragment){
        _fs.collection("cars")
                .document(carDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car car = queryDocumentSnapshots.toObject(Car.class);

                    renterFragment.show(car);
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problem loading car info" + carDocId);
                });

    }

    /**
     * put request for car_id from uid at fs
     * @param car_id UNIQUE car id
     * @param uid UNIQUE user id
     */
    public void sendRequest(String car_id, String uid) {
        _fs.collection("users").whereEqualTo("id",
                        uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots2 -> {
                    List<User> userList = queryDocumentSnapshots2.toObjects(User.class);
                    Log.d("USER UTILS", "Register user " + userList.size());
                    User u = userList.get(0);
                    // add request to database
                    _fs.collection("cars")
                            .document(car_id)
                            .collection("request")
                            .add(u);
            }).addOnFailureListener(fail -> {
                    Log.d("RenterCarViewDetails", "problem pushing request info of car: " + car_id);
                });;
    }

    /**
     * car already requested - cancel request
     * @param car_id UNIQUE car id
     * @param uid UNIQUE user id
     * @param renterFragment obj
     */
    public void cancelRequests(String car_id, String uid, RenterCarViewDetailsFragment renterFragment) {
        _fs.collection("cars").document(car_id)
                .collection("request")
                .whereEqualTo("id", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots1)
                        queryDocumentSnapshot.getReference().delete();
                }).addOnFailureListener(fail -> {
                    Log.d("RenterCarViewDetails", "problem loading car info" + car_id);
                });;
    }

    /**
     * checkAllRequests of specific user for a car
     * (we dont want to "spam" the system with alot of requests)
     * @param car_id UNIQUE car id
     * @param uid UNIQUE user id
     * @param renterFragment obj
     */
    public void checkAllRequests(String car_id, String uid, RenterCarViewDetailsFragment renterFragment) {
        _fs.collection("cars").document(car_id)
                .collection("request")
                .whereEqualTo("id", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                    renterFragment.takeRequestLength(queryDocumentSnapshots1.size());
                });
    }

    /**
     * when activates - extract only entries that stands with start~end dates
     *      than command to renterView to refresh its view
     * @param start DATE calender
     * @param end same
     * @param renterFragment as named
     */
    public void filterSearch(String start, String end, RenterCarViewFragment renterFragment){
        long [] spectrumTime = extractSpectrum(start, end); // idx: 0-START milis
                                                            //      1-END milis

        _fs.collection("cars")
                .whereLessThan("endDateStamp",spectrumTime[1])
                .whereGreaterThan("endDateStamp",spectrumTime[0])
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());

                    // we are doing this because we cant mnake compund queries
                    for (int i = 0; i < cars.size(); i++) {
                        if(cars.get(i).getRenterID() != null){
                            cars.remove(i--);
                        }
                    }

                    renterFragment.refreshCars(cars);
                });
    }

    /**
     * get from FS all the cars of user and let fragment to show them
     * @param uid - user id
     * @param renterFragment - who shall show cars
     */
    public void initMyCars(String uid, RenterMyAcceptedCarsFragment renterFragment) {
        _fs.collection("cars").whereEqualTo("renterID", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> { // get cars with U_ID as RenterID
                    List<Car> cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("RENTER ACCEPTED CARS","SHOWING MY CARS" + cars.size());

                    renterFragment.show(cars);
                });
    }

    /**
     * get all cars from FS and drop them back to view for show
     * @param uid - filter by whom? : uid=null -> gimme all actives
     * @param renterFragment .
     */
    public void showAllCars(String uid, RenterCarViewFragment renterFragment) {

        _fs.collection("cars")
                .whereEqualTo("renterID", uid) // show only cars that are not reserved
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());
                    renterFragment.refreshCars(cars);
                });
    }

    /**
     * take strings presents filter DATE and return it as parsed LONG milis
     * @param start date
     * @param end date
     * @return stare-end in milis (for db)
     */
    private long[] extractSpectrum(String start, String end) {
        long start_date_stamp,end_date_stamp;
        if(start.isEmpty()){ // get start date for filtering
            Calendar calendar = new GregorianCalendar(0,1,1);
            start_date_stamp = calendar.getTimeInMillis();
        }else{ // parse start date for filtering
            String [] splitdate = start.split("/");
            Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                    Integer.parseInt(splitdate[1]),
                    Integer.parseInt(splitdate[0]));
            start_date_stamp = calendar.getTimeInMillis();
        }

        if(end.isEmpty()){ // get end date for filtering
            Calendar calendar = new GregorianCalendar(3000,1,1);
            end_date_stamp = calendar.getTimeInMillis();
        }else{ // parse end date for filtering
            String [] splitdate = end.split("/");
            Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                    Integer.parseInt(splitdate[1]),
                    Integer.parseInt(splitdate[0]));
            end_date_stamp = calendar.getTimeInMillis();
        }

        long[] ans = new long[2];
        ans[0] = start_date_stamp; ans[1] = end_date_stamp;
        return ans;
    }

}
