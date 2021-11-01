package com.autotechsolutions.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Model.NotificationModel;
import com.autotechsolutions.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.Holder> {


    private  Context context;
    private List<NotificationModel> notificationModels;

    public AdapterNotification(Context context, List<NotificationModel> modelList)
    {
        this.context = context;
        this.notificationModels = modelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_notification, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        NotificationModel model = notificationModels.get(i);

        holder.txt_title.setText(""+model.getNotificationTitle());
        holder.txt_description.setText(""+model.getNotificationText());
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_title)
        CustomTextView txt_title;
        @BindView(R.id.txt_description)
        CustomTextView txt_description;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
