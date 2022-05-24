package com.colombina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.colombina.Model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    //variables firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView texUser, texEmail;
    ImageView userImg;
    CardView btn_register, btn_Contar, btn_historial, btn_registra_user, btn_registrar_category;
    ImageButton btn_logout;
    Usuario user;
    LinearLayout linear_aprobo, linear_denego, linear_admin1, linear_admin2, linear_admin3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        btn_register = findViewById(R.id.btnRegister);
        btn_Contar = findViewById(R.id.btnCount);
        btn_historial = findViewById(R.id.btnHistory);
        btn_logout = findViewById(R.id.btnLogout);
        btn_registra_user= findViewById(R.id.btnRegistrarUsuario);
        btn_registrar_category = findViewById(R.id.btnRegistrarCategoria);
        texUser = findViewById(R.id.txtUser);
        texEmail = findViewById(R.id.txtEmail);
        userImg = findViewById(R.id.imgUser);
        linear_aprobo = findViewById(R.id.ll_authenticated);
        linear_denego = findViewById(R.id.ll_no_authenticated);
        linear_admin1 = findViewById(R.id.llAdmin1);
        linear_admin2 = findViewById(R.id.llAdmin2);
        linear_admin3 = findViewById(R.id.llAdmin3);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        inicializarFirebase();

        //set datos:
        //txt.setText(currentUser.getUid());
        Global.empleado = currentUser.getDisplayName();
        Global.correo = currentUser.getEmail();



        if(Global.correo.contains("@uniautonoma.edu.co")){
            Query queryUser = FirebaseDatabase.getInstance().getReference("Usuario")
                    .orderByChild("correo2")
                    .equalTo(currentUser.getEmail());


            queryUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if(datasnapshot.exists()){
                        for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                            user = snapshot.getValue(Usuario.class);
                        }

                        texUser.setText(currentUser.getDisplayName());
                        texEmail.setText(currentUser.getEmail());
                        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(userImg);
                        assert user != null;
                        String rol = user.getRol();

                        if (!rol.equals("Administrador")){
                            linear_admin1.setVisibility(View.GONE);
                            linear_admin2.setVisibility(View.GONE);
                        }
                        else{
                            linear_admin3.setVisibility(View.GONE);
                        }
                    }else{
                        noPermiso();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            noPermiso();
        }

        //Configurar las gso para google signIn con el fin de luego desloguear de google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("464706820539-sjtml3rouvqnu80mjc6cc603j6c1u3ur.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //boton registrar producto
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerProducto = new Intent(Home.this, Producto.class);
                startActivity(registerProducto);
            }
        });

        //boton contar productos
        btn_Contar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent contarProducto = new Intent(Home.this, ContarProducto.class);
                startActivity(contarProducto);
            }
        });
        //boton historial
        btn_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historialProducto = new Intent(Home.this, Historial.class);
                startActivity(historialProducto);
            }
        });
        btn_registra_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrar_user = new Intent(Home.this, registrar_usuario.class);
                startActivity(registrar_user);
            }
        });
        btn_registrar_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registar_category = new Intent(Home.this, registrar_categoria.class);
                startActivity(registar_category);
            }
        });


        //salir
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               cerrarSesion();
            }
        });

    }

    public void cerrarSesion(){
        mAuth.signOut();
        //Cerrar sesión con google tambien: Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//Abrir MainActivity con SigIn button
                if(task.isSuccessful()){
                    Intent login = new Intent(getApplicationContext(), Login.class);
                    startActivity(login);
                    Home.this.finish();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión con google",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //decalarar variables globales
    public static class Global {
        public static String empleado;
        public static String correo;
    }


    public void noPermiso(){
        btn_historial.setVisibility(View.GONE);
        btn_register.setVisibility(View.GONE);
        btn_Contar.setVisibility(View.GONE);
        linear_aprobo.setVisibility(View.GONE);
        linear_denego.setVisibility(View.VISIBLE);

    }


    //Metodo inicializar firebase
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

}
