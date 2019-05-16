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
import com.google.firebase.firestore.Query;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.adapter.ActivityAdapter;
import id.odt.simposiumasiaoceania2019.model.ActionModel;

public class RecordsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_activity)
    RecyclerView rv_activity;
    @BindView(R.id.img_logout)
    ImageView img_logout;
    @BindView(R.id.img_activity)
    ImageView img_activity;
    @BindView(R.id.img_approve)
    ImageView img_approve;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    private FirebaseUser currentUser;
    private ArrayList<ActionModel> actionList = new ArrayList<>();
    private ActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panitia);
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
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
