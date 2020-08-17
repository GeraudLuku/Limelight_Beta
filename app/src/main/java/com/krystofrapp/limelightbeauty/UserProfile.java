package com.krystofrapp.limelightbeauty;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    // private TextView busName, busPhone, busAddress, busNickname;
    private FirebaseFirestore cloudDb = FirebaseFirestore.getInstance();
    private CollectionReference profileDetailsRef = cloudDb.collection("Business Accounts");
    private ProfileDetailsAdapter adapter;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(view -> {
            //Open gallery
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        //busName = findViewById(R.id.profile_bsn);
        //busPhone = findViewById(R.id.profile_number);
        //busAddress = findViewById(R.id.profile_address);
        //busNickname = findViewById(R.id.profile_nickname);
        setUpRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void setUpRecyclerView() {
        Query query = profileDetailsRef.orderBy("businessName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProfileDetails> options = new FirestoreRecyclerOptions.Builder<ProfileDetails>()
                .setQuery(query, ProfileDetails.class)
                .build();

        adapter = new ProfileDetailsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
