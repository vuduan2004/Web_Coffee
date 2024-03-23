package duanvdph37524.fpoly.asm_gd1.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import duanvdph37524.fpoly.asm_gd1.DTO.Student_DTO;
import duanvdph37524.fpoly.asm_gd1.R;
import duanvdph37524.fpoly.asm_gd1.Student_API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Student_Adapter extends RecyclerView.Adapter<Student_Adapter.Viewholder> {
    private Context context;
    private List<Student_DTO> list_student;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Activity activity;
    private View dialogVieww;
    private Uri selectedImageUri; // Biến thành viên selectedImageUri để lưu trữ đường dẫn ảnh đã chọn

    public Student_Adapter(Activity activity, List<Student_DTO> list_student) {
        this.activity = activity;
        this.list_student = list_student;
        this.context = activity.getApplicationContext();
    }

    public void setStudentList(List<Student_DTO> newList) {
        this.list_student = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_sach, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Student_DTO student_dto = list_student.get(position);
        holder.tvIdStudent.setText("Tên Sinh viên: " + student_dto.getName());
        holder.tvTenStudent.setText("Mã Sinh Viên: " + student_dto.getMasv());
        holder.tvDtbStudent.setText("Điểm trung bình: " + student_dto.getDiem());
        // Gán hình ảnh từ URL vào ImageView sử dụng thư viện Picasso
        Picasso.get().load(student_dto.getImage()).into(holder.imgAvatar);

        // Đăng ký sự kiện cho nút xóa và nút sửa
        holder.imgXoaStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentIdToDelete = student_dto.get_id();
                deleteStudentFromServer(studentIdToDelete);
                notchanglist();
            }
        });

        holder.imgEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                dialogVieww = inflater.inflate(R.layout.dialog_edit_danhsach, null);
                dialogBuilder.setView(dialogVieww);

                EditText editTextName = dialogVieww.findViewById(R.id.editTextName);
                EditText editTextStudentId = dialogVieww.findViewById(R.id.editTextStudentId);
                EditText editTextStudentDtb = dialogVieww.findViewById(R.id.editTextStudentDtb);
                ImageView img_avatar_edit = dialogVieww.findViewById(R.id.img_avatar);

                Button buttonChooseImage = dialogVieww.findViewById(R.id.buttonChooseImage);
                Button buttonSave = dialogVieww.findViewById(R.id.buttonSave);
                AlertDialog alertDialog = dialogBuilder.create();
                editTextName.setText(student_dto.getName());
                editTextStudentId.setText(student_dto.getMasv());
                editTextStudentDtb.setText(String.valueOf(student_dto.getDiem()));
                Picasso.get().load(student_dto.getImage()).into(img_avatar_edit);
                buttonChooseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openImagePicker(); // Gọi phương thức chọn ảnh
                    }
                });

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get student information from EditTexts
                        String name = editTextName.getText().toString();
                        String studentId = editTextStudentId.getText().toString();
                        String diemtb = editTextStudentDtb.getText().toString();

                        // Kiểm tra xem các trường thông tin có trống không
                        if (name.isEmpty() || studentId.isEmpty() || diemtb.isEmpty()) {
                            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return; // Thoát khỏi phương thức nếu có trường thông tin trống
                        }

                      try {
                            double diem = Double.parseDouble(diemtb);
   if (diem >= 0 && diem <= 10) {

                                Student_DTO updatedStudent = new Student_DTO();
                                updatedStudent.setName(name);
                                updatedStudent.setMasv(studentId);
                                updatedStudent.setDiem(Double.parseDouble(diemtb));
                                //      updatedStudent.setAvatar(imagePath);
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://localhost:3000/api/") // Địa chỉ URL cơ sở của máy chủ
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                Student_API api = retrofit.create(Student_API.class);

                                Call<Student_DTO> call = api.updateStudent(student_dto.get_id(), updatedStudent);

                                call.enqueue(new Callback<Student_DTO>() {
                                    @Override
                                    public void onResponse(Call<Student_DTO> call, Response<Student_DTO> response) {
                                        if (response.isSuccessful()) {
                                            // Xử lý thành công
                                            Student_DTO updatedStudent = response.body();
                                            Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                            notchanglist();
                                            alertDialog.dismiss();
                                            // Cập nhật dữ liệu trong danh sách hoặc hiển thị thông báo thành công
                                        } else {
                                            Log.d("ResponseError", "Error body: " + response.errorBody());
                                            Toast.makeText(context, "Lỗi if", Toast.LENGTH_SHORT).show();
                                        }
                                        alertDialog.dismiss(); // Đóng dialog sau khi hoàn thành yêu cầu
                                    }

                                    @Override
                                    public void onFailure(Call<Student_DTO> call, Throwable t) {
                                        Toast.makeText(context, "Lõi if ònifa", Toast.LENGTH_SHORT).show();
                                        Log.e("API Error", "Call failed: " + t.getMessage(), t);
                                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss(); // Đóng dialog khi gặp lỗi
                                    }
                                });
                            } else {
                                // Nếu giá trị không nằm trong khoảng từ 0 đến 10, hiển thị thông báo lỗi
                                Toast.makeText(context, "Điểm trung bình phải nằm trong khoảng từ 0 đến 10", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            // Nếu diemtb không phải là số, hiển thị thông báo lỗi
                            Toast.makeText(context, "Vui lòng nhập số vào ô Điểm trung bình", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_student.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView tvIdStudent, tvTenStudent, tvDtbStudent;
        public ImageView imgAvatar, imgXoaStudent, imgEditStudent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvIdStudent = itemView.findViewById(R.id.tv_id_student);
            tvTenStudent = itemView.findViewById(R.id.tv_ten_student);
            tvDtbStudent = itemView.findViewById(R.id.tv_dtb_student);
            imgAvatar = itemView.findViewById(R.id.image_avatar);
            imgXoaStudent = itemView.findViewById(R.id.img_xoa_student);
            imgEditStudent = itemView.findViewById(R.id.img_edit_student);
        }
    }

    private void notchanglist() {
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
                    List<Student_DTO> updatedList = response.body();
                    // Cập nhật danh sách sinh viên trong adapter
                    setStudentList(updatedList);
                } else {
                    Toast.makeText(context, "Không thể cập nhật danh sách sinh viên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student_DTO>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteStudentFromServer(String studentId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Student_API api = retrofit.create(Student_API.class);
        Call<Void> call = api.deleteStudent(studentId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi xóa sinh viên thành công
                    Toast.makeText(context, "Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                    // Refresh danh sách sinh viên sau khi xóa
                    notchanglist();
                } else {
                    // Xử lý khi xóa sinh viên không thành công
                    Toast.makeText(context, "Xóa sinh viên không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối hoặc lỗi xử lý yêu cầu
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    // Handle the result of image picker
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData(); // Lấy URI của ảnh đã chọn
                // Hiển thị ảnh đã chọn lên ImageView trong dialog
                if (dialogVieww != null) {
                    ImageView img_avatar_edit = dialogVieww.findViewById(R.id.img_avatar);
                    // Sử dụng Picasso để tải hình ảnh từ URI và hiển thị nó trong ImageView
                    Picasso.get().load(selectedImageUri).into(img_avatar_edit);
                }
            }
        }
    }
    private String getImagePath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String imagePath = cursor.getString(columnIndex);
                    return imagePath;
                }
            } finally {
                cursor.close(); // Đảm bảo rằng bạn đóng con trỏ sau khi sử dụng
            }
        }
        return null;
    }







}
