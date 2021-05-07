package com.spochi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Profile("!disable-firebase")
public class FirebaseConfig {

    @Value("${firebase.config}")
    private String firebaseConfig;

    @PostConstruct
    public void init() throws IOException {
        InputStream serviceAccount = new ByteArrayInputStream(Base64Utils.decodeFromString(firebaseConfig));

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options, "[DEFAULT]");
    }
}
