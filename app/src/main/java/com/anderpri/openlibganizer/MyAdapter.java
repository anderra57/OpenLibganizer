package com.anderpri.openlibganizer;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<String> titles;
    private List<String> images;

    public MyAdapter(Context context, List<String> titles, List<String> images){
        this.context=context;
        this.images=images;
        this.titles=titles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView.setText(titles.get(position));
        if (!images.get(position).equals("N/A")){
            // Fuente: https://devexperto.com/glide-android/
            Uri uri = Uri.parse(images.get(position));
            Glide.with(context).load(uri).into(holder.mImageView);
        } else {
            holder.mImageView.setImageResource(R.drawable.cover_not_available);
        }

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageview);
            mTextView = itemView.findViewById(R.id.textview);

            itemView.setOnClickListener(view -> {
                //Toast.makeText(view.getContext(),"Clicked on: "+ getAdapterPosition(),Toast.LENGTH_SHORT).show();
            });
        }
    }
}
