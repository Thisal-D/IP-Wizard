package com.example.ipwizard20;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    // all the vars
    Integer usable_display_width,usable_display_height ;
    Float display_density;
    Integer CLASS_A_MAX_IPS_COUNT, CLASS_B_MAX_IPS_COUNT, CLASS_C_MAX_IPS_COUNT, max_ips_count;

    String Class_A_DEFAUL_SUBNET_MASK ,Class_B_DEFAUL_SUBNET_MASK ,Class_C_DEFAUL_SUBNET_MASK ;
    Boolean is_ip_valid ,is_host_count_valid, is_subnet_count_valid, is_subnet_mask_valid;
    String validated_ip_address, validated_subnet_mask;
    Integer req_host_count, req_subnet_count;
    String IP_ADDRESS_HINT, HOST_COUNT_HINT, SUBNET_COUNT_HINT, SUBNET_MASK_HINT;
    String   ip_class;
    Integer section_1_widget_height_dp, section_2_widget_height_dp, section_3_widget_height_dp, section_4_widget_height_px ,version_label_height_dp;
    String subnet_count, host_count, subnet_mask, ip_address;
    String calculated_subnet_mask ;
    Integer calculated_net_bits_count, calculated_host_bit_count;
    Double calculated_ip_address_count, calculated_subnet_count, calculated_host_count ;
    Integer[] net_and_host_bits_count = new Integer[2];
    Integer IP_RANGES_DISPLAY_COUNT ,IP_RANGES_TEXT_VIEW_WIDTH , SPLITTER_TEXT_VIEW_WIDTH;



    TableRow[] ip_ranges_display_table_rows ;
    TextView[][] ip_ranges_display_text_views ;
    //TextWatchers
    TextWatcher ip_address_input_text_watcher, subnet_mask_input_text_watcher, subnet_count_input_text_watcher, host_count_input_text_watcher;

    // All the widgets
    ConstraintLayout widget_section_1, widget_section_2, widget_section_3, widget_section_4;
    TableLayout ip_range_output_table ;
    ScrollView ip_range_output_scrollview;
    EditText ip_address_input_text, host_count_input_text, subnet_count_input_text, subnet_mask_input_text;
    TextView ip_address_count_output_text, host_count_output_text,
            subnet_mask_output_text, subnet_count_output_text,
            available_ip_address_count_output_text, default_subnet_mask_output_text, ip_class_output_text,
            ip_ranges_count_display_output_text;

    ColorStateList default_text_color;
    ImageButton setting_btn;

    String FILE_DIRECTORY, SETTINGS_FILE_NAME;


    //used to set disable input text boxes  edit text
    private void set_enabled(EditText widget, String hint) {
        widget.setEnabled(true);
        widget.setHint(hint);
    }

    //used to set disable input text boxes  edit text
    private void set_disabled(EditText widget, String hint) {
        widget.setEnabled(false);
        widget.setHint(hint);
    }

    // generate the ip ranges as array and display of it
    private void display_ip_range(String calculated_subnet_mask){
        String[][] ranges =  Calculate.generate_range(calculated_subnet_mask, ip_class, validated_ip_address, IP_RANGES_DISPLAY_COUNT);
        ip_range_output_table.removeAllViewsInLayout();
        for (int i=0; i<IP_RANGES_DISPLAY_COUNT; i++){
            if (ranges[i][0]==null) break;
            ip_ranges_display_text_views[i][0].setText(ranges[i][0]);
            ip_ranges_display_text_views[i][2].setText(ranges[i][1]);
            ip_range_output_table.addView(ip_ranges_display_table_rows[i]);
        }
        /*ip_range_output_table.removeAllViewsInLayout();
        Calculate.generate_range_and_display(calculated_subnet_mask, ip_class, validated_ip_address,
                IP_RANGES_DISPLAY_COUNT,ip_ranges_display_text_views, ip_range_output_table, ip_ranges_display_table_rows);*/
    }

    private void reset_outputs() {
        subnet_count_output_text.setText("0");
        ip_address_count_output_text.setText("0");
        host_count_output_text.setText("0");
        subnet_mask_output_text.setText("0.0.0.0");
        ip_range_output_table.removeAllViewsInLayout();
    }

    public Boolean set_history(String file_name) {
        File file = new File(FILE_DIRECTORY, file_name);
        try {
            if (!file.exists()) {
                return false;
            }
            Scanner read_file = new Scanner(file);
            String value_is = read_file.nextLine();
            String value = read_file.nextLine();
            read_file.close();
            if (value_is.equals("subnet_count")) subnet_count_input_text.setText(value);
            if (value_is.equals("host_count")) host_count_input_text.setText(value);
            if (value_is.equals("subnet_mask")) subnet_mask_input_text.setText(value);
            if (value_is.equals("ip_address")) ip_address_input_text.setText(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void save_history(String file_name, String value_is, String value) {
        File file = new File(FILE_DIRECTORY, file_name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter write_file = new FileWriter(file);
            write_file.write(value_is);
            write_file.write("\n");
            write_file.write(value);
            write_file.close();
        } catch (Exception e) {
        }
    }

    private void calculate() {
        if (is_subnet_count_valid || is_host_count_valid || is_subnet_mask_valid) {
            // calculate net_bits_count & host_bit_count
            if (is_subnet_count_valid)
                net_and_host_bits_count = Calculate.get_net_bits__host_bits(ip_class, req_subnet_count, -1);
            if (is_host_count_valid)
                net_and_host_bits_count = Calculate.get_net_bits__host_bits(ip_class, -1, req_host_count + 2);
            if (is_subnet_mask_valid)
                net_and_host_bits_count = Calculate.get_net_bits__host_bits(validated_subnet_mask, ip_class);
            // passing to to another var to  calculate net_bits_count & host_bit_count
            calculated_net_bits_count = net_and_host_bits_count[0];
            calculated_host_bit_count = net_and_host_bits_count[1];
            // check if subnet mask is valid if not we have to calculate it
            if (!is_subnet_mask_valid)calculated_subnet_mask = Calculate.generate_subnet_mask(ip_class, calculated_net_bits_count, calculated_host_bit_count);
            else calculated_subnet_mask = validated_subnet_mask;
            calculated_subnet_count = Math.pow(2.0,calculated_net_bits_count) ;
            calculated_ip_address_count = Math.pow(2.0,calculated_host_bit_count);
            if (calculated_ip_address_count<2) calculated_host_count=0.0;
            else calculated_host_count = calculated_ip_address_count-2;

            subnet_count_output_text.setText(String.valueOf(calculated_subnet_count.intValue()));
            ip_address_count_output_text.setText(String.valueOf(calculated_ip_address_count.intValue()));
            host_count_output_text.setText(String.valueOf(calculated_host_count.intValue()));
            subnet_mask_output_text.setText(calculated_subnet_mask);

            display_ip_range(calculated_subnet_mask);
        }
    }

    private void validate_subnet_count(){
        subnet_count = subnet_count_input_text.getText().toString();
        save_history("value.txt", "subnet_count", subnet_count);
        if (!subnet_count.equals("")) {
            set_disabled(host_count_input_text,"-");
            set_disabled(subnet_mask_input_text,"-");
            is_subnet_count_valid = Valid.is_valid_subnet_count(subnet_count, max_ips_count);
            if (is_subnet_count_valid){
                subnet_count_input_text.setTextColor(default_text_color);
                req_subnet_count = Integer.parseInt(subnet_count);
                calculate();
            }
            else{
                subnet_count_input_text.setTextColor(Color.argb(255,255,0,0));

                reset_outputs();
            }
        }
        else {
            set_enabled(host_count_input_text, HOST_COUNT_HINT);
            set_enabled(subnet_mask_input_text, SUBNET_MASK_HINT);
            is_subnet_count_valid = false;
            reset_outputs();
        }
    }

    private void validate_host_count(){
        host_count = host_count_input_text.getText().toString();
        save_history("value.txt", "host_count", host_count);
        if (!host_count.equals("")) {
            set_disabled(subnet_count_input_text,"-");
            set_disabled(subnet_mask_input_text,"-");
            is_host_count_valid = Valid.is_valid_host_count(host_count, max_ips_count);
            if (is_host_count_valid){
                host_count_input_text.setTextColor(default_text_color);
                req_host_count = Integer.parseInt(host_count);
                calculate();
            }
            else{
                host_count_input_text.setTextColor(Color.argb(255,255,0,0));
                reset_outputs();
            }
        }
        else {
            set_enabled(subnet_count_input_text, SUBNET_COUNT_HINT);
            set_enabled(subnet_mask_input_text, SUBNET_MASK_HINT);
            is_host_count_valid = false;
            reset_outputs();
        }
    }

    private void validate_subnet_mask(){
        subnet_mask = subnet_mask_input_text.getText().toString();
        save_history("value.txt", "subnet_mask", subnet_mask);
        if (!subnet_mask.equals("")) {
            set_disabled(host_count_input_text,"-");
            set_disabled(subnet_count_input_text,"-");
            is_subnet_mask_valid = Valid.is_valid_subnet_mask(subnet_mask, ip_class);
            if (is_subnet_mask_valid){
                subnet_mask_input_text.setTextColor(default_text_color);
                validated_subnet_mask = subnet_mask;
                set_formatted_subnet_mask();
                calculate();
            }
            else{
                subnet_mask_input_text.setTextColor(Color.argb(255,255,0,0));
                reset_outputs();
            }
        }
        else {
            set_enabled(host_count_input_text, HOST_COUNT_HINT);
            set_enabled(subnet_count_input_text, SUBNET_COUNT_HINT);
            is_subnet_mask_valid = false;
            reset_outputs();
        }
    }

    private void set_formatted_subnet_mask(){
        new RemoveTextWatcher(subnet_mask_input_text,subnet_mask_input_text_watcher).run();
        validated_subnet_mask = Calculate.get_formatted_address(validated_subnet_mask);
        subnet_mask_input_text.setText(validated_subnet_mask);
        subnet_mask_input_text.addTextChangedListener(subnet_mask_input_text_watcher);
    }
    private void set_formatted_ip_address(){
        new RemoveTextWatcher(ip_address_input_text,ip_address_input_text_watcher).run();
        validated_ip_address = Calculate.get_formatted_address_by_class(validated_ip_address,ip_class);
        ip_address_input_text.setText(validated_ip_address);
        ip_address_input_text.addTextChangedListener(ip_address_input_text_watcher);
    }

    private void set_ip_address_details(String ip_class, String ips_count, String default_subnet_mask){
       default_subnet_mask_output_text.setText(default_subnet_mask);
       available_ip_address_count_output_text.setText(ips_count);
       ip_class_output_text.setText(ip_class);
    }

    private void validate_ip_address(){
        ip_address = ip_address_input_text.getText().toString();
        save_history("ip_address.txt", "ip_address", ip_address);
        is_ip_valid = Valid.ip_format_is_valid(ip_address);
        if (is_ip_valid) {
            ip_address_input_text.setTextColor(default_text_color);
            ip_class = Valid.get_ip_class(ip_address) ;
            validated_ip_address = ip_address;
            if (ip_class.equals("C")){
                max_ips_count = CLASS_C_MAX_IPS_COUNT;
                set_ip_address_details("Class C", CLASS_C_MAX_IPS_COUNT.toString(), Class_C_DEFAUL_SUBNET_MASK);

            }
            else if (ip_class.equals("B")) {
                max_ips_count = CLASS_B_MAX_IPS_COUNT;
                set_ip_address_details("Class B", CLASS_B_MAX_IPS_COUNT.toString(), Class_B_DEFAUL_SUBNET_MASK);

            }
            else {
                max_ips_count = CLASS_A_MAX_IPS_COUNT;
                set_ip_address_details("Class A", CLASS_A_MAX_IPS_COUNT.toString(), Class_A_DEFAUL_SUBNET_MASK );

            }
            set_formatted_ip_address();
            if (subnet_mask_input_text.isEnabled())validate_subnet_mask();
            if (host_count_input_text.isEnabled())validate_host_count();
            if (subnet_count_input_text.isEnabled())validate_subnet_count();
            calculate();

        }
        else{
            reset_outputs();
            max_ips_count = 0;
            ip_address_input_text.setTextColor(Color.argb(255,255,0,0));
            if (subnet_mask_input_text.isEnabled())validate_subnet_mask();
            if (host_count_input_text.isEnabled())validate_host_count();
            if (subnet_count_input_text.isEnabled())validate_subnet_count();
            set_ip_address_details("None", "None", "None" );
        }
    }


    public void go_to_settings(){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public  int get_ip_ranges_display_count(){
        try{
            File settings_file = new File(FILE_DIRECTORY, SETTINGS_FILE_NAME);
            //checking files is not exists and then create new file
            // write 100 & return 100 because 100 is the default i suggest
            if (!settings_file.exists()){
                settings_file.createNewFile();
                FileWriter settings_file_writer = new FileWriter(settings_file);
                settings_file_writer.write("100");
                settings_file_writer.close();
                return 100;
            }
            else{
                Scanner settings_file_reader = new Scanner(settings_file);
                return Integer.parseInt(settings_file_reader.nextLine());
            }

        }
        catch (Exception e){
            //System.out.println("ERROR : "+e);
            return 100;
        }
    }

    //convert px to dp ,this uses when displaying ip ranges
    // calculate the section 4 height
    public  int pxToDp(int px) {
        return (int) (px / display_density);
    }

    public  int dpToPx(int dp) {
        return (int) (dp * display_density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get display width & height
        //this values gonna use when displaying scrollview to get size of text views
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        usable_display_width = displayMetrics.widthPixels;
        usable_display_height = displayMetrics.heightPixels;
        display_density = Resources.getSystem().getDisplayMetrics().density ;

        //get all widget using id
        widget_section_1 = findViewById(R.id.section1);
        widget_section_2 = findViewById(R.id.section2);
        widget_section_3 = findViewById(R.id.section3);
        widget_section_4 = findViewById(R.id.section4);
        ip_range_output_table = findViewById(R.id.ipAddressRangeTableOutput);
        ip_address_input_text = findViewById(R.id.ipAddressTextInput);
        host_count_input_text = findViewById(R.id.hostCountTextInput);
        subnet_count_input_text = findViewById(R.id.subnetCountTextInput);
        subnet_mask_input_text = findViewById(R.id.subnetMaskTextInput);
        ip_address_count_output_text = findViewById(R.id.ipAddressesCountTextOutput);
        host_count_output_text = findViewById(R.id.hostCountTextOutput);
        subnet_mask_output_text = findViewById(R.id.subnetMaskTextOutput);
        subnet_count_output_text = findViewById(R.id.subnetCountTextOutput);
        ip_range_output_scrollview = findViewById(R.id.ipAddressRangesOutputScrollview);
        available_ip_address_count_output_text = findViewById(R.id.availableIpAddressCountOutputText);
        default_subnet_mask_output_text = findViewById(R.id.defaulSubnetMaskOututText);
        ip_class_output_text = findViewById(R.id.ipClassTextOutput);
        setting_btn = findViewById(R.id.settingButton);
        ip_ranges_count_display_output_text = findViewById(R.id.ipRangesDisplayCountOutputText);

        //widget size
        section_1_widget_height_dp = 80;
        section_2_widget_height_dp = 195;
        section_3_widget_height_dp = 140;
        version_label_height_dp = 10;

        //calculate widget section_4 height and set it
        section_4_widget_height_px = usable_display_height-(dpToPx(section_1_widget_height_dp)+dpToPx(section_2_widget_height_dp)+dpToPx(section_3_widget_height_dp) +dpToPx(version_label_height_dp));
        widget_section_4.setMaxHeight(section_4_widget_height_px);
        widget_section_4.setMinHeight(section_4_widget_height_px);
        //widget_section_4.setBackgroundColor(Color.rgb(0,255,255));

        default_text_color = ip_address_input_text.getTextColors();


        FILE_DIRECTORY = getFilesDir().toString();
        SETTINGS_FILE_NAME = "settings.txt";
        //constant values
        IP_ADDRESS_HINT = "192.168.0.0";
        HOST_COUNT_HINT = "62";
        SUBNET_COUNT_HINT = "4";
        SUBNET_MASK_HINT = "255.255.192.0";
        CLASS_A_MAX_IPS_COUNT = 1677721;
        CLASS_B_MAX_IPS_COUNT = 65536;
        CLASS_C_MAX_IPS_COUNT = 256;
        Class_A_DEFAUL_SUBNET_MASK = "255.0.0.0";
        Class_B_DEFAUL_SUBNET_MASK = "255.255.0.0";
        Class_C_DEFAUL_SUBNET_MASK = "255.255.255.0";
        max_ips_count = 0;
        ip_class = "N";
        IP_RANGES_DISPLAY_COUNT = get_ip_ranges_display_count();
        ip_ranges_count_display_output_text.setText(IP_RANGES_DISPLAY_COUNT.toString());
        //System.out.println("IP RANGES COUNT : "+IP_RANGES_DISPLAY_COUNT);

        is_ip_valid = false;
        is_host_count_valid = false;
        is_subnet_count_valid = false;
        is_subnet_mask_valid = false;

        SPLITTER_TEXT_VIEW_WIDTH = 50;
        IP_RANGES_TEXT_VIEW_WIDTH =  (usable_display_width-SPLITTER_TEXT_VIEW_WIDTH*2)/2;
        ip_ranges_display_table_rows = new TableRow[IP_RANGES_DISPLAY_COUNT];
        ip_ranges_display_text_views = new TextView[IP_RANGES_DISPLAY_COUNT][3];

        //Pre Create widget for get more performance
        // used to display ranges
        for (int i=0;i <IP_RANGES_DISPLAY_COUNT; i++){
            ip_ranges_display_table_rows[i] = new TableRow(this);

            ip_ranges_display_text_views[i][0] = new TextView(this);
            ip_ranges_display_text_views[i][0].setTextSize(16);
            ip_ranges_display_text_views[i][0].setTypeface(Typeface.DEFAULT_BOLD);
            ip_ranges_display_text_views[i][0].setPadding(0,0,50,0);
            ip_ranges_display_text_views[i][0].setWidth(IP_RANGES_TEXT_VIEW_WIDTH);
            ip_ranges_display_text_views[i][0].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            ip_ranges_display_text_views[i][1] = new TextView(this);
            ip_ranges_display_text_views[i][1].setText("-");
            ip_ranges_display_text_views[i][1].setWidth(100);
            ip_ranges_display_text_views[i][1].setTextSize(16);
            ip_ranges_display_text_views[i][1].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            ip_ranges_display_text_views[i][2] = new TextView(this);
            ip_ranges_display_text_views[i][2].setTextSize(16);
            ip_ranges_display_text_views[i][2].setTypeface(Typeface.DEFAULT_BOLD);
            ip_ranges_display_text_views[i][2].setPadding(50,0,0,0);
            ip_ranges_display_text_views[i][2].setWidth(IP_RANGES_TEXT_VIEW_WIDTH);
            ip_ranges_display_text_views[i][2].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            ip_ranges_display_table_rows[i].addView(ip_ranges_display_text_views[i][0]);
            ip_ranges_display_table_rows[i].addView(ip_ranges_display_text_views[i][1]);
            ip_ranges_display_table_rows[i].addView(ip_ranges_display_text_views[i][2]);
        }



        // This Text watcher checking host count input box
        // if the host count  changes this is gonna check validity of the input
        host_count_input_text_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validate_host_count();
            }
        };
        host_count_input_text.addTextChangedListener(host_count_input_text_watcher);


        // This Text watcher checking subnet count input box
        // if the subnet count  changes this is gonna check validity of the input
        subnet_count_input_text_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validate_subnet_count();
            }
        };
        subnet_count_input_text.addTextChangedListener(subnet_count_input_text_watcher);


        // This Text watcher checking subnet mask input box
        // if the subnet mask changes this is gonna check validity  of the input
         subnet_mask_input_text_watcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validate_subnet_mask();
            }
        };
        subnet_mask_input_text.addTextChangedListener(subnet_mask_input_text_watcher);


        // This Text watcher checking ip address input box
        // if the ip address changes this is gonna check validity of the input
        ip_address_input_text_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validate_ip_address();
            }
        };
        ip_address_input_text.addTextChangedListener(ip_address_input_text_watcher);

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_settings();
            }
        });

        set_history("value.txt");
        set_history("ip_address.txt");
    }

}