package com.rosenberg.uni.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditViewProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditViewProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditViewProfileFragment newInstance(String param1, String param2) {
        EditViewProfileFragment fragment = new EditViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        return inflater.inflate(R.layout.fragment_edit_view_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button confirmBtn = view.findViewById(R.id.confirm_button);

        EditText firstName = view.findViewById(R.id.edit_first_name);
        EditText lastName = view.findViewById(R.id.edit_last_name);
        EditText born = view.findViewById(R.id.edit_birth);
        EditText city = view.findViewById(R.id.edit_city);
        EditText phoneNumber = view.findViewById((R.id.edit_phone_num));
        Spinner spinnerGender = view.findViewById(R.id.edit_gender);
        EditText detailsOnUser = view.findViewById(R.id.edit_on_me);


        // Init the spinner of gender
        String [] choisesGenders = new String[]{"Male","Female"};
        ArrayAdapter<String> adapterGenders = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,choisesGenders);
        spinnerGender.setAdapter(adapterGenders);

        String uid = userUtils.getUserID();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            if (userList.size() == 0){
                Log.e("EditProfile","Where is my user? its connected to app but cant see its own details from db " + uid);
            }
            User user = userList.get(0);
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            phoneNumber.setText(user.getPhoneNum());
            spinnerGender.setEnabled(user.getGender());
            born.setText(user.getBorn());
            city.setText(user.getCity());
            detailsOnUser.setText(user.getWritingOnMe());
        });



        confirmBtn.setOnClickListener(v -> {

            fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                if (userList.size() == 0){
                    Log.e("EditProfile","Where is my user? its connected to app but cant see its own details from db " + uid);
                }
                User user = userList.get(0);
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                user.setPhoneNum(phoneNumber.getText().toString());
                user.setBorn(born.getText().toString());
                user.setCity(city.getText().toString());
                user.setWritingOnMe(detailsOnUser.getText().toString());
                fs.collection("users").document(user.getDocumentId()).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("EditViewProfile", "updated to fs the edit of user: "+user.getId());
                                FragmentManager fm = getParentFragmentManager();
                                fm.beginTransaction().replace(R.id.main_fragment, ViewProfileFragment.class, null).commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("EditViewProfile", "failed to update to fs the edit of user: "+user.getId());
                            }
                        });
            });

        });

    }
}