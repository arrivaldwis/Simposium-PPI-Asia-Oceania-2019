package id.odt.simposiumasiaoceania2019.model;

public class ActionModel {
    public String action;
    public String uid;
    public String nama;
    public long created_at;

    public ActionModel() {
    }

    public ActionModel(String action, String uid, String nama, long created_at) {
        this.action = action;
        this.uid = uid;
        this.nama = nama;
        this.created_at = created_at;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
