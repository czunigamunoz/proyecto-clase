package com.colombina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.colombina.Model.ContarProductoClass;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {
    //variables firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //ArrayList para mostrar los productos registrados
    private List<ContarProductoClass> listHistorial = new ArrayList<ContarProductoClass>();
    ArrayAdapter<ContarProductoClass> historialArrayAdapter;


    ListView listView_historial;

    ContarProductoClass contarSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        listView_historial = findViewById(R.id.listHistorial);

        inicializarFirebase();
        listarHistorial();
        seleccionarHistorial();

    }

    //Metodo que se llama en el onCreate principal para listar historial
    private void listarHistorial() {
        Query queryHistorial = FirebaseDatabase.getInstance().getReference("Conteo_producto").orderByValue();

        queryHistorial.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                //Lleno el arrayLisPrestamo
                if (datasnapshot.exists()){
                    listHistorial.clear();
                    for (DataSnapshot objSnaptshot : datasnapshot.getChildren()){
                        ContarProductoClass con = objSnaptshot.getValue(ContarProductoClass.class);
                        listHistorial.add(con);

                        historialArrayAdapter = new ArrayAdapter<ContarProductoClass>(Historial.this, R.layout.list_item_articulo, listHistorial);
                        listView_historial.setAdapter(historialArrayAdapter);
                    }
                }else{
                    listHistorial.clear();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void seleccionarHistorial(){
        //seleccionar un registro de prestamo del arrayList Prestamo
        listView_historial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //invisibleCampos();
                contarSelect = (ContarProductoClass) parent.getItemAtPosition(position);

                //abre una ventana flotante que es un activity con medidas mas pequeñas llevandose el UID del prestamo
                Intent intent = new Intent(Historial.this, Info_historial.class);
                intent.putExtra("uid_contar", contarSelect.getUid_contar());
                startActivity(intent );
                Historial.this.overridePendingTransition(R.anim.zoom_back,R.anim.zoom_in);
            }
        });
    }

    //Metodo para inicializar firebase
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
}