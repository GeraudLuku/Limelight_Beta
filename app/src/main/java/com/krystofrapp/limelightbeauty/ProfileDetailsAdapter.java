package com.krystofrapp.limelightbeauty;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProfileDetailsAdapter extends FirestoreRecyclerAdapter<ProfileDetails, ProfileDetailsAdapter.ProfileDetailsHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProfileDetailsAdapter(@NonNull FirestoreRecyclerOptions<ProfileDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProfileDetailsHolder profileDetailsHolder, int i, @NonNull ProfileDetails profileDetails) {
        profileDetailsHolder.tvBusinessName.setText(profileDetails.getBusinessName());
        profileDetailsHolder.tvBusinessPhone.setText(profileDetails.getBusinessNumber());
        profileDetailsHolder.tvBusinessAddress.setText(profileDetails.getBusinessAddress());
        profileDetailsHolder.tvBusinessNickname.setText(profileDetails.getBusinessNickname());
    }

    @NonNull
    @Override
    public ProfileDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_detail_items, parent, false);
        return new ProfileDetailsHolder(view);
    }

    public static class ProfileDetailsHolder extends RecyclerView.ViewHolder {
        private TextView tvBusinessName, tvBusinessPhone, tvBusinessAddress, tvBusinessNickname;


        public ProfileDetailsHolder(@NonNull View itemView) {
            super(itemView);

            tvBusinessName = itemView.findViewById(R.id.profile_bsn);
            tvBusinessPhone = itemView.findViewById(R.id.profile_number);
            tvBusinessAddress = itemView.findViewById(R.id.profile_address);
            tvBusinessNickname = itemView.findViewById(R.id.profile_nickname);
        }
    }
}
