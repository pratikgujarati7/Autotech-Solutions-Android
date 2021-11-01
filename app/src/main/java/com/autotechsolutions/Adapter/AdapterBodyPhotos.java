package com.autotechsolutions.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autotechsolutions.R;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterBodyPhotos extends RecyclerView.Adapter<AdapterBodyPhotos.Holder> {


    private Context context;
    private ArrayList<AlbumFile> stringListUrl;
    LayoutInflater layoutInflater;
    Bitmap bitmap;

    public AdapterBodyPhotos(Context mContext, ArrayList<AlbumFile> urlList) {
        this.context = mContext;
        this.stringListUrl = urlList;
//      autoTechApp.setImages_url(urlList);
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void notifyDataSetChanged(ArrayList<AlbumFile> imagePathList) {
        this.stringListUrl = imagePathList;
        super.notifyDataSetChanged();
    }
//    public AdapterBodyPhotos(Context mContext, ArrayList<AlbumFile> urlList) {
//        this.context = mContext;
//        this.mAlbumFiles = urlList;
////      autoTechApp.setImages_url(urlList);
//        layoutInflater = LayoutInflater.from(mContext);
//    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.row_bodyshopphotos, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

//        Bitmap bitmap = BitmapFactory.decodeFile(stringListUrl.get(i).toString());
        Bitmap bitmap = BitmapFactory.decodeFile(stringListUrl.get(i).getThumbPath());
        holder.img_carPhoto.setImageBitmap(bitmap);

        holder.img_RemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return stringListUrl == null ? 0 : stringListUrl.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_carPhoto)
        ImageView img_carPhoto;

        @BindView(R.id.img_RemovePhoto)
        ImageView img_RemovePhoto;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeAt(int position) {
        stringListUrl.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, stringListUrl.size());
    }
}
