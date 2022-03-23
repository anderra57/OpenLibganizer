package com.anderpri.openlibganizer;

import android.content.Context;
import android.content.Intent;
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
    //private List<String> titles;
    //private List<String> images;
    private List<Book> books;
    private OnBookListener onBookListener;

    public MyAdapter(Context context, List<Book> books,OnBookListener onBookListener ){ // List<String> titles, List<String> images){
        this.context=context;
        this.books=books;
        this.onBookListener=onBookListener;
        //this.images=images;
        //this.titles=titles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);
        return new MyViewHolder(v, onBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView.setText(books.get(position).getmTitle());
        if (!books.get(position).getmThumbnail().equals("N/A")){
            // Fuente: https://devexperto.com/glide-android/
            Uri uri = Uri.parse(books.get(position).getmThumbnail());
            Glide.with(context).load(uri).into(holder.mImageView);
        } else {
            holder.mImageView.setImageResource(R.drawable.cover_not_available);
        }

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnBookListener onBookListener;
        ImageView mImageView;
        TextView mTextView;

        public MyViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageview);
            mTextView = itemView.findViewById(R.id.textview);
            this.onBookListener=onBookListener;

            itemView.setOnClickListener(this);

            /*
            itemView.setOnClickListener(view -> {

                onBookListener.onBookClick(getAdapterPosition());



                //Toast.makeText(view.getContext(),"Clicked on: "+ getAdapterPosition(),Toast.LENGTH_SHORT).show();
            });*/
        }

        @Override
        public void onClick(View view) {
            onBookListener.onBookClick(getAdapterPosition());
        }
    }

    public interface OnBookListener{
        void onBookClick(int position);
    }
}
