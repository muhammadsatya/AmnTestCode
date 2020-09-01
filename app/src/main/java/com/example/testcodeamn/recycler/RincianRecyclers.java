package com.example.testcodeamn.recycler;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testcodeamn.R;
import com.example.testcodeamn.objcet.Rincian;
import com.example.testcodeamn.objcet.Trip;

import java.text.DecimalFormat;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public abstract class RincianRecyclers extends RecyclerView.Adapter<RincianRecyclers.MyViewHolder> {
    List<Rincian> item;
    Context context;
    public RincianRecyclers(Context context, List<Rincian> item){
        this.context = context;
        this.item = item;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R
                .layout.list_rincian, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Rincian itemRincian = item.get(position);

        holder.tvRincian.setText(itemRincian.getPeruntukan());
        holder.tvRincianDate.setText(itemRincian.getDate_time());
        holder.imgRincian.setImageURI(Uri.parse(itemRincian.getImage()));

        String totalRincian="";
        try {
            double dprice = Double.parseDouble(itemRincian.getJumlah());
            DecimalFormat formatter2 = new DecimalFormat("#,###");
            String AB = String.valueOf(formatter2.format(dprice));
            if (AB.startsWith(".")) AB = "0" + AB;
            totalRincian = AB;
        }catch(Exception e){
            totalRincian = itemRincian.getJumlah();
        }
        holder.tvRincianCurrent.setText("Rp. "+totalRincian);

        if (itemRincian.getType() == 0){
            holder.imgDeleteItem.setVisibility(View.GONE);
        }else {
            holder.imgDeleteItem.setVisibility(View.VISIBLE);
        }

        holder.imgDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteItem(itemRincian, position);
            }
        });

    }

    public abstract void onDeleteItem(Rincian itemTrip, int position);



    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRincian;
        TextView tvRincianDate;
        TextView tvRincianCurrent;
        ImageView imgRincian;
        ImageView imgDeleteItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRincian = itemView.findViewById(R.id.tv_rincian);
            tvRincianDate = itemView.findViewById(R.id.tv_rincian_date);
            tvRincianCurrent = itemView.findViewById(R.id.tv_rincian_current);
            imgRincian = itemView.findViewById(R.id.img_list_rincian);
            imgDeleteItem = itemView.findViewById(R.id.img_delete_rincian);
        }
    }
}
