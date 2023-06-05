package com.example.bankassist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;



    public class chatdisplayrec extends RecyclerView.Adapter<chatdisplayrec.chatDisplay> {


        @NonNull
        @Override
        public chatdisplayrec.chatDisplay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_categoryitemlayout, parent, false);
            chatDisplay categoryView = new chatDisplay(view);
            return categoryView;
        }

        @Override
        public void onBindViewHolder(@NonNull chatdisplayrec.chatDisplay holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 0;
        }

        public class chatDisplay extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvTitle, tvDescription, tvComplete, tvDate, tvPriority;

            public chatDisplay(@NonNull View itemView) {
                super(itemView);

            }


            @Override
            public void onClick(View v) {

            }
        }
    }





