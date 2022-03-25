package com.anderpri.openlibganizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final Context context;
    private final Books books;
    private final OnBookListener onBookListener;

    public MyAdapter(Context context, Books books,OnBookListener onBookListener ){
        this.context=context;
        this.books=books;
        this.onBookListener=onBookListener;
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
        if (!books.get(position).getmThumbnail().equals(context.getString(R.string.default_info))){
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

        final OnBookListener onBookListener;
        final ImageView mImageView;
        final TextView mTextView;

        public MyViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageview);
            mTextView = itemView.findViewById(R.id.textview);
            this.onBookListener=onBookListener;

            itemView.setOnClickListener(this);
            //Toast.makeText(view.getContext(),"Clicked on: "+ getAdapterPosition(),Toast.LENGTH_SHORT).show();

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
