package com.example.shopingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button  btnuploadfood,btnuploadclothes,btnuploadsmart,btnuploadchose;
    EditText txtdata ;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageReference = FirebaseStorage.getInstance().getReference("Product");
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        btnuploadfood= findViewById(R.id.btnuploadfood);
        btnuploadclothes= findViewById(R.id.btnuploadclouthes);
        btnuploadsmart= findViewById(R.id.btnuploadsmart);
        btnuploadchose= findViewById(R.id.btnuploadchose);
        txtdata = findViewById(R.id.txtdata);
        imgview =findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(MainActivity.this);// context name as per your project name


        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });


Buttons();

    }

    public void Buttons(){
        btn_click(btnuploadchose);
        btn_click(btnuploadclothes);
        btn_click(btnuploadfood);
        btn_click(btnuploadsmart);

    }
    public void btn_click(final Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        final String btn_name= button.getText().toString();
        UploadImage(btn_name);


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage(final String Gatogery) {

        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");

            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(Gatogery).child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            final String TempImageName = txtdata.getText().toString().trim();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    @SuppressWarnings("VisibleForTests")
                                    Product imageUploadInfo = new Product(TempImageName,uri.toString());
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(Gatogery).child(ImageUploadId).setValue(imageUploadInfo);
                                }
                            });


                            Intent intent=new Intent(MainActivity.this,ProductActivity.class);
                            intent.putExtra("name",Gatogery);
                            startActivity(intent);

                        }
                    });


        }
        else {

          //  Toast.makeText(MainActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(MainActivity.this,ProductActivity.class);
            intent.putExtra("name",Gatogery);
            startActivity(intent);
        }
    }

}
