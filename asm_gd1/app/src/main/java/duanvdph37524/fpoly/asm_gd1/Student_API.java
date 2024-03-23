package duanvdph37524.fpoly.asm_gd1;
import java.util.List;

import duanvdph37524.fpoly.asm_gd1.DTO.Student_DTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Student_API {
    @PUT("students/{id}")
    Call<Student_DTO> updateStudent(@Path("id") String studentId, @Body Student_DTO student);
    @DELETE("students/{id}")
    Call<Void> deleteStudent(@Path("id") String studentId);
    @GET("get-list-fruit")
    Call<List<Student_DTO>> getStudents();
    @POST("students")
    Call<Student_DTO> addStudent(@Body Student_DTO student);

}
