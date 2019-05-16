package id.odt.simposiumasiaoceania2019.model;

public class PanitiaModel {
    public String nama;
    public String email;
    public String role;
    public String uid;

    public PanitiaModel() {
    }

    public PanitiaModel(String nama, String email, String role, String uid) {
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
