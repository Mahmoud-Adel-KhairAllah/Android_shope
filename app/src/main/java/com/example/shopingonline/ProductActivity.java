package com.example.shopingonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    TextView textView_product;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecounter;
    String nameProduct;
    RecyclerView recyclerView;
    ProductAdabter productAdabter;
    ArrayList<Product> products;
    ProgressBar progressBar;
    FirebaseAnalytics mfirebaseAnalytics;
    static  int countcloth=0;
    static  int countsmart=0;
    static  int countchose=0;
    static  int countfood=0;

    int counter=0;
    Thread t;
    boolean iscount=true;


    // ...



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


              mfirebaseAnalytics=FirebaseAnalytics.getInstance(this);
            textView_product=findViewById(R.id.textView_product);
             Intent i =getIntent();
            if (i.hasExtra("name")){
                nameProduct= i.getStringExtra("name");
                textView_product.setText(nameProduct);
            }

            if (nameProduct.equals("ملابس")){
                countcloth++;
            }else if (nameProduct.equals("الأجهزة الذكية")){
                countsmart++;
            }else if (nameProduct.equals("الأحذية")){
                countchose++;
            }else if (nameProduct.equals("المأكولات")){
                countfood++;
            }

        SharedPreferences preferences=getSharedPreferences("count",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
         editor.putInt("countcloth",countcloth);
         editor.putInt("countsmart",countsmart);
         editor.putInt("countchose",countchose);
         editor.putInt("countfood",countfood);

         editor.commit();

        Log.d("sss", "onCreate:\ncountcloth= "+countcloth+"\ncountsmart="+countsmart+"\n countchose"+countchose+"\n countfood="+countfood);
        if (countfood==5){

            Toast.makeText(this, "555555"+nameProduct, Toast.LENGTH_SHORT).show();
            try {

                setProperity(nameProduct);
            }catch (RuntimeException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if (countchose==5){
            Toast.makeText(this, "555555"+nameProduct, Toast.LENGTH_SHORT).show();
            setProperity(nameProduct);

        }else if (countsmart==5){
            Toast.makeText(this, "555555"+nameProduct, Toast.LENGTH_SHORT).show();
            try {

                setProperity(nameProduct);
            }catch (RuntimeException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }else if (countcloth==5){
            Toast.makeText(this, "555555"+nameProduct, Toast.LENGTH_SHORT).show();
            try {

            setProperity(nameProduct);
            }catch (RuntimeException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Product").child(nameProduct);
        databaseReferencecounter = FirebaseDatabase.getInstance().getReference();
        recyclerView=findViewById(R.id.recyclarview);
        progressBar=findViewById(R.id.progress_circular);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        products =new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Product product=snapshot.getValue(Product.class);
                    products.add(product);
                }



                productAdabter=new ProductAdabter(ProductActivity.this, products);
                recyclerView.setAdapter(productAdabter);
                productAdabter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("sss", "loadPost:onCancelled", databaseError.toException());
                progressBar.setVisibility(View.VISIBLE);

                // ...
            }
        };
        databaseReference.addValueEventListener(postListener);


        trackUser();
        ContTimert();

    }
    void trackUser(){

        mfirebaseAnalytics.setCurrentScreen(ProductActivity.this,"category_"+nameProduct,null);
    }
    void setProperity(String name){
        mfirebaseAnalytics.setUserProperty("interest",name);

    }

    public void ContTimert(){


        t=new Thread(new Runnable() {
            @Override
            public void run() {
                 counter=0;
                while (iscount){

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter++;
                    Log.d("sss", "run: "+counter);
                }

            }
        });
        t.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        counter=0;
        iscount=false;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        String counterScreeenid = databaseReferencecounter.push().getKey();
        databaseReferencecounter.child("counter screen").child(nameProduct).child(counterScreeenid).setValue(counter);

    }
}
