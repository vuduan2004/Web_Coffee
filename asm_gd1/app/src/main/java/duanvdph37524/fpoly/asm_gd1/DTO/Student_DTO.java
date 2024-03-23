package duanvdph37524.fpoly.asm_gd1.DTO;

public class Student_DTO {

    private String _id;
    private String name;
    private String masv;
    private double diem;
    private String image;

    public Student_DTO() {
    }

    public Student_DTO(String _id, String name, String masv, double diem, String image) {
        this._id = _id;
        this.name = name;
        this.masv = masv;
        this.diem = diem;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
