package com.example.arcasparesv21.Etiquetadoras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arcasparesv21.Etiquetadoras.AdapterSparesEtiq.AdapterSapresEtiq;
import com.example.arcasparesv21.Etiquetadoras.ModelSparesEtiq.ModelSparesEtiq;
import com.example.arcasparesv21.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class Activity_Spares extends AppCompatActivity {
  private Window window;
  private ProgressBar progressBar;
  private DatabaseReference myDatabase;
  private AdapterSapresEtiq mAdapter;
  private RecyclerView recyclerView;
  private LinearLayout linearLayout;
  private TextView nameElement;
  private ImageView returnAct;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_spares);
    progressBar = findViewById(R.id.progresar_spares);
    returnAct = findViewById(R.id.imageButton);
    nameElement = findViewById(R.id.nameElement);
    linearLayout = findViewById(R.id.LinearLayout_2);

    String element = getIntent().getStringExtra("element");
    String NameEtiq = getIntent().getStringExtra("Etiq");
    nameElement.setText(element);

    //DEFINIENDO EL RECYCLERVIEW
    recyclerView = findViewById(R.id.recyclerViewSidel);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //////////
    SetTypeActivity(NameEtiq,element);//SETEAR LA ACTIVITY SEGÚN LA ETIQUETADORA
    //RETORNAR A LA ANTERIOR ACTIVITY
    returnAct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent return_ = new Intent(Activity_Spares.this, Activity_Elements.class);
        return_.putExtra("Etiq",NameEtiq);//REGRESA EL TIPO DE ETIQ
        startActivity(return_);
      }
    });
  }
  private void SetTypeActivity(String s, String element) {
    if(Objects.equals(s,"Krones")) {
      ReadDatabase("1M4gIjCfaaZLcXnEPJ7Twq-Y3cRYLQ0ugRmBU6tiAy4Y","DataKrones",element);
      linearLayout.setBackgroundColor(android.graphics.Color.parseColor("#032995"));
      returnAct.setBackgroundColor(android.graphics.Color.parseColor("#032995"));
      this.window = getWindow();
      window.setStatusBarColor(Color.parseColor("#032995"));
    }
    else if(Objects.equals(s, "Sidel")){
      ReadDatabase("1FmFRYsyTjHhQEG80gL3MTYvdKDBKvhkyBIWMJgU4DHI","DataSidel",element);//"Data" la bse de datos sidel
      linearLayout.setBackgroundColor(android.graphics.Color.parseColor("#FF6B4E"));
      returnAct.setBackgroundColor(android.graphics.Color.parseColor("#FF6B4E"));
      this.window = getWindow();
      window.setStatusBarColor(Color.parseColor("#FF6B4E"));
    }
  }

  private void ReadDatabase(String iddrive, String s, String element) {
    myDatabase = FirebaseDatabase.getInstance().getReference().child(iddrive).child(s);
    //Query query = myDatabase.orderByChild("Elemento").startAt(element).endAt(element + "\uf8ff"); //query -> filtra por el element escogido
    Query query = myDatabase.orderByChild("Elemento").equalTo(element); // SOLO LOS QUE SON IGUALES AL ELEMENT
    FirebaseRecyclerOptions<ModelSparesEtiq> firebaseRecyclerOptions =
            new FirebaseRecyclerOptions.Builder<ModelSparesEtiq>().setQuery(query,ModelSparesEtiq.class).build();//setQuery(query.Model.class) query es el filtro antes creado
    //options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(myDatabase,Model.class).build();
    mAdapter = new AdapterSapresEtiq(firebaseRecyclerOptions,progressBar,linearLayout);
    mAdapter.startListening();
    recyclerView.setAdapter(mAdapter);
  }
}