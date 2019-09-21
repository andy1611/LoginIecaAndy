package com.andyg.loginiecaandy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener{

    //declarar variables
    Button btnMultimedia, btnStorage, btnCerarSesion, btnRealTime;
    EditText txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //instanciar variables
        btnMultimedia = findViewById(R.id.btnMultimedia);
        btnCerarSesion = findViewById(R.id.btnCerrarSesion);
        btnRealTime = findViewById(R.id.btnRealTime);
        btnStorage = findViewById(R.id.btnSorage);

        //instanciar botones par metodo onClick
        btnMultimedia.setOnClickListener(this);
        btnRealTime.setOnClickListener(this);
        btnCerarSesion.setOnClickListener(this);
        btnStorage.setOnClickListener(this);
    }

    //metodo onClick donde se asignaran las acciones a ejecutar
    @Override
    public void onClick(View view) {
        //con laid se identificara el boton y se ejecutara su accion correspondiente
        switch (view.getId())
        {
            case R.id.btnMultimedia:
                //ir a multimedia
                Intent in = new Intent(getApplicationContext(),Multimedia.class);
                startActivity(in);
                break;
            case R.id.btnRealTime:
                //ir a Real time
                Intent inR = new Intent(getApplicationContext(), RealTimeActivity.class);
                startActivity(inR);
                break;

            case R.id.btnCerrarSesion:
                //cerras la sesion iniciada
                Intent inC = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inC);
                break;

            case R.id.btnSorage:
                //ir a storage
                Intent inS = new Intent(getApplicationContext(), Storage.class);
                startActivity(inS);
                break;
        }
    }
}
