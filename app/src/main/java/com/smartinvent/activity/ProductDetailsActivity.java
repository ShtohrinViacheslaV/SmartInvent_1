package com.smartinvent.activity;

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
import com.smartinvent.model.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView txtName, txtDescription, txtProductWorkId, txtCount, txtCategory, txtStorage;
    private ImageView imgQrCode;
    private Button btnEditProduct, btnBack, btnSaveQr, btnShareQr;
    private Bitmap qrBitmap;
    private String productWorkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Ініціалізація елементів
        txtName = findViewById(R.id.txt_name);
        txtDescription = findViewById(R.id.txt_description);
        txtProductWorkId = findViewById(R.id.txt_product_work_id);
        txtCount = findViewById(R.id.txt_count);
        txtCategory = findViewById(R.id.txt_category);
        txtStorage = findViewById(R.id.txt_storage);
        imgQrCode = findViewById(R.id.img_qr_code);

        btnEditProduct = findViewById(R.id.btn_edit_product);
        btnBack = findViewById(R.id.btn_back);
        btnSaveQr = findViewById(R.id.btn_save_qr);
        btnShareQr = findViewById(R.id.btn_share_qr);

        // Отримання товару
        Product product = getIntent().getParcelableExtra(Constants.KEY_PRODUCT);

        if (product != null) {
            productWorkId = product.getProductWorkId();
            txtName.setText(product.getName());
            txtDescription.setText(product.getDescription());
            txtProductWorkId.setText(String.valueOf(productWorkId));
            txtCount.setText("Кількість: " + product.getCount());
            txtCategory.setText("Категорія: " + product.getCategory());
            txtStorage.setText("Склад: " + product.getStorage());

            generateQrCode(String.valueOf(productWorkId));
        }

        btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProductActivity.class);
            intent.putExtra(Constants.KEY_PRODUCT, product);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());

        btnSaveQr.setOnClickListener(v -> saveQrToGallery());
        btnShareQr.setOnClickListener(v -> shareQrImage());
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

            qrBitmap = bitmap;
            imgQrCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не вдалося згенерувати QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQrToGallery() {
        if (qrBitmap == null && imgQrCode.getDrawable() instanceof BitmapDrawable) {
            qrBitmap = ((BitmapDrawable) imgQrCode.getDrawable()).getBitmap();
        }

        if (qrBitmap != null) {
            String savedImageURL = MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    qrBitmap,
                    "QR_" + productWorkId,
                    "QR-код для продукту #" + productWorkId
            );

            if (savedImageURL != null) {
                Toast.makeText(this, "QR-код збережено в галерею", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Помилка збереження", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "QR-код не знайдено", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQrImage() {
        if (qrBitmap == null && imgQrCode.getDrawable() instanceof BitmapDrawable) {
            qrBitmap = ((BitmapDrawable) imgQrCode.getDrawable()).getBitmap();
        }

        if (qrBitmap != null) {
            try {
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), qrBitmap, "QR_" + productWorkId, null);
                Uri uri = Uri.parse(path);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/png");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Поділитись QR-кодом"));

            } catch (Exception e) {
                Toast.makeText(this, "Не вдалося поділитися зображенням", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "QR-код не знайдено", Toast.LENGTH_SHORT).show();
        }
    }
}
