package com.andyg.loginiecaandy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import modelosdatos.Model;

public class MyRecyclerViewHolder extends RecyclerView.Adapter<MyRecyclerViewHolder.ViewHolder>
            implements View.OnClickListener{

    //estructura de datos para llenar los elementos gráficos

    private ArrayList<Model> modelList;
    private View.OnClickListener list1;//escuchador

    //crear un constructor para inicializar la lista
    //de modelos, con los datos que manda firebase y poder usarlos


    public MyRecyclerViewHolder(ArrayList<Model> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(
                viewGroup.getContext()).
                inflate(
                        R.layout.model_item_db,
                        viewGroup,
                        false);
        //LayoutInflater, llenar mostras, asociar con e elemnto grafico

        view.setOnClickListener(this);


        return new ViewHolder(view);
    }


    //enlazar los datos con el array
    //recorrido del parametro de datos que le estamos mandando
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Model model = modelList.get(i);
        viewHolder.lblId.setText(model.getId());

        viewHolder.lblGrupo.setText(model.getGroup());

        viewHolder.lblMateria.setText(model.getMateria());

        viewHolder.lblActividad.setText(model.getActivity());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
        //ya esta el conteo de los items
    }

    public void setOnClickListener (View.OnClickListener listener){
            this.list1 = listener;
    }

    @Override
    public void onClick(View view) {

        if (list1 != null)
        {
            list1.onClick(view);
        }

    }
    //construye la clase (asigana valores locales a partir de cuando se instancia una clase)
    //


   //crear una clase interna, sirve para hacer sub-instancias
   public class ViewHolder extends RecyclerView.ViewHolder
   {


       //aqui vamos a inicializar los componentes graficos del xml
       //que trae la lista de objetos que manda el usuario al acceder
       // a la lista de datos de firebase


       private TextView lblId, lblGrupo, lblMateria, lblActividad;

       public View view;//para poder manipular los elemntso graficos

       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           this.view = itemView;
           this.lblId = view.findViewById(R.id.lblIdModelItem);
           this.lblGrupo = view.findViewById(R.id.lblGrupoModelItem);
           this.lblMateria = view.findViewById(R.id.lblMateriaModelItem);
           this.lblActividad = view.findViewById(R.id.lblActivityModelItem);
       }


   }

}
