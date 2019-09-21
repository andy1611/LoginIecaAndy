package com.andyg.loginiecaandy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import modelosdatos.Upload;

public class Storage extends AppCompatActivity implements View.OnClickListener {

    //NO PUEDE SER MODIFICADA DESPUES, TIENE QUE SER EN MAYUSCULAS
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri mImageUri;

    //para el hilo de conexion
    private StorageTask mUploadTask;

    Button btnChooseFile, btnUpload, btnCardView;
    EditText txtFileName;
    ImageView img;
    ProgressBar progressBar;

    //conexion
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        //inicializar variables
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnUpload = findViewById(R.id.btnUpload);
        btnCardView = findViewById(R.id.btnCardView);

        txtFileName = findViewById(R.id.txtFileName);
        img = findViewById(R.id.imgStorage);

        progressBar = findViewById(R.id.progressBar);

        btnChooseFile.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnCardView.setOnClickListener(this);

        //sobre carga de constructor con un location que hace referencia a donde queremos guardar
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");



    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnChooseFile:
                //ejecutar el metodo
                seleccionarImagen();
                break;

            case R.id.btnUpload:
                //subir el archivo
                if (mUploadTask != null && mUploadTask.isInProgress())
                {//mientras esta subiendo
                    Toast.makeText(getApplicationContext(), getString(R.string.msgInProgress), Toast.LENGTH_SHORT).show();
                }else {//ejecutar metodo para subir el archivo
                    subirArchivo();
                }
                break;

            case R.id.btnCardView:
                //cabiar a la ventana de la vista
                Intent intent = new Intent(getApplicationContext(), ImagesActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void subirArchivo() {

        //validar que haya una imagen cargada

        if (mImageUri != null)
        {
            //subimos archivo
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getFileExtension (mImageUri));

            //abrir conexion o tarea para subir el archivo

            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //abrir el hilo de la conexion

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 5000);

                    Toast.makeText(getApplicationContext(), getString(R.string.msgSuccess), Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //usamos nuestro modelo de datos para crear la estructura
                            //que subiremos a firebase dentro de la base de datos
                            Upload upload = new Upload(txtFileName.getText().toString().trim(),uri.toString());

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // e.getMessage() manda el tipo de error
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                //ProgressBar
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int p = (int) (100*
                            (taskSnapshot.getBytesTransferred()
                            /taskSnapshot.getTotalByteCount()
                            ));

                    progressBar.setProgress(p);
                }
            });

        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.msgNotFileSelected), Toast.LENGTH_SHORT).show();
        }


    }

    //resolver que traiga la extencion del archivo
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void seleccionarImagen() {

        Intent intent = new Intent();
        //QUE VA REGRESAR
        intent.setType("image/*");
        //
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_IMAGE_REQUEST );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //validar codigo
        //requestCode==PICK_IMAGE_REQUEST   que los datos sean los que pedimos
        // && resultCode == RESULT_OK   se desepeño correctamente la activity, unicamente con msj de que no funciono
        // && data!= null    validar que traigo algo diferente de nulo
        // && data.getData() != null   los datos que nosotros queremos
        if (requestCode==PICK_IMAGE_REQUEST
        && resultCode == RESULT_OK
        && data!= null
        && data.getData() != null)
        {
            //Consultamos la informacion que regresa el chooser de android

            mImageUri = data.getData();

            //previsualizacion de la imagen

            img.setImageURI(mImageUri);
        }

    }
}
