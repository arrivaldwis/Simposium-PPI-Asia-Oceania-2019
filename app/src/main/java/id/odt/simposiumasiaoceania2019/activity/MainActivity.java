package id.odt.simposiumasiaoceania2019.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pixplicity.fontview.FontAppCompatTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.util.Util;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, PermissionListener {

    @BindView(R.id.tv_email)
    FontAppCompatTextView tv_email;
    @BindView(R.id.tv_status)
    FontAppCompatTextView tv_status;
    @BindView(R.id.tv_lengkapi)
    FontAppCompatTextView tv_lengkapi;
    @BindView(R.id.ll_daftar)
    NestedScrollView ll_daftar;
    @BindView(R.id.rl_empty)
    RelativeLayout rl_empty;
    @BindView(R.id.rl_qrcode)
    RelativeLayout rl_qrcode;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.empty_box)
    LottieAnimationView empty_box;

    //Form
    @NotEmpty
    @BindView(R.id.nama)
    MaterialEditText nama;
    @NotEmpty
    @BindView(R.id.no_passport)
    MaterialEditText no_passport;
    @NotEmpty
    @BindView(R.id.kota)
    MaterialEditText kota;
    @NotEmpty
    @BindView(R.id.negara)
    MaterialEditText negara;
    @NotEmpty
    @BindView(R.id.universitas)
    MaterialEditText universitas;
    @NotEmpty
    @BindView(R.id.jurusan)
    MaterialEditText jurusan;
    @NotEmpty
    @BindView(R.id.no_hp)
    MaterialEditText no_hp;
    @BindView(R.id.wechat)
    MaterialEditText wechat;
    @BindView(R.id.whatsapp)
    MaterialEditText whatsapp;
    @BindView(R.id.rb_laki)
    RadioButton rb_laki;
    @BindView(R.id.rb_perempuan)
    RadioButton rb_perempuan;
    @BindView(R.id.rb_puasa)
    RadioButton rb_puasa;
    @BindView(R.id.rb_no_puasa)
    RadioButton rb_no_puasa;
    @BindView(R.id.rb_vege)
    RadioButton rb_vege;
    @BindView(R.id.rb_no_vege)
    RadioButton rb_no_vege;
    @BindView(R.id.rb_kacang)
    RadioButton rb_kacang;
    @BindView(R.id.rb_telur)
    RadioButton rb_telur;
    @BindView(R.id.rb_seafood)
    RadioButton rb_seafood;
    @BindView(R.id.rb_other)
    RadioButton rb_other;
    @BindView(R.id.rb_tidak_ada)
    RadioButton rb_tidak_ada;
    @BindView(R.id.ck_delegasi)
    CheckBox ck_delegasi;
    @BindView(R.id.ck_peninjau)
    CheckBox ck_peninjau;
    @BindView(R.id.ck_peninjau_plus)
    CheckBox ck_peninjau_plus;
    @BindView(R.id.ck_peserta_konferensi)
    CheckBox ck_peserta_konferensi;
    @BindView(R.id.ck_peserta_tambahan)
    CheckBox ck_peserta_tambahan;
    @BindView(R.id.ll_foto)
    LinearLayout ll_foto;
    @BindView(R.id.img_foto)
    ImageView img_foto;
    @BindView(R.id.img_foto2)
    ImageView img_foto2;
    @BindView(R.id.img_logout)
    ImageView img_logout;
    @BindView(R.id.img_approve)
    ImageView img_approve;
    @BindView(R.id.img_activity)
    ImageView img_activity;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.email_sign_in_button)
    LinearLayout email_sign_in_button;
    @BindView(R.id.et_lainnya)
    MaterialEditText et_lainnya;
    @BindView(R.id.btn_screenshot)
    LinearLayout btn_screenshot;
    @BindView(R.id.btn_save)
    FontAppCompatTextView btn_save;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ArrayList<String> status = new ArrayList<>();
    private String img_uploaded_url = "";
    private String gender = "L";
    private String alergi = "Tidak ada";
    private boolean puasa = true;
    private boolean vege = false;
    private boolean imgUpload = false;
    private boolean isApprove = false;
    private FirebaseUser currentUser;
    private Validator validator;
    private ProgressDialog pDialog;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        currentUser = BaseApp.mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            TedPermission.with(this)
                    .setPermissionListener(this)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .check();

            dialogLoading();
            checkRole();
            btn_back.setVisibility(View.GONE);
            tv_email.setText(currentUser.getEmail());
            validator = new Validator(this);
            validator.setValidationListener(this);
            email_sign_in_button.setOnClickListener(this);
            img_logout.setOnClickListener(this);
            img_activity.setOnClickListener(this);
            img_approve.setOnClickListener(this);
            ll_foto.setOnClickListener(this);
            fab.setOnClickListener(this);
            btn_screenshot.setOnClickListener(this);
            ck_delegasi.setOnCheckedChangeListener(this);
            ck_peninjau.setOnCheckedChangeListener(this);
            ck_peninjau_plus.setOnCheckedChangeListener(this);
            ck_peserta_tambahan.setOnCheckedChangeListener(this);
            ck_peserta_konferensi.setOnCheckedChangeListener(this);
            rb_laki.setOnCheckedChangeListener((buttonView, isChecked) -> gender = "M");
            rb_perempuan.setOnCheckedChangeListener((buttonView, isChecked) -> gender = "F");
            rb_puasa.setOnCheckedChangeListener((buttonView, isChecked) -> puasa = isChecked);
            rb_vege.setOnCheckedChangeListener((buttonView, isChecked) -> vege = isChecked);
            rb_kacang.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    alergi = "Kacang";
                    et_lainnya.setVisibility(View.GONE);
                }
            });
            rb_telur.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    alergi = "Telur";
                    et_lainnya.setVisibility(View.GONE);
                }
            });
            rb_seafood.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    alergi = "Seafood";
                    et_lainnya.setVisibility(View.GONE);
                }
            });
            rb_other.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    alergi = "Other";
                    et_lainnya.setVisibility(View.VISIBLE);
                }
            });
            rb_tidak_ada.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    alergi = "Tidak ada";
                    et_lainnya.setVisibility(View.GONE);
                }
            });
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Harap tunggu...");
        }
    }

    String role;

    private void checkRole() {
        dialog.show();
        BaseApp.db
                .collection("panitia")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(task12 -> {
                    if (task12.getResult() != null) {
                        if (task12.getResult().size() > 0) {
                            for (DocumentSnapshot documentSnapshot : task12.getResult()) {
                                role = documentSnapshot.getData().get("role").toString();
                                BaseApp.db
                                        .collection("panitia")
                                        .document(documentSnapshot.getId())
                                        .update("uid", currentUser.getUid());
                            }

                            if (role.toLowerCase().equals("panitia")) {
                                img_activity.setVisibility(View.VISIBLE);
                                img_approve.setVisibility(View.GONE);
                                tv_status.setText("Panitia");
                                ll_daftar.setVisibility(View.GONE);
                                tv_lengkapi.setVisibility(View.GONE);
                                rl_empty.setVisibility(View.GONE);
                                fab.setVisibility(View.VISIBLE);
                                generateQRCode();
                                dialog.dismiss();
                            } else if (role.toLowerCase().equals("admin")) {
                                img_approve.setVisibility(View.VISIBLE);
                                img_activity.setVisibility(View.VISIBLE);
                                tv_status.setText("Administrator");
                                ll_daftar.setVisibility(View.GONE);
                                tv_lengkapi.setVisibility(View.GONE);
                                rl_empty.setVisibility(View.GONE);
                                fab.setVisibility(View.VISIBLE);
                                generateQRCode();
                                dialog.dismiss();
                            }
                        } else {
                            img_approve.setVisibility(View.GONE);
                            img_activity.setVisibility(View.GONE);
                            checkUserApprove();
                        }
                    } else {
                        img_approve.setVisibility(View.GONE);
                        img_activity.setVisibility(View.GONE);
                        checkUserApprove();
                    }
                });
    }

    private void checkUserApprove() {
        BaseApp.db
                .collection("user")
                .whereEqualTo("email", currentUser.getEmail())
                .limit(1)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null) {
                        if (queryDocumentSnapshots.size() > 0) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                boolean approve = Boolean.parseBoolean(documentSnapshot.getData().get("approve").toString());
                                isApprove = approve;
                                if (approve) {
                                    tv_status.setText("Terdaftar");
                                    ll_daftar.setVisibility(View.GONE);
                                    tv_lengkapi.setVisibility(View.GONE);
                                    rl_empty.setVisibility(View.GONE);
                                    generateQRCode();
                                } else {
                                    tv_status.setText("Proses verifikasi");
                                    tv_lengkapi.setVisibility(View.GONE);
                                    ll_daftar.setVisibility(View.GONE);
                                    rl_empty.setVisibility(View.VISIBLE);
                                    rl_qrcode.setVisibility(View.GONE);
                                    empty_box.setRepeatCount(Animation.INFINITE);
                                    empty_box.playAnimation();
                                }
                            }
                        } else {
                            tv_status.setText("Belum Terdaftar");
                            tv_lengkapi.setVisibility(View.VISIBLE);
                            ll_daftar.setVisibility(View.VISIBLE);
                            rl_empty.setVisibility(View.GONE);
                            rl_qrcode.setVisibility(View.GONE);
                        }
                    } else {
                        tv_status.setText("Belum Terdaftar");
                        tv_lengkapi.setVisibility(View.VISIBLE);
                        ll_daftar.setVisibility(View.VISIBLE);
                        rl_empty.setVisibility(View.GONE);
                        rl_qrcode.setVisibility(View.GONE);
                    }
                });
        dialog.dismiss();
    }

    private void dialogLoading() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LottieAnimationView loading = dialog.findViewById(R.id.loading);
        loading.setRepeatCount(Animation.INFINITE);
        loading.playAnimation();
    }

    private void generateQRCode() {
        String text = currentUser.getEmail(); // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img_qrcode.setImageBitmap(bitmap);
            rl_qrcode.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void uploadFoto() {
        pDialog.show();

        Long tsLong = System.currentTimeMillis() / 1000;
        StorageReference storyImage = BaseApp.storageRef.child("bukti_transfer/" + tsLong + ".jpg");

        Bitmap bitmap = Bitmap.createBitmap(img_foto2.getDrawable().getIntrinsicWidth(), img_foto2.getDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        img_foto2.getDrawable().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        img_foto2.getDrawable().draw(canvas);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storyImage.putBytes(data);

        uploadTask.addOnProgressListener(taskSnapshot -> {
            int progress = (100 * (int) taskSnapshot.getBytesTransferred()) / (int) taskSnapshot.getTotalByteCount();
        }).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            return storyImage.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                if (downloadUri == null)
                    return;

                img_uploaded_url = downloadUri.toString();
                loadUpdateUser();
            }
        });
    }

    private void loadUpdateUser() {
        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("nama", nama.getText().toString());
        userUpdate.put("gender", gender);
        userUpdate.put("no_passport", no_passport.getText().toString());
        userUpdate.put("status", status);
        userUpdate.put("kota", kota.getText().toString());
        userUpdate.put("negara", negara.getText().toString());
        userUpdate.put("universitas", universitas.getText().toString());
        userUpdate.put("jurusan", jurusan.getText().toString());
        userUpdate.put("puasa", puasa);
        userUpdate.put("vege", vege);
        userUpdate.put("alergi", alergi);
        userUpdate.put("nohp", no_hp.getText().toString());
        userUpdate.put("wechat", wechat.getText().toString());
        userUpdate.put("whatsapp", whatsapp.getText().toString());
        userUpdate.put("bukti_url", img_uploaded_url);
        userUpdate.put("approve", false);
        userUpdate.put("created_at", System.currentTimeMillis());
        userUpdate.put("uid", currentUser.getUid());

        BaseApp.db
                .collection("user")
                .add(userUpdate)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data berhasil di kirim..", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            File imgFile = new File(image.getPath());
            if (imgFile.exists()) {
                Bitmap myBitmap = Util.decodeFile(imgFile.getAbsolutePath());
                img_foto2.setImageBitmap(myBitmap);
                img_foto.setVisibility(View.GONE);
                imgUpload = true;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onValidationSucceeded() {
        if (status.size() == 0) {
            Toast.makeText(this, "Harap isi status anda dalam Simposium", Toast.LENGTH_SHORT).show();
            return;
        }

        if (alergi.equals("Other") && et_lainnya.getText().toString().isEmpty()) {
            et_lainnya.setError("Harap isi jenis alergi");
            return;
        } else {
            alergi = et_lainnya.getText().toString();
        }

        if (imgUpload) {
            uploadFoto();
        } else {
            Toast.makeText(this, "Harap sertakan foto bukti transfer", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (status.size() >= 2) {
                buttonView.setChecked(false);
            } else {
                status.add(buttonView.getText().toString());
            }
        } else {
            try {
                status.remove(buttonView.getText().toString());
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                validator.validate();
                break;
            case R.id.img_logout:
                BaseApp.mAuth.signOut();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_foto:
                ImagePicker.create(MainActivity.this)
                        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                        .toolbarFolderTitle("Select Picture") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                        .single() // single mode
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .start();
                break;
            case R.id.btn_screenshot:
                takeScreenshot();
                break;
            case R.id.btn_save:
                takeScreenshot();
                break;
            case R.id.fab:
                intent = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(intent);
                break;
            case R.id.img_approve:
                intent = new Intent(MainActivity.this, UserApproveActivity.class);
                startActivity(intent);
                break;
            case R.id.img_activity:
                intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "Identitas QR Code sudah disimpan pada folder Screenshot", Toast.LENGTH_LONG).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

    }
}
