package com.example.pelaporanbakesbangpolmalang;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pelaporanbakesbangpolmalang.helper.ApiHelper;
import com.example.pelaporanbakesbangpolmalang.helper.ImageResizer;
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.example.pelaporanbakesbangpolmalang.helper.VolleyHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddLaporanActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    // GMAP
    private static final int REQUEST_LOCATION_PERMISSION = 99;
    // Request code
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    // item dialog
    final CharSequence[] dialogItems = {"Camera", "Gallery", "Cancel"};

    Toolbar addLaporanToolbar;
    TextInputLayout searchInput;
    SessionHelper sessionHelper;
    RequestQueue requestQueue;

    // current location
    FusedLocationProviderClient fusedLocationProviderClient;

    //widget
    EditText searchBar;
    MaterialButton searchBtn, cameraButton, tambahBtn;
    TextInputLayout inputJudul, inputDeskripsi;
    ProgressDialog progressDialog;

    //map
    private LatLng lastKnownLocation;
    private LatLng locationLaporan;
    private float zoom = 10;
    private float zoom_in = 15;
    private GoogleMap mMap;
    private Bitmap laporanBitmap;
    private ImageView laporanImage;

    private int CAMERA_REQUEST = 1888;
    private int GALLERY_REQUEST = 1999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_laporan);

        init();
    }

    private void init() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddLaporanActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addLaporanMapView);
        mapFragment.getMapAsync(this);

        progressDialog = new ProgressDialog(this);
        sessionHelper = new SessionHelper(this);
        requestQueue = VolleyHelper.getInstance(this).getRequestQueue();

        addLaporanToolbar = findViewById(R.id.addLaporanToolbar);
        searchBar = findViewById(R.id.autoCompleteSearchInput);
        searchInput = findViewById(R.id.addLaporanLokasiInput);
        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchBtn = findViewById(R.id.searchButton);
        inputJudul = findViewById(R.id.addLaporanJudulInput);
        inputDeskripsi = findViewById(R.id.addLaporanDeskripsiInput);
        cameraButton = findViewById(R.id.addLaporanPilihFotoButton);
        laporanImage = findViewById(R.id.addLaporanImage);
        tambahBtn = findViewById(R.id.addLaporanSimpan);

        tambahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (laporanBitmap != null
                        && locationLaporan != null
                        && !inputJudul.getEditText().getText().toString().isEmpty()
                        && !inputDeskripsi.getEditText().getText().toString().isEmpty()) {
                    if (validateInput()) {
                        uploadLaporan();
                    }
                } else {
                    Toast.makeText(AddLaporanActivity.this, "Harap melengkapi semua data laporan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getKeyCode() == KeyEvent.ACTION_DOWN
                        || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    Toast.makeText(AddLaporanActivity.this, "mencariii", Toast.LENGTH_SHORT).show();

                    // execute search method
                    geoLocate();
                }

                return false;
            }
        });

        // setup toolbar
        setSupportActionBar(addLaporanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void selectImage() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddLaporanActivity.this);
        dialog.setTitle("Add Image");
        dialog.setItems(dialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // select by camera
                if (dialogItems[which].equals("Camera")) {
                    if (ContextCompat.checkSelfPermission(AddLaporanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddLaporanActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA
                                }, REQUEST_CAMERA);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }

                //select by gallery
                if (dialogItems[which].equals("Gallery")) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                }

                if (dialogItems[which].equals("Cancel")) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void uploadLaporan() {
        progressDialog.setMessage("Sedang memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest uploadRequest = new StringRequest(Request.Method.POST, ApiHelper.ADD_LAPORAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject objectResponse = new JSONObject(response);

                    Toast.makeText(AddLaporanActivity.this, objectResponse.getString("message"), Toast.LENGTH_SHORT).show();

                    Intent toHomeUser = new Intent(AddLaporanActivity.this, HomeActivity.class);
                    toHomeUser.putExtra("GOTO_FRAGMENT", "LAPORAN");
                    startActivity(toHomeUser);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(AddLaporanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(AddLaporanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", sessionHelper.getIdUser());
                params.put("judul", inputJudul.getEditText().getText().toString().trim());
                params.put("deskripsi", inputDeskripsi.getEditText().getText().toString().trim());
                params.put("foto", imageToString(laporanBitmap));
                params.put("alamat", searchBar.getText().toString().trim());
                params.put("lat", String.valueOf(locationLaporan.latitude));
                params.put("lng", String.valueOf(locationLaporan.longitude));
                return params;
            }
        };

        requestQueue.add(uploadRequest);
    }

    private boolean validateInput() {
        if (inputJudul.getEditText().getText().toString().isEmpty()) {
            inputJudul.setErrorEnabled(true);
            inputJudul.setError("Judul harus diisi");
            return false;
        } else {
            inputJudul.setErrorEnabled(false);
            inputJudul.setError("");
        }

        if (inputDeskripsi.getEditText().getText().toString().isEmpty()) {
            inputDeskripsi.setErrorEnabled(true);
            inputDeskripsi.setError("Deskripsi harus diisi");
            return false;
        } else {
            inputDeskripsi.setErrorEnabled(false);
            inputDeskripsi.setError("");
        }

        if (searchInput.getEditText().getText().toString().isEmpty()) {
            searchInput.setErrorEnabled(true);
            searchInput.setError("Alamat harus valid");
            return false;
        } else {
            searchInput.setErrorEnabled(false);
            searchInput.setError("");
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            tambahBtn.setEnabled(true);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            laporanBitmap = ImageResizer.reduceBitmapSize(photo, 2250000);

            laporanImage.setImageBitmap(laporanBitmap);
            Toast.makeText(this, "Berhasil mengambil gambar", Toast.LENGTH_SHORT).show();
        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            tambahBtn.setEnabled(true);
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                laporanBitmap = ImageResizer.reduceBitmapSize(photo, 2250000);

                laporanImage.setImageBitmap(laporanBitmap);
                Toast.makeText(this, "Berhasil mengambil gambar", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            tambahBtn.setEnabled(false);
            Toast.makeText(this, "Harap Pilih Gambar", Toast.LENGTH_SHORT).show();
        }
    }

    //    konversi gambar menjadi string
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void geoLocate() {
//        String searchString = searchBar.getText().toString();
        String searchString = searchInput.getEditText().getText().toString();

        Geocoder geocoder = new Geocoder(AddLaporanActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            searchBar.setText(address.getAddressLine(0));

            LatLng searchLocation = new LatLng(address.getLatitude(), address.getLongitude());
            locationLaporan = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().title(address.getAddressLine(0)).position(searchLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, zoom_in));

            inputJudul.getEditText().requestFocus();
//            Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
        } else {
            searchBar.setText("");
            searchBar.requestFocus();
            Toast.makeText(this, "Gagal mendapatkan alamat", Toast.LENGTH_LONG).show();
        }
    }

    private void geoLocateLatLng() {
        Geocoder geocoder = new Geocoder(AddLaporanActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            searchBar.setText(address.getAddressLine(0));

            locationLaporan = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().title(address.getAddressLine(0)).position(lastKnownLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, zoom_in));

            inputJudul.getEditText().requestFocus();

//            Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
        } else {
            searchBar.setText("");
            searchBar.requestFocus();
            Toast.makeText(this, "Gagal mendapatkan alamat", Toast.LENGTH_LONG).show();
        }
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(AddLaporanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddLaporanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, zoom));
                    geoLocateLatLng();
                }
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(AddLaporanActivity.this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(AddLaporanActivity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent toHomeUser = new Intent(AddLaporanActivity.this, HomeActivity.class);
        toHomeUser.putExtra("GOTO_FRAGMENT", "LAPORAN");
        startActivity(toHomeUser);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        LatLng alun = new LatLng(-8.168875, 113.702261);

//        mMap.addMarker(new MarkerOptions().title("Perbaikan jalan di jalan mastrip").position(polije).snippet("Laporan dari POLIJE"));
//        mMap.addMarker(new MarkerOptions().title("Pelanggaran Protokol Covid").position(alun).snippet("Laporan dari pusat kota (Alun - alun Jember)"));

        getLastKnownLocation();
        enableMyLocation();
    }
}