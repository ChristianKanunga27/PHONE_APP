package com.example.phone_shop_app;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // DEFINITIVE FIX: This is the correct way to initialize the Cloudinary SDK
        // using a configuration map.
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "duppdvfdg");
        // No API key or secret is needed for unsigned uploads on the client side
        MediaManager.init(this, config);
    }
}
