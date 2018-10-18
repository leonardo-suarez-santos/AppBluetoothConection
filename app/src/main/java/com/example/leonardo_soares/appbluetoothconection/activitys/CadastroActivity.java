package com.example.leonardo_soares.appbluetoothconection.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {
    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autentificacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());



                String nomes = nome.getText().toString();
                String emails = email.getText().toString();
                String senhas = senha.getText().toString();
                // Boolean res = false;
                if (isCampoVazio(nomes)) {
                    nome.requestFocus();
                    nome.setError("Preencha o campo nome!!");

                } else if (isCampoVazio(emails)) {
                    email.requestFocus();
                    email.setError("Preencha o campo email!!");

                } else if (isCampoVazio(senhas)) {

                    senha.requestFocus();
                    senha.setError("Preencha o campo senha!!");

                }else{

                    cadastraUsuario();

                }


            }
        });


    }

    private void cadastraUsuario() {

        autentificacao = ConfiguracaoFireBase.getFireBaseAutencicacao();
        autentificacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {




                    Toast.makeText(CadastroActivity.this, "sucesso cadastro", Toast.LENGTH_LONG).show();


                    FirebaseUser usuarioFirebase = task.getResult().getUser();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();



                    //   autentificacao.signOut();
                    // finish();
                    abrirLoginUsuario();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuario(identificadorUsuario,usuario.getNome());


                } else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        senha.requestFocus();
                        senha.setError("Digite uma senha mais forte,contendo mais caracteres e com letras e numeros!");
                        erroExcecao = "Digite uma senha mais forte,contendo mais caracteres e com letras e numeros!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        email.requestFocus();
                        email.setError("Email invalido!");
                        erroExcecao = "Email invalido!";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Ja existe um usuario cadastrado com esse email!";
                        email.requestFocus();
                        email.setError("Ja existe um usuario cadastrado com esse email!");

                    } catch (Exception e) {
                        erroExcecao = "ERRO AO EFETUAR O CADASTRO!";

                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "FALHA " + erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void abrirLoginUsuario() {

        Intent intent= new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //VERIFICANDO SE O CAMPO ESTA VAZIO USANDO METODO PADRAO DO ANDROID
    public boolean isCampoVazio(String valor) {

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());


        return resultado;

    }

    @Override
    public void onBackPressed() {
        vibrar();
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("     Você esta saindo");
        //define a mensagem
        builder.setMessage("  Tem certeza que quer fazer isso?");
        //define um botão como positivo
        //builder.setIcon(R.drawable.botservice);
        //define um botão como negativo.
        builder.setNegativeButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.setPositiveButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        //cria o AlertDialog
        AlertDialog alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long mili = 100;
        rr.vibrate(mili);


    }

    private void validaCampos() {
        String nomes = nome.getText().toString();
        String emails = email.getText().toString();
        String senhas = senha.getText().toString();
        // Boolean res = false;
        if (isCampoVazio(nomes)) {
            nome.requestFocus();
            nome.setError("Preencha o campo nome!!");

        } else if (isCampoVazio(emails)) {
            email.requestFocus();
            email.setError("Preencha o campo email!!");

        } else if (isCampoVazio(senhas)) {

            senha.requestFocus();
            senha.setError("Preencha o campo senha!!");

        }


    }
}
