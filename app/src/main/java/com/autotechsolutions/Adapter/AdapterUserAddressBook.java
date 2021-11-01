package com.autotechsolutions.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autotechsolutions.Config;
import com.autotechsolutions.CustomView.CustomTextView;
import com.autotechsolutions.Model.UserAddressBookListModel;
import com.autotechsolutions.R;
import com.autotechsolutions.Response.Common;
import com.autotechsolutions.RetroApiClient;
import com.autotechsolutions.RetroApiInterface;
import com.autotechsolutions.fragment.FragmentAddressBook;
import com.autotechsolutions.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterUserAddressBook extends RecyclerView.Adapter<AdapterUserAddressBook.Holder> {


    private Context context;
    private List<UserAddressBookListModel> userAddressBookListModels;
    private FragmentAddressBook fab;

    public interface OnItemClickListener {
        void onItemClick(UserAddressBookListModel item);
    }

    private OnItemClickListener listener;

    public AdapterUserAddressBook(Context context, List<UserAddressBookListModel> modelList, OnItemClickListener listener) {
        this.context = context;
        this.userAddressBookListModels = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_address, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        holder.bind(userAddressBookListModels.get(i), listener);

//        final UserAddressBookListModel model = userAddressBookListModels.get(i);
//
//
    }

    @Override
    public int getItemCount() {
        return userAddressBookListModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title)
        CustomTextView txt_title;
        @BindView(R.id.txt_description)
        CustomTextView txt_description;
        @BindView(R.id.delete_address)
        ImageView delete_address;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final UserAddressBookListModel item, final OnItemClickListener listener) {

            txt_title.setText("" + item.getTitle());
            txt_description.setText("" + item.getAddress());
            delete_address.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
