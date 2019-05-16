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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.adapter.PendaftarAdapter;
import id.odt.simposiumasiaoceania2019.model.UserModel;

public class UserApproveActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_pendaftar)
    RecyclerView rv_pendaftar;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.img_logout)
    ImageView img_logout;
    @BindView(R.id.img_activity)
    ImageView img_activity;
    @BindView(R.id.img_approve)
    ImageView img_approve;

    private FirebaseUser currentUser;
    private ArrayList<UserModel> userList = new ArrayList<>();
    private PendaftarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        currentUser = BaseApp.mAuth.getCurrentUser();

        img_activity.setVisibility(View.GONE);
        img_approve.setVisibility(View.GONE);
        img_logout.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);

        populateData();
    }

    private void populateData() {
        btn_back.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_pendaftar.setLayoutManager(linearLayoutManager);
        mAdapter = new PendaftarAdapter(userList, this);
        rv_pendaftar.setAdapter(mAdapter);
        loadPendaftar();
    }

    private void loadPendaftar() {
        BaseApp.db
                .collection("user")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    userList.clear();
                    mAdapter.notifyDataSetChanged();

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
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
                        boolean net_dinner = Boolean.parseBoolean(documentSnapshot.getData().get("net_dinner").toString());
                        boolean puasa = Boolean.parseBoolean(documentSnapshot.getData().get("puasa").toString());
                        boolean vege = Boolean.parseBoolean(documentSnapshot.getData().get("vege").toString());
                        long created_at = Long.parseLong(documentSnapshot.getData().get("created_at").toString());
                        ArrayList<String> status = (ArrayList<String>) documentSnapshot.getData().get("status");

                        if (!approve) {
                            UserModel model = new UserModel(uid, approve, alergi, bukti_url, created_at,
                                    gender, jurusan, kota, nama, negara, no_passport, nohp, puasa,
                                    status, universitas, vege, wechat, whatsapp, net_dinner);

                            userList.add(model);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
