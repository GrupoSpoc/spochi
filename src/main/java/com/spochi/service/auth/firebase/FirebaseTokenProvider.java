package com.spochi.service.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseTokenProvider {

    public String getUidFromToken(String firebaseToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(firebaseToken).getUid();
    }
}
