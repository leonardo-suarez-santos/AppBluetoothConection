package com.example.leonardo_soares.appbluetoothconection.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardo_soares.appbluetoothconection.R;
import com.example.leonardo_soares.appbluetoothconection.model.Usuario;
import com.example.leonardo_soares.appbluetoothconection.utils.Base64Custom;
import com.example.leonardo_soares.appbluetoothconection.utils.ConfiguracaoFireBase;
import com.example.leonardo_soares.appbluetoothconection.utils.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText email, senha;
    private Button logar;
    private Usuario usuario;
    private FirebaseAuth autentificacao;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebase;
    private String identificadorUsuarioLogado;
    //  private String[] permissoesNecessarias = new String[]{
    //       android.Manifest.permission.SEND_SMS,           android.Manifest.permission.INTERNET

    // };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        verificarUsuarioLogado();


        email = (EditText) findViewById(R.id.edit_email);
        senha = (EditText) findViewById(R.id.edit_senha);
        logar = (Button) findViewById(R.id.button_logar);


        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                String emails= email.getText().toString();
                String senhas= senha.getText().toString();

                if ( !isEmailValido(emails)){

                    email.requestFocus();
                    email.setError("Email nulo ou invalido!!");

                }else if (isCampoVazio(senhas)){
                    senha.requestFocus();
                    senha.setError("Preencha o campo senha!!");

                }else {
                    validarLogin();
                }
            }
        });


    }

    private void verificarUsuarioLogado() {
        autentificacao = ConfiguracaoFireBase.getFireBaseAutencicacao();
        if (autentificacao.getCurrentUser() != null) {

            abrirTelaPrincipal();
        }


    }

    private void validarLogin() {

        autentificacao = ConfiguracaoFireBase.getFireBaseAutencicacao();
        autentificacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());



                    firebase = ConfiguracaoFireBase.getFireBase().child("usuarios").child(identificadorUsuarioLogado);

                    valueEventListenerUsuario= new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                            Preferencias preferencias = new Preferencias(LoginActivity.this);

                            preferencias.salvarUsuario(identificadorUsuarioLogado,usuarioRecuperado.getNome());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);



                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "sucesso Login", Toast.LENGTH_LONG).show();


                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        email.requestFocus();
                        email.setError("Email não existe ou foi desabilitado!");
                        erroExcecao = "Email não existe ou foi desabilitado!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        senha.requestFocus();
                        senha.setError("Senha errada ou inexistente");
                        erroExcecao = "Senha errada ou inexistente";

                    } catch (Exception e) {
                        erroExcecao = "FALHOU!!!";

                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "FALHA Login : " + erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private void abrirTelaPrincipal() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);

    }
    public   boolean isCampoVazio(String valor){

        boolean resultado = (TextUtils.isEmpty(valor)|| valor.trim().isEmpty() );


        return resultado;

    }
    private boolean isEmailValido(String email ){
        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());

        return resultado;


    }

}