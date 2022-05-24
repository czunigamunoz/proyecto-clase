package com.colombina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.colombina.Model.Categoria;
import com.colombina.Model.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class registrar_categoria extends AppCompatActivity {
    //variables firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    EditText txt_categoria;
    Button btn_guardar_categoria;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_categoria);

        txt_categoria = findViewById(R.id.editCategoria);
        btn_guardar_categoria = findViewById(R.id.btnGuardarCategoria);

        inicializarFirebase();
        guardarCategoria();
    }

    public void guardarCategoria(){

        btn_guardar_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categoria cat = new Categoria();
                String categoria = txt_categoria.getText().toString();

                if (categoria.equals("")){
                    validarCampos();
                }else {
                    cat.setUid_categoria(UUID.randomUUID().toString());
                    cat.setNombre_categoria(categoria);
                    databaseReference.child("Categoria").child(cat.getUid_categoria()).setValue(cat);
                    Toast.makeText(registrar_categoria.this, "Categoria Agregada Correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCajas();
                }


            }
        });
    }

    public void validarCampos(){
        String categoria = txt_categoria.getText().toString();
        if (categoria.equals("")){
            txt_categoria.setError("Requerido");
            txt_categoria.requestFocus();
        }
    }

    private void limpiarCajas() {
        txt_categoria.setText("");
    }
    //Metodo inicializar firebase
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
}