package com.andyg.loginiecaandy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity  {

    //declaracion de variables
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    EditText txtEmail, txtPass;
    Button btnRegistrarme, btnLogin;
    SignInButton btnGoogle;

    //crear los objetos de conexion
    private FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Inicializar la variable de conexion con firebase
        //es una interfaz
        //da permiso para usar toda la intefaz conectando con Firebase
        myFirebaseAuth = FirebaseAuth.getInstance();

        btnGoogle = findViewById(R.id.btnGoogle);

        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarme = findViewById(R.id.btnRegistrarme);

        //instrucciones para el boton de google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInGoogle();

            }
        });

        //intruciones para las acciones del click del boton login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mandar llamar el metodo de login en firebase
                Login(txtEmail.getText().toString().trim(),
                        txtPass.getText().toString());
            }
        }
        );

        //registro de usuarios
        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        crearCuenta(txtEmail.getText().toString().trim(), txtPass.getText().toString());
    }
});


    }//cierra el oncreate


        protected void onStart () {
            super.onStart();
            FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
        }

        public void crearCuenta (String user, String pass){
            //aqui colocamos las instrucciones para agregar a un Usuario
            //a la cuenta de firebase
            //validar campos
            if (user.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Campo Usuario Obligatorio",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(pass))
            {
                Toast.makeText(getApplicationContext(),
                        "Campo Contraseña Obligatorio",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (pass.length()<8)
            {
                Toast.makeText(getApplicationContext(),
                        "Debe contener al menos 8 caracteres",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //agregar usuario

            myFirebaseAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //validar si el usuario se registro con exito en la plataforma
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),
                                "Registro completado con exito",
                                Toast.LENGTH_SHORT).show();
                    }else
                    {
                        //da informacion al usuario
                        if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),
                                    "Ya existe un Usuario con estos datos",
                                    Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Error al ejecutar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                });
            }//cerrar crear cuenta

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    //indicar las acciones para el login
            public void Login (final String user, final String pass){
            //manipular cadenas de texto
                if (TextUtils.isEmpty(user))
                {//llenar obligatoramente el email
                    txtEmail.setError("Campo Obligatorio");
                    txtEmail.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(pass))
                {//llenar obligatoramente la contraseña
                    txtPass.setError("Campo Obligatorio");
                    txtPass.setFocusable(true);
                    return;
                }
                if (pass.length()<8)
                {//minimo de caracteres
                    txtPass.setError("Debe contener al menos 8 caracteres");
                    txtPass.setFocusable(true);
                    return;
                }

                /*enviar los datos a la plataforma y validar que exista
                al menos u  usuario y password registrados
                 */

                myFirebaseAuth.signInWithEmailAndPassword(user,pass).
                        addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                            if (task.isSuccessful())
                            {//si el registro fue correcto se envia a la siguiente ventana
                                Intent intent = new Intent(getApplicationContext(),Home.class);
                                intent.putExtra("myUser", user);

                                intent.putExtra("myPass", pass);

                                startActivity(intent);

                            }else
                            {//si el usuario y contraseña no son correctos pedir verificacion de datos
                                Toast.makeText(getApplicationContext(),
                                        "Verificar Usuario y Contraseña",
                                        Toast.LENGTH_SHORT).show();
                            }

                    }
                });

            }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        myFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = myFirebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),Home.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }


                    }
                });
    }




        }


