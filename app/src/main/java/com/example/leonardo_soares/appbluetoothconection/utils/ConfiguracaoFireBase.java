package com.example.leonardo_soares.appbluetoothconection.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFireBase {

    private static DatabaseReference referenciaFireBase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFireBase() {


        if (referenciaFireBase == null) {
            referenciaFireBase = FirebaseDatabase.getInstance().getReference();

        }
        return referenciaFireBase;


    }

    public static FirebaseAuth getFireBaseAutencicacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;

    }
}
