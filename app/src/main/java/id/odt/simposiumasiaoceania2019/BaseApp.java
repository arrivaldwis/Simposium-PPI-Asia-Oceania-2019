package id.odt.simposiumasiaoceania2019;

import android.app.Application;
import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by qornanali on 03/09/17.
 */

public class BaseApp extends Application {
    public static FirebaseFirestore db;
    public static FirebaseStorage storage;
    public static FirebaseAuth mAuth;
    public static StorageReference storageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
