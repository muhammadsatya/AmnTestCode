package com.example.testcodeamn.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.testcodeamn.R;
import com.example.testcodeamn.objcet.Trip;

import java.text.DecimalFormat;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public abstract class TripRecyclers extends RecyclerView.Adapter<TripRecyclers.MyViewHolder> {
    List<Trip> item;
    Context context;
    public TripRecyclers(Context context, List<Trip> item){
        this.context = context;
        this.item = item;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R
                .layout.list_business_trip, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Trip itemTrip = item.get(position);
        Log.d("CheckRecycler",itemTrip.getDestination());

        holder.tvEmployee.setText(itemTrip.getEmployee());
        holder.tvVisitDate.setText(itemTrip.getVisit_date());
        holder.tvDestination.setText(itemTrip.getDestination());

        holder.cardTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem(itemTrip);
            }
        });
        holder.linCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem(itemTrip);
            }
        });
        holder.linCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem(itemTrip);
            }
        });

    }

    public abstract void onClickItem(Trip itemTrip);



    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployee;
        TextView tvVisitDate;
        TextView tvDestination;
        CardView cardTrip;
        LinearLayout linCard;
        LinearLayout linCard2;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvEmployee = itemView.findViewById(R.id.tv_employee_trip);
            tvVisitDate = itemView.findViewById(R.id.tv_date_trip);
            tvDestination = itemView.findViewById(R.id.tv_destination_trip);
            cardTrip = itemView.findViewById(R.id.card_trip);
            linCard = itemView.findViewById(R.id.lin_card);
            linCard2 = itemView.findViewById(R.id.lin_card2);
        }
    }
}
