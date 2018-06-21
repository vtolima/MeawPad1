package vto.com.br.meawpad;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Atributos do XML, do Firebase e a lista de TAGs
    private EditText tagEditText;
    private EditText TextEditText;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference MeawPadReference;

    private List<Tag> tags;

//---------------------Métodos do ciclo de vida------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextEditText = findViewById(R.id.TextEditText);
        tagEditText = findViewById(R.id.tagEditText);


        tags = new ArrayList<Tag>();

        configuraFirebase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaTag();
            }
        });


        TextEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                salvaTag();
                return false;
            }
        });

        TextEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                salvaTag();
            }
        });

        tagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextEditText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tagEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String chave = tagEditText.getEditableText().toString();
                if (chave.contentEquals("")){
                    Toast.makeText(MainActivity.this, getString(R.string.falta_tag),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        salvaTag();
    }


    //---------------------Métodos da aplicação------------------

    private void configuraFirebase (){
        firebaseDatabase = FirebaseDatabase.getInstance();
        MeawPadReference = firebaseDatabase.getReference("meaw");
    }

    private void salvaTag(){
        String chave = tagEditText.getEditableText().toString();
        String conteudo = TextEditText.getEditableText().toString();

        if (chave.contentEquals("")){
            Toast.makeText(MainActivity.this, getString(R.string.falta_tag),
                    Toast.LENGTH_SHORT).show();
        } else{
            Tag tag = new Tag(chave, conteudo);
            MeawPadReference.child(chave).setValue(tag);
        }
    }

    private void buscaTag () {
        tagEventListener ();
    }

    private void tagEventListener (){
        final String chave = tagEditText.getEditableText().toString();
        MeawPadReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tags.clear();
                for (DataSnapshot json : dataSnapshot.getChildren()) {
                    Tag todas = json.getValue(Tag.class);
                    todas.setTag(json.getKey());
                    tags.add(todas);
                }
                tagArraySearch (chave);
                TextEditText.setSelection(TextEditText.getText().length());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tagArraySearch (String chave){
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getTag().equals(chave)) {
                String conteudo = tags.get(i).getConteudo();
                TextEditText.setText(conteudo);
                break;
            }
        }
    }


}
