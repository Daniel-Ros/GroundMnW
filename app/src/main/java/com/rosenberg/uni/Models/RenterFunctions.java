package com.rosenberg.uni.Models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.Renter.RenterMyAcceptedCarsFragment;
import com.rosenberg.uni.Renter.RenterMyCarDetailsViewFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RenterFunctions {

    private User user;
    private final FirebaseFirestore _fs;
    private final FirebaseAuth _fsAuth;
    private List<Car> cars;

    public RenterFunctions() {
        _fs = FirebaseFirestore.getInstance();
        _fsAuth = FirebaseAuth.getInstance();
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
