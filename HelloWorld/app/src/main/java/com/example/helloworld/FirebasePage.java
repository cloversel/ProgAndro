package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class FirebasePage extends AppCompatActivity {

    private EditText emailMhs;
    private EditText passMhs;
    private EditText confirmMhs;
    private Button buttonSimpan;
    private Button buttonHapus;
    private Button buttonCari;
    private FirebaseFirestore firebaseFirestoreDb;
    private FirebaseAuth myFirebaseAuth;
    private String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_page);

        emailMhs = (EditText) findViewById(R.id.emailMhs);
        passMhs = (EditText) findViewById(R.id.passwordMhs);
        confirmMhs = (EditText) findViewById(R.id.confirmPassMhs);
        buttonSimpan = (Button) findViewById(R.id.simpanButton);
        buttonHapus = (Button) findViewById(R.id.hapusButton);
        buttonCari = (Button) findViewById(R.id.cariButton);

        firebaseFirestoreDb = FirebaseFirestore.getInstance();
        myFirebaseAuth = FirebaseAuth.getInstance();

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sanity check
                if (!emailMhs.getText().toString().isEmpty() && !passMhs.getText().toString().isEmpty()) {
                    if (passMhs.getText().toString().equals(confirmMhs.getText().toString())){

                        tambahMahasiswa();
                    }else{
                        Toast.makeText(getApplicationContext(), "Konfirmasi Password salah",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No dan Nama Mhs tidak boleh kosong",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataMahasiswa();
            }
        });

      //  buttonCari.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          getDataMahasiswa();
      //      }
       // });


    }


    private void tambahMahasiswa() {

     //   Mahasiswa mhs = new Mahasiswa(noMhs.getText().toString(),
      //          namaMhs.getText().toString(),
      //          phoneMhs.getText().toString());


        myFirebaseAuth.createUserWithEmailAndPassword(emailMhs.getText().toString(),passMhs.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserID = myFirebaseAuth.getCurrentUser().getUid();
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", emailMhs.getText().toString());
                    user.put("password", passMhs.getText().toString());
                    firebaseFirestoreDb.collection("DaftarMhs").document(UserID).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Mahasiswa berhasil didaftarkan",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "ERROR" + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());
                                }
                            });
                }
            }
        });


    }

   /* private void getDataMahasiswa() {

        DocumentReference docRef = firebaseFirestoreDb.collection("DaftarMhs").document(noMhs.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Mahasiswa mhs = document.toObject(Mahasiswa.class);
                        noMhs.setText(mhs.getNim());
                        namaMhs.setText(mhs.getNama());
                        phoneMhs.setText(mhs.getPhone());
                    } else {
                        Toast.makeText(getApplicationContext(), "Document tidak ditemukan",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Document error : " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }  */

    private void deleteDataMahasiswa() {
        firebaseFirestoreDb.collection("DaftarMhs").document(emailMhs.getText().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        emailMhs.setText("");
                        passMhs.setText("");
                        confirmMhs.setText("");
                        Toast.makeText(getApplicationContext(), "Mahasiswa berhasil dihapus",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error deleting document: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }





}
