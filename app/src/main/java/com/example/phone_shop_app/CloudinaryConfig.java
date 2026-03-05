package com.example.phone_shop_app;

import android.content.Context;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {

    private static boolean isInitialized = false;

    public static synchronized void initialize(Context context) {
        if (isInitialized) {
            return; // Avoid re-initializing
        }

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "duppdvfdg");
        config.put("api_key", "435388717449898");

        
        // Initialize the MediaManager
        MediaManager.init(context, config);
        
        isInitialized = true;
    }
}
