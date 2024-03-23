package duanvdph37524.fpoly.asm_gd1;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import duanvdph37524.fpoly.asm_gd1.Adapter.Student_Adapter;
import duanvdph37524.fpoly.asm_gd1.DTO.Student_DTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Danh_Sach extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private RecyclerView recyclerView;
    private Student_Adapter adapter;
    private View dialogView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);
        recyclerView = findViewById(R.id.recyclerViewStudents);


        // Set onClickListener for the button to show the dialog
        ImageView img_cuon = findViewById(R.id.img_cuon);
        img_cuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // Load students from server
        nodchanglist();
    }

    private void nodchanglist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Student_API api = retrofit.create(Student_API.class);
        Call<List<Student_DTO>> call = api.getStudents();
        call.enqueue(new Callback<List<Student_DTO>>() {
            @Override
            public void onResponse(Call<List<Student_DTO>> call, Response<List<Student_DTO>> response) {
                if (response.isSuccessful()) {
                    List<Student_DTO> studentList = response.body();
                    adapter = new Student_Adapter(Danh_Sach.this, studentList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Danh_Sach.this));
                } else {
                    Toast.makeText(Danh_Sach.this, "Không thể lấy dữ liệu từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Student_DTO>> call, Throwable t) {
                Toast.makeText(Danh_Sach.this, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Danh_Sach", "Đã xảy ra lỗi: " + t.getMessage(), t);
            }
        });
    }

    // Method to show dialog for adding new student
    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Danh_Sach.this);
        LayoutInflater inflater = LayoutInflater.from(Danh_Sach.this);
         dialogView = inflater.inflate(R.layout.dialog_add_danhsach, null);

        dialogBuilder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName_add);
        EditText editTextStudentId = dialogView.findViewById(R.id.editTextStudentId_add);
        EditText editTextStudentDtb = dialogView.findViewById(R.id.editTextStudentDtb_add);

        ImageView img_avatar = dialogView.findViewById(R.id.img_avatar_add);
        Button buttonChooseImage = dialogView.findViewById(R.id.buttonChooseImage_add);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave_add);
        AlertDialog alertDialog = dialogBuilder.create();

        // Set onClickListener for the button to choose image
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImagePicker();
            }
        });



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get student information from EditTexts
                String name = editTextName.getText().toString();
                String studentId = editTextStudentId.getText().toString();
                String diemtb = editTextStudentDtb.getText().toString();
                if (name.isEmpty() || studentId.isEmpty() || diemtb.isEmpty()) {
                    Toast.makeText(Danh_Sach.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return; // Thoát khỏi phương thức nếu có trường thông tin trống
                }
                // Kiểm tra xem diemtb có phải là số hay không
                try {
                    double diem = Double.parseDouble(diemtb);

                    // Kiểm tra xem giá trị có nằm trong khoảng từ 0 đến 10 hay không
                    if (diem >= 0 && diem <= 10) {
                        // Check if an image has been selected
                        if (selectedImageUri != null) {
                            String imagePath = getImagePath(selectedImageUri);
                            if (imagePath != null) {
                                // Create a new Student_DTO object
                                Student_DTO newStudent = new Student_DTO();
                                newStudent.setName(name);
                                newStudent.setMasv(studentId);
                                newStudent.setDiem(Double.parseDouble(diemtb));
                                newStudent.setImage(imagePath);

                                // Add the new student to the database
                                addStudentToDatabase(newStudent);
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(Danh_Sach.this, "Vui lòng chọn ảnh trước khi lưu", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Danh_Sach.this, "Vui lòng chọn ảnh trước khi lưu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Nếu giá trị không nằm trong khoảng từ 0 đến 10, hiển thị thông báo lỗi
                        Toast.makeText(Danh_Sach.this, "Điểm trung bình phải nằm trong khoảng từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    // Nếu diemtb không phải là số, hiển thị thông báo lỗi
                    Toast.makeText(Danh_Sach.this, "Vui lòng nhập số vào ô Điểm trung bình", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    // Method to open image picker
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    // Handle the result of image picker
    // Handle the result of image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                if (dialogView != null) {
                    ImageView img_avatar = dialogView.findViewById(R.id.img_avatar_add);
                    Picasso.get().load(selectedImageUri).into(img_avatar);
                }
            }
        }
    }

    // Method to get image path from URI
    private String getImagePath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    // Method to add student to the database
    private void addStudentToDatabase(Student_DTO student) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.24.26.93:3027/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Student_API api = retrofit.create(Student_API.class);
        Call<Student_DTO> call = api.addStudent(student);

        call.enqueue(new Callback<Student_DTO>() {
            @Override
            public void onResponse(Call<Student_DTO> call, Response<Student_DTO> response) {
                if (response.isSuccessful()) {
                    // Handle successful addition of student
                    Student_DTO addedStudent = response.body();
                    Toast.makeText(Danh_Sach.this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                    // Refresh student list or perform other necessary actions
                    nodchanglist();
                } else {
                    // Handle unsuccessful addition of student
                    Toast.makeText(Danh_Sach.this, "Thêm sinh viên không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student_DTO> call, Throwable t) {
                // Handle connection failure or request processing failure
                Toast.makeText(Danh_Sach.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
