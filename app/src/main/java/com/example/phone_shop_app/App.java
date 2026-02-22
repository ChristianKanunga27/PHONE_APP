
package com.example.phone_shop_app;

import android.app.Application;
import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // This is the correct way to initialize Cloudinary.
        // It uses a config map.
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "duppdvfdg");
        config.put("api_key", "435388717449898");
        // No API secret in client-side code
        MediaManager.init(this, config);
    }
}
