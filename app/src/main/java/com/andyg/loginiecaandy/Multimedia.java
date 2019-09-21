package com.andyg.loginiecaandy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PatternMatcher;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Multimedia extends AppCompatActivity {

    //Declarar variables
     private ImageView imgCamara, imgVideo, imgAudio;

    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";

     String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        //inicializar las variables
        imgAudio = findViewById(R.id.imgAudio);
        imgCamara = findViewById(R.id.imgCamara);
        imgVideo = findViewById(R.id.imgVideo);

        //metodo onClick para el bloque de ejecuciones del boton de audio
        imgAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ejecutar metodo
                abrirMusica();

            }
        });
        //metodo onClick para el bloque de ejecuciones del boton de video
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ejecutar metodo
                abrirvideo();

            }
        });
        //metodo onClick para el bloque de ejecuciones del boton de camara
        imgCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ejecutar el metodo
                abrircamara();

            }
        });



    }

    public void abrirMusica(){

        /*
         Intent intent =new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory()+ File.separator;)));
        startActivityForResult(intent, 20);

         */

        //abrir ventana para selecionar yn reproductor, reproducir audio
        path= Environment.getExternalStorageDirectory()+ File.separator;
        File music = new File(path);
        Intent intent =new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(music));
        startActivityForResult(intent, 20);

        //puede ir cualquier numero entero puesto que sirve para identificacion
    }



    public void abrirvideo (){

        /*
        path= Environment.getExternalStorageDirectory()+ File.separator;
        File video = new File(path);
        Intent intent =new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(video));
        startActivityForResult(intent, 20);
        */
        //abrir la camara para comenzar a grabar un video
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(i,10);

    }



    public void abrircamara (){

        //permisos en tiempo de ejecucion
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                &&
            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED
                &&
            ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED

        )//cierra el if

        {//cuerpo del if
            ActivityCompat.requestPermissions(
                    Multimedia.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1
            );
            return;
        }

        /*Arreglos

        String nombre [], n;
        las variables despues de los corchetes son arreglos
        String [] nombre2, m;
        */


        /*
        path= Environment.getExternalStorageDirectory()+ File.separator;
        File cam = new File(path);
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cam));
        startActivityForResult(intent, 20);
        */

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,2);



    } //cierra public abriri camara



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Guardar en la memoria

        switch (requestCode){

            case 2:
                //aqui guardo mi imagen
                //el 2 significa el numero del resultCode que manda el startActivityForResult

                Date date = new Date();

                //variable que almacena la respuesta de la toma de bits
                Bitmap picture = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

                picture.compress(Bitmap.CompressFormat.PNG, 0,arrayOutputStream);

                //java.io archivos de entrada y salida
                File file = new File(Environment.getExternalStoragePublicDirectory(
                       Environment.DIRECTORY_PICTURES),
                        "nombre"+ date.getTime()
                        + date.getHours()
                        + date.getMinutes()
                        + date.getSeconds() +".png" );

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(arrayOutputStream.toByteArray());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        //super: ejecutar el metodo del padre del que esta heredando los conceptos
        super.onActivityResult(requestCode, resultCode, data);
    }
}
