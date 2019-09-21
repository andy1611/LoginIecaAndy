package com.andyg.loginiecaandy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import modelosdatos.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealTimeActivity extends AppCompatActivity implements View.OnClickListener {

    //declarar las variables
    TextView lblGrupo, lblMateria;
    Spinner spGrupo, spMateria;
    Button btnGuardar, btnEliminar, btnActualizar;
    EditText txtActividad;

    String itemAlmacenarId;
    String inicializarItemM;
    String inicializarItemG;
    RecyclerView recyclerView;

    //definir las variables de conexion SQL

    FirebaseDatabase firebaseDatabase;
    DatabaseReference modelClass;

    //crear arreglos
    String[] grupos = {"TI-701","AG-701","GE-701","IN-701","ME-701"};
    String[] materias = {"Calculo I","Calculo II","Ecuaciones Dif","Colorimetria","Comunicacion asertiva"};

    public ArrayList<Model> list = new ArrayList<>();
    public MyRecyclerViewHolder myRecyclerViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //a ese archivo se le va asiganr el xml que contiene los elementos visuales
        setContentView(R.layout.activity_real_time);
        //conexion
        firebaseDatabase = FirebaseDatabase.getInstance();
        modelClass = firebaseDatabase.getReference("Model");

        //inicializar variables
        spGrupo = findViewById(R.id.spGrupo);
        spMateria = findViewById(R.id.spMateria);
        lblGrupo = findViewById(R.id.lblGrupo);
        lblMateria = findViewById(R.id.lblMateria);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        txtActividad = findViewById(R.id.txtActividad);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //llenar spinners

        spGrupo.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, grupos));

        spMateria.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, materias));


        //clics

        btnGuardar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);

        getDataFirebase();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnGuardar:
                // tap en el boton guardar
                addNode();
                break;


            case R.id.btnEliminar:
                // tap en el boton guardar
                deleteNode(itemAlmacenarId);
                break;

            case R.id.btnActualizar:
                // tap en el boton actualizar
                updateNode (itemAlmacenarId);
                break;
        }
    }

    public void addNode()
    {
        //recolectar datos del formulari
        //grupo y materia

        String datosGrupo = spGrupo.getSelectedItem().toString();
        String datosMateria = spMateria.getSelectedItem().toString();
        String datosActividad = txtActividad.getText().toString().trim();

        if (datosActividad.isEmpty())
        {
            txtActividad.setError("Llenar campo");
            txtActividad.setFocusable(true);
        }else
        {
            //agregamos el dato a firebase
            //consultamos la base donde se agregaran los elementos

            String idDataBase = modelClass.push().getKey();
            //instancia de nuestro modelo de datos, para poder guardar informacion

            Model myActivity = new Model(idDataBase,
                    datosGrupo,
                    datosMateria,
                    datosActividad);

            //guardar en la base de datos de firebase
            modelClass.child("Lectures").
                    child(idDataBase).
                    setValue(myActivity);

            Toast.makeText(getApplicationContext(), "Agregado correctamente",
                    Toast.LENGTH_SHORT).show();
        }
    }


    //consultar los datos de la DB almacenada en firebase

    public void getDataFirebase()
    {


        //addValueEventListener hace el real time
        modelClass.child("Lectures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                //captura o toma de los datos almacenas de firebase (trae una copia al programa)
                {
                    //procesar la informacion que recolectamos de firebase

                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        //for mejorado, ayuda a no desbordar

                        String id= ds.child("id").getValue().toString();
                        String grupo = ds.child("group").getValue().toString();
                        String mat = ds.child("materia").getValue().toString();
                        String actividad = ds.child("activity").getValue().toString();

                        list.add(new Model(id, grupo, mat, actividad));
                    }

                    //llenar el recycler view

                    myRecyclerViewHolder = new MyRecyclerViewHolder(list);
                    recyclerView.setAdapter(myRecyclerViewHolder);

                    myRecyclerViewHolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            txtActividad.setText(list.get(recyclerView.getChildAdapterPosition(view)).getActivity());
                            txtActividad.setFocusable(true);

                            itemAlmacenarId = list.get(recyclerView.getChildAdapterPosition(view)).getId();
                            Toast.makeText(getApplicationContext(), itemAlmacenarId,Toast.LENGTH_SHORT).show();

                            inicializarItemM = list.get(recyclerView.getChildAdapterPosition(view)).getMateria() ;
                            inicializarItemG = list.get(recyclerView.getChildAdapterPosition(view)).getGroup() ;

                            spMateria.setSelection(obtenerPosicionItemGrupo(spMateria, inicializarItemM));
                            spGrupo.setSelection(obtenerPosicionItemMateria(spGrupo, inicializarItemG));

                        }
                    });
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static int obtenerPosicionItemMateria (Spinner spinnerM, String datoM){

        int posicionM = 0;

        for (int i = 0; i<spinnerM.getCount(); i++ ){

            if (spinnerM.getItemAtPosition(i).toString().equalsIgnoreCase(datoM))
            {
                posicionM = i;
            }
        }
        return posicionM;
    }

    public static int obtenerPosicionItemGrupo (Spinner spinner, String datoG){

        int posicionG = 0;

        for (int g = 0; g<spinner.getCount(); g++ ){

            if (spinner.getItemAtPosition(g).toString().equalsIgnoreCase(datoG))
            {
                posicionG = g;
            }
        }
        return posicionG;
    }

    //eliminar nodo
    public void deleteNode (String id)
    {//eliminar el valor selecionado
        modelClass.child("Lecures").child(id).removeValue().
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Elemento eliminado correctamente",  Toast.LENGTH_SHORT).show();
            }

            //por si no logra eliminarlos
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "No se pudo eliminar, intenta nuevamente",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateNode (String id )
    {
        String datoGrupo = spGrupo.getSelectedItem().toString();
        String datoMateria = spMateria.getSelectedItem().toString();
        String datoActividad = txtActividad.getText().toString();

        //HashMap cambiar un caracter a otro caracter
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("group", datoGrupo );
        dataMap.put("materia",datoMateria);
        dataMap.put("activity", datoActividad);
        //indicar la direccion de la informacion que sera actualizada
        modelClass.child("Lectures").child(id).updateChildren(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(getApplicationContext(), "Elemento actualizado correctamente", Toast.LENGTH_SHORT).show();
        //en caso de que ocurra algun error
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "No se pudo actualizar, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
