package id.odt.simposiumasiaoceania2019.activity;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.adapter.PendaftarAdapter;
import id.odt.simposiumasiaoceania2019.model.UserModel;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {

    @BindView(R.id.tv_email)
    FontAppCompatTextView tv_email;
    @BindView(R.id.rv_pendaftar)
    RecyclerView rv_pendaftar;
    @BindView(R.id.img_logout)
    ImageView img_logout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FirebaseUser currentUser;
    private ArrayList<UserModel> userList = new ArrayList<>();
    private PendaftarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        currentUser = BaseApp.mAuth.getCurrentUser();

        TedPermission.with(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

        populateData();
    }

    private void populateData() {
        fab.setOnClickListener(this);
        img_logout.setOnClickListener(this);
        tv_email.setText(currentUser.getEmail());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_pendaftar.setLayoutManager(linearLayoutManager);
        mAdapter = new PendaftarAdapter(userList,this);
        rv_pendaftar.setAdapter(mAdapter);
        loadPendaftar();
    }

    private void loadPendaftar() {
        BaseApp.db
                .collection("user")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    userList.clear();
                    mAdapter.notifyDataSetChanged();

                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        String uid = documentSnapshot.getData().get("uid").toString();
                        String alergi = documentSnapshot.getData().get("alergi").toString();
                        String bukti_url = documentSnapshot.getData().get("bukti_url").toString();
                        String gender = documentSnapshot.getData().get("gender").toString();
                        String jurusan = documentSnapshot.getData().get("jurusan").toString();
                        String kota = documentSnapshot.getData().get("kota").toString();
                        String nama = documentSnapshot.getData().get("nama").toString();
                        String negara = documentSnapshot.getData().get("negara").toString();
                        String no_passport = documentSnapshot.getData().get("no_passport").toString();
                        String nohp = documentSnapshot.getData().get("nohp").toString();
                        String universitas = documentSnapshot.getData().get("universitas").toString();
                        String wechat = documentSnapshot.getData().get("wechat").toString();
                        String whatsapp = documentSnapshot.getData().get("whatsapp").toString();
                        boolean approve = Boolean.parseBoolean(documentSnapshot.getData().get("approve").toString());
                        boolean puasa = Boolean.parseBoolean(documentSnapshot.getData().get("puasa").toString());
                        boolean vege = Boolean.parseBoolean(documentSnapshot.getData().get("vege").toString());
                        long created_at = Long.parseLong(documentSnapshot.getData().get("created_at").toString());
                        ArrayList<String> status = (ArrayList<String>) documentSnapshot.getData().get("status");

                        if(!approve) {
                            UserModel model = new UserModel(uid, approve, alergi, bukti_url, created_at,
                                    gender, jurusan, kota, nama, negara, no_passport, nohp, puasa,
                                    status, universitas, vege, wechat, whatsapp);

                            userList.add(model);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logout:
                BaseApp.mAuth.signOut();
                Intent intent1 = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.fab:
                Intent intent = new Intent(AdminActivity.this, QRScanActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

    }
}
