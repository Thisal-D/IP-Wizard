package com.example.ipwizard20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {


    Integer usable_root_width,usable_root_height;
    ConstraintLayout section_1, section_2, section_3, section_4;
    TableLayout ip_range_output_table ;

    ScrollView ip_range_output_scrollview ;

    EditText ip_address_input_text, host_count_input_text, subnet_count_input_text,subnet_mask_input_text ;
    TextView ip_address_count_output_text, host_count_output_text,
            subnet_mask_output_text, subnet_count_output_text,
            available_ip_address_count_output_text ;
    String ip_address_hint, host_count_hint, subnet_count_hint, subnet_mask_hint, ip_class;
    Integer class_a_ips_count, class_b_ips_count, class_c_ips_count, max_ips_count;
    ColorStateList default_text_color ;

    Boolean is_ip_valid ,is_host_count_valid, is_subnet_count_valid, is_subnet_mask_valid;

    String ip_address, subnet_mask;
    Integer req_host_count, req_subnet_count;


    private void set_enabled(EditText widget,String hint){
        widget.setEnabled(true);
        widget.setHint(hint);
    }
    private void set_disabled(EditText widget,String hint){
        widget.setEnabled(false);
        widget.setHint(hint);
    }


    /*TableLayout tl = (TableLayout) findViewById(R.id.ipAddressRangeTableOutput);*/

    /*for(Integer i=0; i<50; i++){
    TableRow tr_head = new TableRow(this);
    tr_head.setId(10);
    tr_head.setBackgroundColor(Color.GRAY);        // part1


    TextView label_hello = new TextView(this);
    label_hello.setId(20);
    label_hello.setText("HELLO");
    label_hello.setTextColor(Color.WHITE);          // part2
    label_hello.setPadding(5, 5, 5, 5);
    tr_head.addView(label_hello);// add the column to the table row here

    TextView label_android = new TextView(this);    // part3
    label_android.setId(21);// define id that must be unique
    label_android.setText("ANDROID..!!"); // set the text for the header
    label_android.setTextColor(Color.WHITE); // set the color
    label_android.setPadding(5, 5, 5, 5); // set the padding (if required)
    tr_head.addView(label_android); // add the column to the table row here
    tl.addView(tr_head);}*/

    private void display_ip_range(String subnet_mask_generated){
        Integer array_size = 100;
        String[][] ranges =  Calculate.generate_range(subnet_mask_generated, ip_class, ip_address);
        ip_range_output_table.removeAllViewsInLayout();


        Integer ip_width = (usable_root_width-100)/2;
        System.out.println("MAIN WIDTH  : "+usable_root_width);
        System.out.println("MAIN WIDTH  : "+ip_width);
        System.out.println("MAIN WIDTH  : "+"------------------------------");
        for (Integer index=0; index<array_size; index++){
            if (ranges[index][0]==null) break;
            TableRow tr = new TableRow(this);
            tr.setMinimumWidth(ip_range_output_table.getWidth());
            tr.setId(index);

            TextView start_ip = new TextView(this);
            start_ip.setText(ranges[index][0].toString());
            start_ip.setTextSize(16);

            start_ip.setTypeface(Typeface.DEFAULT_BOLD);
            start_ip.setPadding(0,0,50,0);
            start_ip.setWidth(ip_width);
            //start_ip.setBackgroundColor(Color.rgb(255,0,0));
            start_ip.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            TextView dash = new TextView(this);
            dash.setText("-");
            dash.setWidth(100);
            dash.setTextSize(16);

            dash.setPadding(0,0,0,0);
            dash.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //dash.setBackgroundColor(Color.rgb(0,255,0));

            TextView end_ip = new TextView(this);
            end_ip.setText(ranges[index][1].toString());
            end_ip.setTypeface(Typeface.DEFAULT_BOLD);
            end_ip.setTextSize(16);
            end_ip.setPadding(50,0,0,0);
            end_ip.setWidth(ip_width);
            end_ip.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            //end_ip.setBackgroundColor(Color.rgb(0,0,255));


            tr.addView(start_ip);
            tr.addView(dash);
            tr.addView(end_ip);

            ip_range_output_table.addView(tr);
        }
    }

    private void calculate(){
        System.out.println("VALIDITY CHECK address IS VALID : "+is_ip_valid);
        System.out.println("VALIDITY CHECK host IS VALID : "+is_host_count_valid);
        System.out.println("VALIDITY CHECK Subnet count IS VALID : "+is_subnet_count_valid);
        System.out.println("VALIDITY CHECK Subnet mask IS VALID : "+is_subnet_mask_valid);
        String subnet_mask_generated = "";
        Integer net_bits_count = 0;
        Integer host_bit_count = 0;
        Integer[] net_host_bits = new Integer[2];
        Double ip_address_count_generated = 0.0 ;
        Double host_count_generated = 0.0 ;
        Double subnet_count_generated = 0.0 ;
        if (is_subnet_count_valid || is_host_count_valid || is_subnet_mask_valid ){
            System.out.println("STATUS : HERE1");
            if (is_subnet_count_valid) net_host_bits = Calculate.get_net_bits__host_bits(ip_class, req_subnet_count, -1);
            System.out.println("STATUS : HERE2");
            if (is_host_count_valid) net_host_bits = Calculate.get_net_bits__host_bits(ip_class, -1, req_host_count+2);
            System.out.println("STATUS : HERE3");
            if (is_subnet_mask_valid) net_host_bits = Calculate.get_net_bits__host_bits(subnet_mask, ip_class);
            System.out.println("STATUS : HERE4");
            net_bits_count = net_host_bits[0];
            host_bit_count = net_host_bits[1];
            System.out.println("STATUS : HERE5");
            if (!is_subnet_mask_valid)subnet_mask_generated = Calculate.generate_subnet_mask(ip_class, net_bits_count, host_bit_count);
            else subnet_mask_generated = subnet_mask;
            System.out.println("STATUS : HERE6");
            subnet_count_generated = Math.pow(2.0,net_bits_count) ;
            ip_address_count_generated = Math.pow(2.0,host_bit_count);
            host_count_generated = ip_address_count_generated-2;

            subnet_count_output_text.setText(String.valueOf(subnet_count_generated.intValue()));
            ip_address_count_output_text.setText(String.valueOf(ip_address_count_generated.intValue()));
            host_count_output_text.setText(String.valueOf(host_count_generated.intValue()));
            subnet_mask_output_text.setText(subnet_mask_generated);

            display_ip_range(subnet_mask_generated);

        }
        else{
            subnet_count_output_text.setText("0");
            ip_address_count_output_text.setText("0");
            host_count_output_text.setText("0");
            subnet_mask_output_text.setText("0.0.0.0");
        }
    };

    private void validate_subnet_count(){
        String str_subnet_count;
        str_subnet_count = subnet_count_input_text.getText().toString();
        if (!str_subnet_count.equals("")) {
            set_disabled(host_count_input_text,"-");
            set_disabled(subnet_mask_input_text,"-");
            is_subnet_count_valid = Valid.is_valid_subnet_count(str_subnet_count, max_ips_count);
            if (is_subnet_count_valid){
                subnet_count_input_text.setTextColor(default_text_color);
                req_subnet_count = Integer.parseInt(str_subnet_count);
                calculate();
            }
            else{
                subnet_count_input_text.setTextColor(Color.argb(255,255,0,0));
                calculate();
            }
        }
        else {
            set_enabled(host_count_input_text, host_count_hint);
            set_enabled(subnet_mask_input_text, subnet_mask_hint);
            is_subnet_count_valid = false;
            calculate();
        }
    }

    private void validate_host_count(){
        String str_host_count;
        str_host_count = host_count_input_text.getText().toString();
        if (!str_host_count.equals("")) {
            set_disabled(subnet_count_input_text,"-");
            set_disabled(subnet_mask_input_text,"-");
            is_host_count_valid = Valid.is_valid_host_count(str_host_count, max_ips_count);
            if (is_host_count_valid){
                host_count_input_text.setTextColor(default_text_color);
                req_host_count = Integer.parseInt(str_host_count);
                calculate();
            }
            else{
                host_count_input_text.setTextColor(Color.argb(255,255,0,0));
                calculate();
            }
        }
        else {
            set_enabled(subnet_count_input_text, subnet_count_hint);
            set_enabled(subnet_mask_input_text, subnet_mask_hint);
            is_host_count_valid = false;
            calculate();
        }
    }

    private void validate_subnet_mask(){
        String str_subnet_mask ;
        str_subnet_mask = subnet_mask_input_text.getText().toString();
        if (!str_subnet_mask.equals("")) {
            set_disabled(host_count_input_text,"-");
            set_disabled(subnet_count_input_text,"-");
            is_subnet_mask_valid = Valid.is_valid_subnet_mask(str_subnet_mask, ip_class);
            if (is_subnet_mask_valid){
                subnet_mask_input_text.setTextColor(default_text_color);
                subnet_mask = str_subnet_mask;
                calculate();
            }
            else{
                subnet_mask_input_text.setTextColor(Color.argb(255,255,0,0));
                calculate();
            }
        }
        else {
            set_enabled(host_count_input_text, host_count_hint);
            set_enabled(subnet_count_input_text, subnet_count_hint);
            is_subnet_mask_valid = false;
            calculate();
        }
    }

    private void validate_ip_address(){
        String str_ip_address;
        str_ip_address = ip_address_input_text.getText().toString();
        is_ip_valid = Valid.ip_format_is_valid(str_ip_address);
        if (is_ip_valid) {
            ip_address_input_text.setTextColor(default_text_color);
            ip_class = Valid.get_ip_class(str_ip_address) ;
            ip_address = str_ip_address;
            if (ip_class.equals("C")){
                max_ips_count = class_c_ips_count;
            }
            else if (ip_class.equals("B")) {
                max_ips_count = class_b_ips_count;
            }
            else {
                max_ips_count = class_a_ips_count;
            }
            calculate();
        }
        else{
            max_ips_count = 0;
            ip_address_input_text.setTextColor(Color.argb(255,255,0,0));
        }
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        usable_root_width = displayMetrics.widthPixels;
        usable_root_height = displayMetrics.heightPixels;

        section_1 = findViewById(R.id.section1);
        section_2 = findViewById(R.id.section2);
        section_3 = findViewById(R.id.section3);
        section_4 = findViewById(R.id.section4);
        System.out.println("MAX HEIGHT : "+section_1.getHeight());
        section_4.setMaxHeight((usable_root_height-(dpToPx(80)+dpToPx(195)+dpToPx(140) +dpToPx(10))));
        section_4.setMinHeight((usable_root_height-(dpToPx(80)+dpToPx(195)+dpToPx(140) +dpToPx(10))));
        //section_4.setBackgroundColor(Color.rgb(0,255,255));


        ip_address_hint = "192.168.0.0";
        host_count_hint = "62";
        subnet_count_hint = "4";
        subnet_mask_hint = "255.255.192.0";

        ip_range_output_table = findViewById(R.id.ipAddressRangeTableOutput);
        ip_address_input_text = findViewById(R.id.ipAddressTextInput);
        host_count_input_text = findViewById(R.id.hostCountTextInput);
        subnet_count_input_text = findViewById(R.id.subnetCountTextInput);
        subnet_mask_input_text = findViewById(R.id.subnetMaskTextInput);
        ip_address_count_output_text = findViewById(R.id.ipAddressesCountTextOutput);
        host_count_output_text = findViewById(R.id.hostCountTextOutput);
        subnet_mask_output_text = findViewById(R.id.subnetMaskTextOutput);
        subnet_count_output_text = findViewById(R.id.subnetCountTextOutput);
        available_ip_address_count_output_text = findViewById(R.id.availableIpsCountTextOutput3);
        ip_range_output_scrollview = findViewById(R.id.ipAddressRangesOutputScrollview);


        Double base = 2.0;
        Double exponent = 8.0 ;
        class_a_ips_count = 1677721;
        class_b_ips_count = 65536;
        class_c_ips_count = 256;
        max_ips_count = 0;
        ip_class = "N";

        default_text_color = ip_address_input_text.getTextColors();

        is_ip_valid = false;
        is_host_count_valid = false;
        is_subnet_count_valid = false;
        is_subnet_mask_valid = false;


        TextWatcher host_count_input_text_watcher = new TextWatcher() {
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


        // subnet count input

        TextWatcher subnet_count_input_text_watcher = new TextWatcher() {
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


        // subnet mask input
        TextWatcher subnet_mask_input_text_watcher =  new TextWatcher() {
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



        TextWatcher ip_address_input_text_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validate_ip_address();
                if (host_count_input_text.isEnabled())validate_host_count();
                if (subnet_count_input_text.isEnabled())validate_subnet_count();
                if (subnet_mask_input_text.isEnabled())validate_subnet_mask();
                if (is_ip_valid){
                    available_ip_address_count_output_text.setText(max_ips_count.toString());
                }
                else{
                    available_ip_address_count_output_text.setText("0");
                }

            }
        };
        ip_address_input_text.addTextChangedListener(ip_address_input_text_watcher);
    }

}