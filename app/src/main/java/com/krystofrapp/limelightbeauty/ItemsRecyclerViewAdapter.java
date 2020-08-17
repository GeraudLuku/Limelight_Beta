package com.krystofrapp.limelightbeauty;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.RecyclerViewHolder> {

    // private static final String TAG = "PreserveFoodTipsAdapter";
    private ArrayList<ModelTopOrdered> mList;

    private Context mContext;

    public ItemsRecyclerViewAdapter(Context context, ArrayList<ModelTopOrdered> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        //View view = layoutInflater.inflate(R.layout.list_item_layout, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, parent, false);
        //RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsRecyclerViewAdapter.RecyclerViewHolder holder, final int position) {

        ModelTopOrdered topOrdered = mList.get(position);
        CircleImageView imageView;
        TextView itemTitle;

        imageView = holder.image;
        itemTitle = holder.title;

        imageView.setImageResource(topOrdered.getImage());
        itemTitle.setText(topOrdered.getTitle());

        holder.image.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, Services.class);
            mContext.startActivity(intent);
        });
        holder.cardViewHolder.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, Services.class);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private CardView cardViewHolder;
        private TextView title;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewHolder = itemView.findViewById(R.id.card_items);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
        }
    }
}
