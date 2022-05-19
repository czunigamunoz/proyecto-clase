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

import com.colombina.Model.CategoriaClass;
import com.colombina.Model.ProductoClass;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Producto extends AppCompatActivity {
    //variables firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText edt_producto, edt_cantidad, edt_precio;
    Spinner sp_categoria;
    Button btn_guardar_producto;
    String categoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);


        edt_producto = findViewById(R.id.editNombreProducto);
        edt_cantidad = findViewById(R.id.editCantidad);
        edt_precio = findViewById(R.id.editPrecio);
        sp_categoria = findViewById(R.id.spCategoria);
        btn_guardar_producto = findViewById(R.id.btnGuardarProducto);


        //INICIALIZARMOS FIREBASE
        inicializarFirebase();
        listarCateroria();
        spinnerCategoria();



        btn_guardar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarProducto();
            }
        });

    }

    public void listarCateroria() {
        Query queryCateroria = FirebaseDatabase.getInstance().getReference("Categoria").orderByValue();

        List<CategoriaClass> categorias = new ArrayList<>();
        queryCateroria.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    categorias.clear();
                    for (DataSnapshot obj : datasnapshot.getChildren()) {
                        String id = obj.getKey();
                        String nombre = obj.child("nombre_categoria").getValue().toString();
                        categorias.add(new CategoriaClass(id, nombre));

                    }
                    ArrayAdapter<CategoriaClass> arrayCategoria = new ArrayAdapter<>(Producto.this, android.R.layout.simple_dropdown_item_1line, categorias);
                    sp_categoria.setAdapter(arrayCategoria);
                }else{
                    categorias.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void spinnerCategoria(){
        //Spinner para seleccionar categoria
        sp_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void guardarProducto(){

        //recupero los datos que estan en los editex
        String producto = edt_producto.getText().toString();
        String cantidad = edt_cantidad.getText().toString();
        String precio = edt_precio.getText().toString();


        ProductoClass art = new ProductoClass();


        if (producto.equals("") || cantidad.equals("") || precio.equals("")) {
            validacion();
        } else {
            art.setUid(UUID.randomUUID().toString());    //el UID es aleatorio
            art.setFecha(fecha());
            art.setNombre_producto(producto);
            art.setCategoria(categoria);
            art.setCantidad(cantidad);
            art.setPrecio(precio);
            databaseReference.child("Producto").child(art.getUid()).setValue(art);


            limpiarCajas();
            Toast.makeText(this, "Se guardó correctamente el producto", Toast.LENGTH_LONG).show();
        }


        }

    public void validacion(){
        //recupero los datos que estan en los editex
        String producto = edt_producto.getText().toString();
        String cantidad = edt_cantidad.getText().toString();
        String precio = edt_precio.getText().toString();

        if (producto.equals("")) {
            edt_producto.setError("Requerido");
            edt_producto.requestFocus();
        }
        if (cantidad.equals("")){
            edt_cantidad.setError("Requerido");
            edt_cantidad.requestFocus();
        }
        if (precio.equals("")){
            edt_precio.setError("Requerido");
            edt_precio.requestFocus();
        }


    }
    //Metodo para obtener la fecha del sistema
    public String fecha(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);
        return fecha;
    }

    private void limpiarCajas() {
        edt_producto.setText("");
        edt_cantidad.setText("");
        edt_precio.setText("");
    }


    //Metodo inicializar firebase
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
}