package com.example.ipwizard20;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;


public class SettingsActivity extends AppCompatActivity {
    ImageButton back_btn;
    Button save_btn;
    EditText ip_range_count_input_text;
    TextWatcher ip_range_count_input_text_watcher;

    String ip_range_count, old_ip_range_count;
    String SETTINGS_DIRECTORY, SETTINGS_FILE_NAME;


    public  String get_ip_ranges_display_count(){
        try{
            File settings_file = new File(SETTINGS_DIRECTORY,SETTINGS_FILE_NAME);
            //checking files is not exists and then create new file
            // write 100 & return 100 because 100 is the default i suggest
            if (!settings_file.exists()){
                settings_file.createNewFile();
                FileWriter settings_file_writer = new FileWriter(settings_file);
                settings_file_writer.write("100");
                settings_file_writer.close();
                return "100";
            }
            else{
                Scanner settings_file_reader = new Scanner(settings_file);
                return settings_file_reader.nextLine();
            }

        }
        catch (Exception e){
            //System.out.println("ERROR : "+e);
            return "100";
        }
    }

    public void save_settings(){
        ip_range_count = ip_range_count_input_text.getText().toString();
        try{
            //checking files is not exists and then create new file
            //System.out.println("DIRECTORY : "+settings_path);
            //read ol
            File settings_file = new File(SETTINGS_DIRECTORY,SETTINGS_FILE_NAME);
            if (!ip_range_count.equals(old_ip_range_count)){
                FileWriter settings_file_writer = new FileWriter(settings_file);
                settings_file_writer.write(ip_range_count);
                settings_file_writer.close();
                restart();
            }
            else{
                back();
            }
        }
        catch (Exception e){
            //System.out.println("ERROR : "+e);
        }
    }

    public void restart(){
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void back(){
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back_btn = findViewById(R.id.backButton);
        save_btn = findViewById(R.id.saveButton);
        ip_range_count_input_text = findViewById(R.id.ipRangesCountInputText);

        SETTINGS_DIRECTORY = getFilesDir().toString();
        SETTINGS_FILE_NAME = "settings.txt";

        old_ip_range_count = get_ip_ranges_display_count();
        ip_range_count_input_text.setText(get_ip_ranges_display_count());

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_settings();
            }
        });


        ip_range_count_input_text_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                save_btn.setEnabled(Valid.is_valid_ip_range_count(ip_range_count_input_text.getText().toString()));
            }
        };

        ip_range_count_input_text.addTextChangedListener(ip_range_count_input_text_watcher);
    }
}