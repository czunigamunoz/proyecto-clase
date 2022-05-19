package com.colombina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.colombina.Model.ContarProductoClass;
import com.colombina.Model.ProductoClass;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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

public class ContarProducto extends AppCompatActivity implements SensorEventListener {
    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    //variables firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    Spinner sp_producto;

    TextView txt_cantidad, txt_categoria, txt_contador, txt_aprobo;
    TextInputEditText txt_pedido;

    Button btn_iniciar, btn_finalizar;

    String categoria = "";
    String producto = "";
    Integer cantidad = 0;

    // Variables sensor
    SensorManager sm;
    Sensor sensor;
    int var1 = 0, var2 = 0;


    ProductoClass contarProducto;



    private List<ProductoClass> listProducto = new ArrayList<ProductoClass>();
    ArrayAdapter<ProductoClass> productoArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contar_producto);

        //variables sensor
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(sensor.TYPE_PROXIMITY);
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);


        sp_producto =  findViewById(R.id.spProducto);
        txt_cantidad = findViewById(R.id.textCantidad);
        txt_categoria = findViewById(R.id.textCategoria);
        txt_contador = findViewById(R.id.textContador);
        txt_pedido = findViewById(R.id.txtPedido);
        txt_aprobo = findViewById(R.id.textAprobo);

        btn_iniciar = findViewById(R.id.btnIniciar);
        btn_finalizar = findViewById(R.id.btnFinalizar);

        inicializarFirebase();
        spinnerProducto();
        listarProducto();



        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarConteo();
            }
        });


        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarConteo();

            }
        });
    }

    //spinner para seleccionar el tipo de articulo
    public void spinnerProducto(){

        Query queryProducto = FirebaseDatabase.getInstance().getReference("Producto").orderByValue();
        //Spinner para seleccionar tipo de articulo
        sp_producto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                producto = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void consultarProducto(){


        Query queryInfo = FirebaseDatabase.getInstance().getReference("Producto")
                .orderByChild("nombre_producto")
                .equalTo(producto);

        queryInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren() ){
                        contarProducto = snapshot.getValue(ProductoClass.class);
                    }
                }
                txt_categoria.setText(contarProducto.getCategoria());
                txt_cantidad.setText(contarProducto.getCantidad());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void finalizarConteo(){
        btn_iniciar.setVisibility(View.VISIBLE);
        btn_finalizar.setVisibility(View.GONE);
        txt_aprobo.setVisibility(View.VISIBLE);
        txt_contador.setVisibility(View.GONE);


        String aprobo = "";

        if (txt_cantidad.getText().toString().equals(txt_contador.getText().toString())){
            txt_aprobo.setText("APROBADO");
            aprobo = "APROBADO";
        }else{
            txt_aprobo.setText("NO APROBADO");
            aprobo = "NO APROBADO";
        }



        String cantidad = txt_cantidad.getText().toString();
        String contador = txt_contador.getText().toString();
        String categoria = txt_categoria.getText().toString();
        String pedido = txt_pedido.getText().toString();


        ContarProductoClass con = new ContarProductoClass();
        con.setUid_contar(UUID.randomUUID().toString());    //el UID es aleatorio
        con.setFecha(fecha());
        con.setCantidad(cantidad);
        con.setProducto(producto);
        con.setCategoria(categoria);
        con.setAprobo(aprobo);
        con.setConteo(contador);
        con.setPedido(pedido);
        databaseReference.child("Conteo_producto").child(con.getUid_contar()).setValue(con);

        limpiarCampos();

        Toast.makeText(this, "Registrado", Toast.LENGTH_LONG).show();

    }

    public void iniciarConteo(){

        String pedido = txt_pedido.getText().toString();

        if (pedido.equals("")){
            txt_pedido.setError("Requerido");
            txt_pedido.requestFocus();
        }else {
            btn_iniciar.setVisibility(View.GONE);
            btn_finalizar.setVisibility(View.VISIBLE);
            txt_aprobo.setVisibility(View.GONE);
            txt_contador.setVisibility(View.VISIBLE);
            consultarProducto();
        }


    }

    public void listarProducto() {

        Query queryProducto = FirebaseDatabase.getInstance().getReference("Producto").orderByValue();


        queryProducto.addListenerForSingleValueEvent(new ValueEventListener() {


            String precio = "", categoria = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    listProducto.clear();
                    for (DataSnapshot obj : datasnapshot.getChildren()) {
                        ProductoClass nombreProducto = obj.getValue(ProductoClass.class);
                        listProducto.add(nombreProducto);

                        productoArrayAdapter = new ArrayAdapter<ProductoClass>(ContarProducto.this, R.layout.list_item_articulo, listProducto);
                        sp_producto.setAdapter(productoArrayAdapter);
                    }
                } else {
                    listProducto.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Metodo para inicializar firebase
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    //Fecha del sistema
    public String fecha(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);
        return fecha;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String text = String.valueOf(event.values[0]);
        float valor = Float.parseFloat(text);

        txt_contador.setText(var2+"");

        if(valor==0){
            var1 = 1;
            //Toast.makeText(this, "si", Toast.LENGTH_LONG).show();
            var2 = var2 + var1;
            txt_contador.setText(var2+"");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void limpiarCampos(){
        txt_categoria.setText("");
        txt_cantidad.setText("");
        txt_pedido.setText("");
        var1 = 0;
        var2 = 0;
    }

}