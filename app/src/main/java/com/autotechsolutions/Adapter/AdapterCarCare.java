package com.autotechsolutions.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.Placeholder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.InquireActivity;
import com.autotechsolutions.Model.CarCareModel;
import com.autotechsolutions.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterCarCare extends RecyclerView.Adapter<AdapterCarCare.Holder> {


    Context context;
    List<CarCareModel> careModels=null;
    String carID;

    public AdapterCarCare(Context context,List<CarCareModel> carCareModels,String carID){
        this.context = context;
        this.careModels = carCareModels;
        this.carID = carID;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_carcare, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {


        final CarCareModel model = careModels.get(i);
//        "http://goo.gl/gEgYUd"
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo_mtsbc)
                .error(R.drawable.logo_mtsbc)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(context).load(model.getImage()).apply(options).into(holder.img_categoryImage);

        holder.txt_title.setText(""+model.getName());

        holder.img_categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InquireActivity.class);
                intent.putExtra("image",model.getImage());
                intent.putExtra("name",model.getName());
                intent.putExtra("description",model.getDescription());
                intent.putExtra("productmodelID",model.getProductModelID());
                intent.putExtra("carID",carID);
                context.startActivity(intent);
//                context.startActivity(new Intent(context, InquireActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return careModels.size();
    }

    public class  Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_categoryImage)
        ImageView img_categoryImage;

        @BindView(R.id.txt_title)
        CustomTextView txt_title;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
