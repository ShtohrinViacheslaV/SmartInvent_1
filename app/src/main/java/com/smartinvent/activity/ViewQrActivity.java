package com.smartinvent.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.smartinvent.R;
import com.smartinvent.model.Constants;

import java.io.OutputStream;

public class ViewQrActivity extends AppCompatActivity {

    private ImageView imageViewQr;
    private TextView textViewProductId;
    private Button btnSaveQr, btnShareQr, btnBack;

    private long productWorkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        imageViewQr = findViewById(R.id.imageViewQr);
        textViewProductId = findViewById(R.id.textViewProductId);
        btnSaveQr = findViewById(R.id.btnSaveQr);
        btnShareQr = findViewById(R.id.btnShareQr);
        btnBack = findViewById(R.id.btnBack);

        // Отримуємо productWorkId
        productWorkId = getIntent().getLongExtra(Constants.KEY_PRODUCT_WORK_ID, -1);
        if (productWorkId == -1) {
            Toast.makeText(this, "Невірний ідентифікатор продукту", Toast.LENGTH_SHORT).show();
            finish();
        }

        textViewProductId.setText("" + productWorkId);
        generateQrCode(String.valueOf(productWorkId));

        btnSaveQr.setOnClickListener(v -> saveQrToGallery());
        btnShareQr.setOnClickListener(v -> shareQrImage());
        btnBack.setOnClickListener(v -> finish());
    }

    private void generateQrCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            int size = 500;
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }

            imageViewQr.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не вдалося згенерувати QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQrToGallery() {
        BitmapDrawable drawable = (BitmapDrawable) imageViewQr.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "QR_" + productWorkId,
                "QR-код для продукту #" + productWorkId
        );

        if (savedImageURL != null) {
            Toast.makeText(this, "QR-код збережено в галерею", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Помилка збереження", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQrImage() {
        BitmapDrawable drawable = (BitmapDrawable) imageViewQr.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        try {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QR_" + productWorkId, null);
            Uri uri = Uri.parse(path);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(shareIntent, "Поділитись QR-кодом"));

        } catch (Exception e) {
            Toast.makeText(this, "Не вдалося поділитися зображенням", Toast.LENGTH_SHORT).show();
        }
    }
}
