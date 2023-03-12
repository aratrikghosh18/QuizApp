package com.memory.quizapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class QuizRecyclerViewAdapter extends RecyclerView.Adapter<QuizRecyclerViewAdapter.ViewHolder> {

    LayoutInflater adapterLayoutInflater;
    Context adapterContext;
    ArrayList<String> adapterCategoryList;

    QuizRecyclerViewAdapter (Context context, ArrayList<String> categoryList){
        adapterLayoutInflater = LayoutInflater.from(context);
        adapterContext = context;
        adapterCategoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = adapterLayoutInflater.inflate(R.layout.category_list_recyclerview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categorytextview.setText(adapterCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return adapterCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView categorytextview;
        CardView categorycardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categorycardview = itemView.findViewById(R.id.categorycardview);
            categorytextview = itemView.findViewById(R.id.categorytextview);
            categorytextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(adapterContext,QuizActivity.class);
                    intent.putExtra("selectedcategory",categorytextview.getText().toString());
                    adapterContext.startActivity(intent);
                }
            });
        }
    }
}
