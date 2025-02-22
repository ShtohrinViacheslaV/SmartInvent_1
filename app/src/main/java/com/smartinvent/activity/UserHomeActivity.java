package com.smartinvent.activity;

import androidx.fragment.app.Fragment;
import com.smartinvent.fragment.BottomNavigationUserFragment;

public class UserHomeActivity extends BaseHomeActivity {
    @Override
    protected Fragment getBottomNavigationFragment() {
        return new BottomNavigationUserFragment();
    }
}


//package com.smartinvent.activity;
//
//
//
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import com.smartinvent.R;
//
//
//public class UserHomeActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main_user);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//
//
////    private void downloadDatabase() {
////        String url = "https://yourserver.com/api/database/export"; // Замініть на ваш URL
////
////        OkHttpClient client = new OkHttpClient();
////        Request request = new Request.Builder().url(url).build();
////
////        client.newCall(request).enqueue(new Callback() {
////            @Override
////            public void onFailure(Call call, IOException e) {
////                e.printStackTrace();
////            }
////
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////                if (response.isSuccessful()) {
////                    File dbFile = new File(getApplicationContext().getFilesDir(), "inventory.sqlite");
////                    FileOutputStream fos = new FileOutputStream(dbFile);
////                    fos.write(response.body().bytes());
////                    fos.close();
////                }
////            }
////        });
////    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////
////import android.os.Bundle;
////import androidx.activity.EdgeToEdge;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////import com.smartinvent.R;
////
////public class UserHomeActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        setContentView(R.layout.activity_main_user);
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////    }
////}