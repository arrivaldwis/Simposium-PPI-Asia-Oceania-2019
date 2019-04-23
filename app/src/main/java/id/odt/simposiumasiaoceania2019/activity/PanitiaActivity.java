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
import com.google.firebase.firestore.Query;
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
import id.odt.simposiumasiaoceania2019.adapter.ActivityAdapter;
import id.odt.simposiumasiaoceania2019.adapter.PendaftarAdapter;
import id.odt.simposiumasiaoceania2019.model.ActionModel;
import id.odt.simposiumasiaoceania2019.model.UserModel;

public class PanitiaActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {

    @BindView(R.id.tv_email)
    FontAppCompatTextView tv_email;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_activity)
    RecyclerView rv_activity;
    @BindView(R.id.img_logout)
    ImageView img_logout;

    private FirebaseUser currentUser;
    private ArrayList<ActionModel> actionList = new ArrayList<>();
    private ActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panitia);
        ButterKnife.bind(this);
        fab.setOnClickListener(this);
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
        rv_activity.setLayoutManager(linearLayoutManager);
        mAdapter = new ActivityAdapter(actionList,this);
        rv_activity.setAdapter(mAdapter);
        loadActivity();
    }

    private void loadActivity() {
        actionList.clear();
        BaseApp.db
                .collection("action")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    actionList.clear();
                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        String uid = documentSnapshot.getData().get("uid").toString();
                        String action = documentSnapshot.getData().get("action").toString();
                        String nama = documentSnapshot.getData().get("nama").toString();
                        long created_at = Long.parseLong(documentSnapshot.getData().get("created_at").toString());

                        ActionModel model = new ActionModel(action, uid, nama, created_at);
                        actionList.add(model);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logout:
                BaseApp.mAuth.signOut();
                Intent intent1 = new Intent(PanitiaActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.fab:
                Intent intent = new Intent(PanitiaActivity.this, QRScanActivity.class);
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
