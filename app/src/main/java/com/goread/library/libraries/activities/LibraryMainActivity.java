package com.goread.library.libraries.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.goread.library.R;
import com.goread.library.activities.ChatsActivity;
import com.goread.library.auth.LoginActivity;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.Order;
import com.goread.library.models.User;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LibraryMainActivity extends BaseActivity implements View.OnClickListener {

    MaterialCardView cvNewOrders, cvBooks, cvQuotes, cvChat;
    ImageButton btnLogout, btnGenerate;
    FirebaseAuth firebaseAuth;
    ArrayList<PieEntry> pieEntries;
    PieChart pieChart;
    DatabaseReference databaseReference;
    int myTotalOrders = 0, othersTotalOrders = 0;
    View bsheet;
    Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 22, PICK_FILE_REQUEST = 40;
    User library;
    TextView tvLibrary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();

        drawChart();

        // initCharts();

        if (ContextCompat.checkSelfPermission(LibraryMainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LibraryMainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PICK_FILE_REQUEST);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(LibraryMainActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQR();
            }
        });


    }

    @Override
    public int defineLayout() {
        return R.layout.activity_library_main;
    }


    private void drawChart() {
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieEntries = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        final int[] MY_COLORS = {Color.rgb(223, 153, 102), Color.rgb(82, 54, 32)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);


        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieEntries.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        Order order = postSnapshot2.getValue(Order.class);
                        if (order.getLibraryId().equals(firebaseAuth.getCurrentUser().getUid())) {
                            orderList.add(order);
                            myTotalOrders += order.getTotalPrice();
                        } else {
                            othersTotalOrders += order.getTotalPrice();

                        }
                    }

                }

                ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
                yvalues.add(new PieEntry(myTotalOrders, "Mine", 0));
                yvalues.add(new PieEntry(othersTotalOrders, "Others", 1));

                PieDataSet dataSet = new PieDataSet(yvalues, "Total Orders");
                PieData data = new PieData(dataSet);

                data.setValueFormatter(new PercentFormatter());
                pieChart.setData(data);
                pieChart.animateXY(500, 500);
                pieChart.getDescription().setEnabled(false);
                pieChart.setDrawSlicesUnderHole(true);
                dataSet.setColors(MY_COLORS);
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.WHITE);
                pieChart.getDescription().setEnabled(false);

                dataSet.setDrawValues(true);
                pieChart.setData(new PieData(dataSet));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initCharts() {
        pieEntries = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        final int[] MY_COLORS = {Color.rgb(82, 54, 32), Color.rgb(223, 153, 102)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);


        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieEntries.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        Order order = postSnapshot2.getValue(Order.class);
                        if (order.getLibraryId().equals(firebaseAuth.getCurrentUser().getUid())) {
                            orderList.add(order);
                            myTotalOrders += order.getTotalPrice();
                        } else {
                            othersTotalOrders += order.getTotalPrice();

                        }
                    }

                }


                PieEntry pieEntry = new PieEntry(0, "Mine: " + myTotalOrders + " RY");
                PieEntry pieEntry2 = new PieEntry(1, "Others " + othersTotalOrders + " RY");
                pieEntries.add(pieEntry);
                pieEntries.add(pieEntry2);


                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total Money");
                pieDataSet.setColors(colors);

                pieDataSet.setDrawValues(false);
                pieChart.setData(new PieData(pieDataSet));
                pieChart.animateXY(500, 500);
                pieChart.getDescription().setEnabled(false);
                pieChart.setDrawSlicesUnderHole(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void generateQR() {
        ImageView btnDownload;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LibraryMainActivity.this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(LibraryMainActivity.this)
                .inflate(R.layout.my_qr_dialog,
                        null);

        btnDownload = bottomSheetView.findViewById(R.id.imgDownload);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LibraryMainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

                try {
                    saveImage(bitmap, "MyQR");
                    bottomSheetDialog.cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LibraryMainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(firebaseAuth.getCurrentUser().getUid(), BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = bottomSheetView.findViewById(R.id.qrPlaceHolder);
            imageViewQrCode.setImageBitmap(bitmap);
            imageViewQrCode.setBackgroundColor(Color.BLUE);
            System.out.println("Generated" + firebaseAuth.getCurrentUser().getUid());
            bottomSheetDialog.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void shareQr(Uri imageUri) {
        String text = "This is Our Store QR Code \n Scan it in GoRead App and explore the books ";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        shareIntent.setType("image/*");
        // Launch sharing dialog for image
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;
        String folderName = "QRCodes";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getApplicationContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + folderName);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            shareQr(imageUri);
            System.out.println("Moha: " + getRealPathFromURI(this, imageUri));

            fos = resolver.openOutputStream(imageUri);

        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + folderName;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);
            System.out.println("Moha Path" + image.getAbsolutePath());
            System.out.println("Moha Path" + image.getCanonicalPath());
            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        Log.i("Saving", "saveImage: True");
        fos.flush();
        fos.close();
    }

    private void defineViews() {
        cvBooks = findViewById(R.id.card_upload_book);
        cvNewOrders = findViewById(R.id.card_new_order);
        cvQuotes = findViewById(R.id.card_upload_quote);
        cvChat = findViewById(R.id.card_chat);
        btnLogout = findViewById(R.id.btnLLogOut);
        btnGenerate = findViewById(R.id.btnGenerate);
        pieChart = findViewById(R.id.pieChart);
        tvLibrary = findViewById(R.id.library_name);
        getLibrary();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        cvBooks.setOnClickListener(this);
        cvNewOrders.setOnClickListener(this);
        cvQuotes.setOnClickListener(this);
        cvChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_upload_book:
                startActivity(new Intent(LibraryMainActivity.this, MyBooksActivity.class));
                break;

            case R.id.card_new_order:
                startActivity(new Intent(LibraryMainActivity.this, LibraryOrdersActivity.class));
                break;

            case R.id.card_upload_quote:
                startActivity(new Intent(LibraryMainActivity.this, MyQuotesActivity.class));
                break;

            case R.id.card_chat:
                startActivity(new Intent(LibraryMainActivity.this, ChatsActivity.class));
                break;
        }
    }

    public void getLibrary() {
        SharedPreferences mPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = mPrefs.getString("Key", "");
        library = gson.fromJson(json2, User.class);
        if (library != null) {
            tvLibrary.setText(library.getName());
        }
    }


}