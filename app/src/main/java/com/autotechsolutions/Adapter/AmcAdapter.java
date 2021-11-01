package com.autotechsolutions.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AmcAdapter extends RecyclerView.Adapter<AmcAdapter.Holder> {


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
