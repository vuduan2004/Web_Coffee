package duanvdph37524.fpoly.asm_gd1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

public class Dang_Ky extends AppCompatActivity {
    private EditText editTextUsername,editTextPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        editTextUsername = findViewById(R.id.editTextUsername1);
        editTextPassword = findViewById(R.id.editTextPassword1);
        buttonRegister = findViewById(R.id.buttonRegister1);
        mAuth = FirebaseAuth.getInstance();
buttonRegister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if(email.equals("")||password.equals("")){
            Toast.makeText(Dang_Ky.this, "vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(Dang_Ky.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6 || !Character.isUpperCase(password.charAt(0))) {
            Toast.makeText(Dang_Ky.this, "Mật khẩu phải có ít nhất 6 kí tự và viết hoa chữ cái đầu tiên!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent in = new Intent(Dang_Ky.this,Dang_Nhap_GMAIL.class);
                            in.putExtra("email",email);
                            in.putExtra("password",password);
                            startActivity(in);
                            Toast.makeText(Dang_Ky.this, "Đăng Kí Thành Công!",
                                    Toast.LENGTH_SHORT).show();

                         } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Dang_Ky.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }); }
});

    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}