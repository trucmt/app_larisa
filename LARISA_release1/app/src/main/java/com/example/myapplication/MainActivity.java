package com.example.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    TextView userName;
    TextView welcome;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    Button scanButton;
    TextView lastScanText;
    private final Context context = this;
    SharedPreferences sharedPreferences;
    FilePickerDialog dialog;
    private List<AppInfo> applist;
    private AppsAdapter appsAdapter;
    private boolean withSysApps = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        welcome=findViewById(R.id.welcome);
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);
        applist= new ArrayList<>();

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null){
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }
        else {
            String email = getIntent().getStringExtra("email");
            if (email != null) {
                int atIndex = email.indexOf("@");
                if (atIndex != -1) {
                    email = email.substring(0, atIndex);
                }
            }
            userName.setText(email);
        }

        setupSharedPreferences();
        Toolbar toolbar = findViewById(R.id.toolbar);
        lastScanText = findViewById(R.id.textView1);
        setSupportActionBar(toolbar);
        withSysApps = sharedPreferences.getBoolean("includeSystemApps", false);

        String lastScan = sharedPreferences.getString("lastScan", this.getString(R.string.never));
        lastScanText.setText(this.getString(R.string.last_scan) + " " + lastScan);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ScanActivity.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    if (dialog != null) {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (dialog != null) {   //Show dialog if the read permission has been granted.
                    dialog.show();
                }
            } else {
                //Permission has not been granted. Notify the user.
                Toast.makeText(MainActivity.this, this.getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("includeSystemApps") && sharedPreferences.getBoolean(key, false)) {
            withSysApps = sharedPreferences.getBoolean(key, false);
            Toast.makeText(getApplicationContext(), this.getString(R.string.scan_system_apps_toast), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan_apk) {
            DialogProperties properties = new DialogProperties();
            properties.selection_mode = DialogConfigs.SINGLE_MODE;
            properties.selection_type = DialogConfigs.FILE_SELECT;
            properties.root = Environment.getExternalStorageDirectory();
            properties.error_dir = properties.root;
            properties.offset = properties.root;
            properties.extensions = new String[]{"apk"};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }

            dialog = new FilePickerDialog(MainActivity.this, properties);
            dialog.setTitle(this.getString(R.string.select_a_file));
            dialog.setDialogSelectionListener(files -> {
                //files is the array of the paths of files selected by the Application User.
                if (files != null) {
                    File selectedFile = new File(files[0]);
                    if (selectedFile.exists() && selectedFile.isFile()) {
                        final ApkScanner apkScanner = new ApkScanner(context, files[0]);
                        apkScanner.execute();
                        //Toast.makeText(context,files[0],Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.file_does_not_exist), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.error_loading_file), Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        } else if (id == R.id.nav_custom_scan) {
            this.startActivity(new Intent(this, CustomScanActivity.class).putExtra("withSysApps", withSysApps));
        }
        else if (id == R.id.nav_policy) {
            this.startActivity(new Intent(this, PolicyActivity.class));
        }
        else if(id==R.id.nav_out){
            if (item.getItemId() == R.id.nav_out) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                return true; // Đã xử lý thành công sự kiện
            }
        }
        else if(id==R.id.nav_web){
            String url = "https://www.larisa.tech/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean isServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}