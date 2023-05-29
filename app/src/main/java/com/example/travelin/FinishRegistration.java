package com.example.travelin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FinishRegistration extends AppCompatActivity {
    //Deklarasi variabel
    private EditText et_email, et_password, et_confirmpassword;
    private Button btnlogin, btnregister;
    private FirebaseAuth auth;
    private String getEmail, getPassword, getConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_registration);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirmpassword = findViewById(R.id.et_confirmpassword);
        btnlogin = findViewById(R.id.btnlogin);
        btnregister = findViewById(R.id.btnregister);
        auth = FirebaseAuth.getInstance();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Perintah tombol register
                cekDataUser();

            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishRegistration.this, Home.class);
                startActivity(intent);
                onBackPressed();

            }
        });

    }

    boolean isEmail(EditText text) {
        CharSequence et_email = text.getText().toString();
        return (!TextUtils.isEmpty(et_email) && Patterns.EMAIL_ADDRESS.matcher(et_email).matches());
    }

    private void cekDataUser() {
        //mendapatkan data inputan user
        getEmail = et_email.getText().toString();
        getPassword = et_password.getText().toString();
        getConfirmPassword = et_confirmpassword.getText().toString();

        //Untuk mengecek apakah edittext email dan password kosong
        if (TextUtils.isEmpty(getEmail)) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();

        } else if (isEmail(et_email) == false) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(getPassword)) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (getPassword.length() < 6) {
            Toast.makeText(this, "Password Terlalu Pendek, minimal 6 karakter", Toast.LENGTH_SHORT).show();
        } else {
            createUserAccount();
        }
    }


    private void createUserAccount() {
        auth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        Pengguna user = new Pengguna(getEmail, getPassword,getConfirmPassword);
                        FirebaseDatabase.getInstance().getReference("Pengguna")
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //Cek status Keberhasilan saat mendaftarkan email dan sandi baru
                                        if (task.isSuccessful()) {
                                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(FinishRegistration.this, "Registrasi Berhasil !!, Please Check your email for verification", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(FinishRegistration.this, login.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(FinishRegistration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FinishRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }
                });
    }
}