package com.andyg.loginiecaandy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import modelosdatos.Upload;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImagenViewHolder> {


    //declarar variables
    private Context mContext;
    private List<Upload> mUploads;

    //constructor para el adapter

    public ImageAdapter (Context context, List<Upload> uploads){
        this.mContext = context;
        this.mUploads = uploads;
    }

    @NonNull
    @Override
    public ImagenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImagenViewHolder (v);
    }


    //tomar control
    @Override
    public void onBindViewHolder(@NonNull ImagenViewHolder holder, int position) {

        Upload uploaCurrent = mUploads.get(position);
        holder.textViewName.setText(uploaCurrent.getName());

        // picasso sirve para traer las imagenes de la base de datos
        Picasso.with(mContext)
                .load(uploaCurrent.getImgUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);

    }

    //llamar el arreglo de las imagenes
    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    // colocar los elemntos de la base de datos en la parte visual
    public class ImagenViewHolder extends RecyclerView.ViewHolder{

        // instanciar variables
        public TextView textViewName;
        public ImageView imageView;

        //asignar variables al contenedor
        public ImagenViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }

}
