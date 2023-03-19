package com.example.chatgpt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.viewHolder> {

    Context context;

    public OnBoardingAdapter(Context context)
    {
        this.context = context;
    }

    class viewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.onboarding_itemview,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        if(position == 0)
        {
            holder.imageView.setImageResource(R.drawable.on_b0);
        }
        if(position == 1)
        {
            holder.imageView.setImageResource(R.drawable.on_b2);
        }
        if(position == 2)
        {
            holder.imageView.setImageResource(R.drawable.on_b4);
        }
        if(position == 3)
        {
            holder.imageView.setImageResource(R.drawable.on_b6);
        }

        if(position == 4)
        {
            holder.imageView.setImageResource(R.drawable.on_b7);
        }





    }



    @Override
    public int getItemCount() {
        return 5;
    }
}
