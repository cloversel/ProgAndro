package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText nim, nama;
    Button login;

    SharedPreferences pref;

    public static final String TAG = "MainActivity";
    private Button btnStartJob;
    private Button btnCancelJob;
    private Button btnFirebasePage;
    private FirebaseFirestore firebaseFirestoreDb;
    private FirebaseAuth myFirebaseAuth;

    String idNim;
    String passNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartJob = findViewById(R.id.buttonStartJob);
        btnCancelJob = findViewById(R.id.buttonCancelJob);
        btnFirebasePage = findViewById(R.id.firebase);
        nim = findViewById(R.id.editTextNim);
        nama = findViewById(R.id.editTextNama);
        login = findViewById(R.id.btnlogin);

        myFirebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataMahasiswa();

            }
        });

       /* pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (pref.getString("KEY1",null).equals("Home")){
            GoToHomePage();
        }
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        button = findViewById((R.id.button));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("admin@ti.ukdw.ac.id") && password.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                    GoToHomePage();
                }else{
                    Toast.makeText(getApplicationContext(), "Email or Password is wrong!", Toast.LENGTH_LONG).show();
                }

            }
        });   */
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(View view){
        ComponentName componentName = new ComponentName(getApplicationContext(), MyJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.i(TAG, "scheduleJob: Job Scheduled");
        }else{
            Log.i(TAG, "scheduleJob: Job Scheduling Failed");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(View view){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.i(TAG, "cancelJob");
    }


    public void GoToHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void GoToFirebasePage(View view){
        Intent intent = new Intent(this, FirebasePage.class);
        startActivity(intent);
    }

    private void getDataMahasiswa() {

        myFirebaseAuth.signInWithEmailAndPassword(nim.getText().toString(), nama.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    GoToHomePage();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Login gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });

     /*   DocumentReference docRef = firebaseFirestoreDb.collection("DaftarMhs").document(nim.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Mahasiswa mhs = document.toObject(Mahasiswa.class);
                        //noMhs.setText(mhs.getNim());
                      //  namaMhs.setText(mhs.getNama());
                       // phoneMhs.setText(mhs.getPhone());
                        idNim = mhs.getNim();
                        passNama = mhs.getNama();
                    } else {
                        Toast.makeText(getApplicationContext(), "Document tidak ditemukan",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Document error : " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });  */
    }


}
