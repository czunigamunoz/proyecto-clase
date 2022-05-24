package com.colombina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.colombina.Model.TipoUser;
import com.colombina.Model.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class registrar_usuario extends AppCompatActivity {
    //variables firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Spinner sp_rol;
    EditText txt_email;
    Button btn_guardar_usuario;

    String roles ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);


        sp_rol = findViewById(R.id.spRol);
        txt_email = findViewById(R.id.editEmail);
        btn_guardar_usuario = findViewById(R.id.btnGuardarUsuario);

        inicializarFirebase();
        listarTipo();
        spinnerRol();

        btn_guardar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarUsuario();
            }
        });
    }


    public void guardarUsuario(){
        // traer el valor del campo correo
        String email = txt_email.getText().toString();

        Usuario usuario = new Usuario();

        if (email.equals("")){
            validarCampos();
        }else {
            if (email.contains("@uniautonoma.edu.co")){

                Query queryUser = FirebaseDatabase.getInstance().getReference("Usuario")
                        .orderByChild("correo2")
                        .equalTo(email);

                queryUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        if(datasnapshot.exists()) {
                            Toast.makeText(registrar_usuario.this, "Este usuario ya esta registrado", Toast.LENGTH_SHORT).show();
                            txt_email.requestFocus();
                        }else{
                            usuario.setUid_user(UUID.randomUUID().toString());
                            usuario.setCorreo2(email);
                            usuario.setRol(roles);
                            databaseReference.child("Usuario").child(usuario.getUid_user()).setValue(usuario);
                            Toast.makeText(registrar_usuario.this, "Nuevo usuario guardado", Toast.LENGTH_SHORT).show();
                            limpiarCajas();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }else{
                Toast.makeText(this, "Debe ser un correo Institucional", Toast.LENGTH_LONG).show();
                txt_email.setError("Verificar");
                txt_email.requestFocus();
            }
        }
    }

    public void spinnerRol(){
        //Spinner para seleccionar categoria
        sp_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roles = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void listarTipo() {
        Query queryTipo = FirebaseDatabase.getInstance().getReference("Tipo").orderByValue();

        List<TipoUser> tipo_u = new ArrayList<>();
        queryTipo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    tipo_u.clear();
                    for (DataSnapshot obj : datasnapshot.getChildren()) {
                        String id = obj.getKey();
                        String nombre = obj.child("tipo_user").getValue().toString();
                        tipo_u.add(new TipoUser(id, nombre));

                    }
                    ArrayAdapter<TipoUser> arrayTipo = new ArrayAdapter<>(registrar_usuario.this, android.R.layout.simple_dropdown_item_1line, tipo_u);
                    sp_rol.setAdapter(arrayTipo);
                }else{
                    tipo_u.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void validarCampos(){
        String email = txt_email.getText().toString();
        if (email.equals("")){
            txt_email.setError("Requerido");
            txt_email.requestFocus();
        }
    }

    private void limpiarCajas() {
        txt_email.setText("");
    }

    //Metodo inicializar firebase
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
}