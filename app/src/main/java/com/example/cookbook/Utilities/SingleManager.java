package com.example.cookbook.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.Models.Following;
import com.example.cookbook.Models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SingleManager {
    private static SingleManager instance = null;
    private Context context;
    private static FirebaseFirestore db;
    private static FirebaseStorage storage;
    private static Vibrator vibrator;
    private static UserManager userManager;
    private static DBManager dbManager;

    private SingleManager(Context context) {
        this.context = context;
    }

    public static SingleManager getInstance() {
        return instance;
    }
    public static void init(Context context) {
        User user1 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("john_doe")
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setProfile_URL("https://example.com/john_doe")
                .setBio("A passionate cook and food enthusiast.");

        User user2 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("jane_smith")
                .setFirstName("Jane")
                .setLastName("Smith")
                .setEmail("jane.smith@example.com")
                .setProfile_URL("https://example.com/jane_smith")
                .setBio("Lover of all things culinary. Always experimenting with new recipes.");

        User user3 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("chef_mike")
                .setFirstName("Mike")
                .setLastName("Anderson")
                .setEmail("chef.mike@example.com")
                .setProfile_URL("https://example.com/chef_mike")
                .setBio("Professional chef with a flair for fusion cuisine.");

        User user4 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("foodie_gal")
                .setFirstName("Emily")
                .setLastName("Brown")
                .setEmail("emily.brown@example.com")
                .setProfile_URL("https://example.com/foodie_gal")
                .setBio("Exploring the world, one dish at a time.");

        User user5 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("culinary_artist")
                .setFirstName("Alex")
                .setLastName("Chen")
                .setEmail("alex.chen@example.com")
                .setProfile_URL("https://example.com/culinary_artist")
                .setBio("Turning ingredients into masterpieces.");

        User user6 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("taste_explorer")
                .setFirstName("Samantha")
                .setLastName("Taylor")
                .setEmail("samantha.taylor@example.com")
                .setProfile_URL("https://example.com/taste_explorer")
                .setBio("On a quest to discover new flavors and cuisines.");

        User user7 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("recipe_lover")
                .setFirstName("Michael")
                .setLastName("Wilson")
                .setEmail("michael.wilson@example.com")
                .setProfile_URL("https://example.com/recipe_lover")
                .setBio("Collecting recipes from around the globe.");

        User user8 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("cooking_enthusiast")
                .setFirstName("Jessica")
                .setLastName("Johnson")
                .setEmail("jessica.johnson@example.com")
                .setProfile_URL("https://example.com/cooking_enthusiast")
                .setBio("Passionate about cooking and sharing delicious meals.");

        User user9 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("flavor_adventurer")
                .setFirstName("Daniel")
                .setLastName("Martinez")
                .setEmail("daniel.martinez@example.com")
                .setProfile_URL("https://example.com/flavor_adventurer")
                .setBio("Embarking on culinary journeys to tantalize the taste buds.");

        User user10 = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername("gourmet_expert")
                .setFirstName("Sophia")
                .setLastName("Garcia")
                .setEmail("sophia.garcia@example.com")
                .setProfile_URL("https://example.com/gourmet_expert")
                .setBio("Elevating everyday meals with gourmet flair.");

        synchronized (SingleManager.class) {
            if (instance == null) {
                instance = new SingleManager(context);
                db = FirebaseFirestore.getInstance();
                dbManager = new DBManager();
                storage = FirebaseStorage.getInstance();
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                userManager = new UserManager();
//                dbManager.saveNewUser(user1,(success, user) -> {});
//                dbManager.saveNewUser(user2,(success, user) -> {});
//                dbManager.saveNewUser(user3,(success, user) -> {});
//                dbManager.saveNewUser(user4,(success, user) -> {});
//                dbManager.saveNewUser(user5,(success, user) -> {});
//                dbManager.saveNewUser(user6,(success, user) -> {});
//                dbManager.saveNewUser(user7,(success, user) -> {});
//                dbManager.saveNewUser(user8,(success, user) -> {});
//                dbManager.saveNewUser(user9,(success, user) -> {});
//                dbManager.saveNewUser(user10,(success, user) -> {});
            }
        }
    }

    public FirebaseFirestore getDb() {
        return db;
    }
    public DBManager getDBManager() {
        return dbManager;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public UserManager getUserManager(){ return userManager; }
    public void toast(String text) {
        Toast.makeText(
                        context
                        , text
                        , Toast.LENGTH_SHORT)
                .show();    }
    public void vibrate(long milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }
    public byte[] compresImage(Uri uri){
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // here we can choose quality factor
        // in third parameter(ex. here it is 25)
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);

        byte[] fileInBytes = baos.toByteArray();
        return fileInBytes;
    }
}
