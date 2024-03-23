package duanvdph37524.fpoly.asm_gd1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Dang_Nhap_GMAIL extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;

    private Button buttonLogin, buttonRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_gmail);
        editTextUsername = findViewById(R.id.editTextUsernameDN);
        editTextPassword = findViewById(R.id.editTextPasswordDN);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance(); // Khởi tạo FirebaseAuth
        Intent intent = getIntent();
        if (intent!=null) {
            Bundle ex = intent.getExtras();
            if (ex!=null){
                editTextUsername.setText(ex.getString("email"));
                editTextPassword.setText(ex.getString("password"));
   }}
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                dangNhap(username, password);
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dang_Nhap_GMAIL.this, Dang_Ky.class));
            }
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void dangNhap(String username, String password) {
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Dang_Nhap_GMAIL.this, "Thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Dang_Nhap_GMAIL.this,Danh_Sach.class));
                } else {
                    Toast.makeText(Dang_Nhap_GMAIL.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
