package com.example.ipwizard20;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.nio.file.FileSystems;



public class Calculate {

    public static  String get_formatted_address_by_class(String address, String ip_class){
        String[] split_ip =  Valid.split(address,'.',4);
        String formatted_address = "";
        Integer end = 0;
        if (ip_class=="C") end=3;
        if (ip_class=="B") end=2;
        if (ip_class=="A") end=1;
        for (Integer i=0; i<end; i++){
            formatted_address+=String.valueOf(Integer.parseInt(split_ip[i])) + ".";
        }
        formatted_address += str_multiply("0.",(4-end));
        formatted_address =   formatted_address.substring(0,formatted_address.length()-1);
        return  formatted_address;
    }

    public static  String get_formatted_address(String address){
        String formatted_address = "";
        String[] split_ip =  Valid.split(address,'.',4);
            for (Integer i=0; i<3; i++){
            //System.out.print(split_ip[i]+ ".");
            formatted_address+=String.valueOf(Integer.parseInt(split_ip[i])) + ".";
        }
        //System.out.println(split_ip[3]);
        formatted_address += String.valueOf(Integer.valueOf(split_ip[3])) ;
        return formatted_address;
    }

    public static Integer twos_power(Integer value){
        Double base = 2.0;
        Double exponent = 0.0;
        Double total = Math.pow(base,exponent);
        while (total.intValue() < value){
            exponent += 1.0;
            total = Math.pow(base,exponent);
        }
        return exponent.intValue();
    }

    public static String str_multiply(String value,Integer multiply){
        String multiply_value = "";
        for (int i=0; i<multiply; i++){
            multiply_value += value;
        }
        return  multiply_value;
    }

    public static String binary_to_decimal(String bin_val){
        Double exponent = 2.0;
        Double power = 0.0;
        Double decimal =  0.0;
        for (Integer i=bin_val.length()-1; i>=0; i--){
            decimal += Math.pow(exponent,power) * Double.valueOf(bin_val.substring(i,i+1));
            power += 1.0;
        }
        return String.valueOf(decimal.intValue());
    }

    public static String decimal_to_binary(Integer number){

        String binary_val = "";
        while (number>0){
            binary_val = String.valueOf(number%2)  +  binary_val;
            number = (int)number/2;
        }
        return binary_val;
    }


    public static Integer generate_ip_address_count(String subnet_mask){
        Double exponent = 0.0;
        for (Integer i=9; i< subnet_mask.length(); i++){
            if (subnet_mask.charAt(i)=='0'){
                exponent++;
            }
        }
        Double ip_address_count = Math.pow(2.0,exponent);
        return ip_address_count.intValue() ;
    }

    public static Integer generate_subnets_count(String subnet_mask){
        Double exponent = 0.0;
        for (Integer i=9; i< subnet_mask.length(); i++){
            if (subnet_mask.charAt(i)=='1'){
                exponent++;
            }
        }
        Double sub_nets_count = Math.pow(2.0,exponent);
        return sub_nets_count.intValue() ;
    }


    public static String[][] generate_range(String subnet_mask, String ip_class, String ip_address, Integer array_size){
        Double block_size_double = 0.0;
        Integer block_bits = 0;
        Integer block_get = 1;
        Integer block_size = 0;
        String[] subnet_values = new String[4];
        String binary_val = "";
        subnet_values = Valid.split(subnet_mask,'.',4);
        for(Integer i=0; i<4; i++){
            binary_val = decimal_to_binary(Integer.valueOf(subnet_values[i]));
            binary_val = binary_val + str_multiply("0",8-binary_val.length());
            for (Integer i2=0; i2<binary_val.length(); i2++){
                if(binary_val.charAt(i2) == '0') block_bits++;
            }
            if (!(block_bits==0)){
                break;
            }
            block_get ++;
        }
       
        block_size_double =  Math.pow(2,block_bits);
        block_size = block_size_double.intValue();
        String[][] ip_range = new String[array_size][2];

        String[] ip_address_values = Valid.split(ip_address,'.',4);
        String start_ip = "";
        String end_ip = "";
        String ip_temp = "";
        Integer index = 0 ;
        if (ip_class.equals("C")) {
            ip_temp = ip_address_values[0] + "." + ip_address_values[1] + "." + ip_address_values[2];
            for (Integer ip4=0; ip4<256; ip4+=block_size){
                if (index>=array_size)break;
                start_ip = ip_temp + "." + String.valueOf(ip4);
                end_ip = ip_temp + "." + String.valueOf(ip4+block_size-1);
                ip_range[index] = new String[]{start_ip,end_ip};
                index ++;
            }
        }
        if (ip_class.equals("B")) {
            ip_temp = ip_address_values[0] + "." + ip_address_values[1] ;

            if(block_get==3){
                for (Integer ip3=0; ip3<256; ip3+=block_size){
                    if (index>=array_size)break;
                    start_ip = ip_temp + "." + String.valueOf(ip3) + "." + "0";
                    end_ip = ip_temp + "." + String.valueOf(ip3+block_size-1) + "." + "255";
                    ip_range[index] = new String[]{start_ip,end_ip};
                    index ++;
                }
            }
            if(block_get==4) {
                for (Integer ip3=0; ip3 < 256; ip3++) {
                    if (index>=array_size) break;
                    for (Integer ip4=0; ip4<256 ; ip4+=block_size){
                        if (index>=array_size) break;
                        start_ip = ip_temp + "." + String.valueOf(ip3) + "." + String.valueOf(ip4);
                        end_ip = ip_temp + "." + String.valueOf(ip3) + "." + String.valueOf(ip4+block_size-1);
                        ip_range[index] = new String[]{start_ip,end_ip};
                        index ++;
                    }
                }
            }
        }
        if (ip_class.equals("A")){
            ip_temp = ip_address_values[0];
            if (block_get==2){
                for (Integer ip2=0; ip2<256; ip2+=block_size){
                    if (index>=array_size) break;
                    start_ip = ip_temp + "." + String.valueOf(ip2) + "." + "0" + "." + "0";
                    end_ip = ip_temp + "." + String.valueOf(ip2+block_size-1) + "." + "255" + "." + "255";
                    ip_range[index] = new String[]{start_ip,end_ip};
                    index ++;
                }
            }
            if (block_get==3){
                for (Integer ip2=0; ip2<256; ip2++){
                    if (index>=array_size) break;
                    for (Integer ip3=0; ip3<256; ip3+=block_size){
                        if (index>=array_size) break;
                        start_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3) + "." + "0";
                        end_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3+block_size-1) + "." + "255";
                        ip_range[index] = new String[]{start_ip,end_ip};
                        index ++;
                    }
                }
            }
            if (block_get==4){
                for (Integer ip2=0; ip2<256; ip2++){
                    if (index>=array_size) break;
                    for (Integer ip3=0; ip3<256; ip3++){
                        if (index>=array_size) break;
                        for (Integer ip4=0; ip4<256; ip4+=block_size ){
                            if (index>=array_size) break;
                            start_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3) + "." + String.valueOf(ip4);
                            end_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3) + "." + String.valueOf(ip4+block_size-1);
                            ip_range[index] = new String[]{start_ip,end_ip};
                            index ++;
                        }
                    }
                }
            }

        }
        return ip_range;
    }


    public static Integer[] get_net_bits__host_bits(String ip_class, Integer subnets, Integer hosts){
        Integer subnet_bits = 0;
        Integer host_bits = 0;
        if (ip_class.equals("C")) {
            if (!subnets.equals(-1)) {
                subnet_bits = twos_power(subnets);
                host_bits = 8 - subnet_bits;
            } else {
                host_bits = twos_power(hosts);
                subnet_bits = 8 - host_bits;
            }
        }
        else if (ip_class.equals("B")) {
            if (!subnets.equals(-1)) {
                subnet_bits = twos_power(subnets);
                host_bits = 16 - subnet_bits;
            } else {
                host_bits = twos_power(hosts);
                subnet_bits = 16 - host_bits;
            }
        }
        else if (ip_class.equals("A")) {
            if (!subnets.equals(-1)) {
                subnet_bits = twos_power(subnets);
                host_bits = 24 - subnet_bits;
            } else {
                host_bits = twos_power(hosts);
                subnet_bits = 24 - host_bits;
            }
        }
        Integer[] sub_host_bits = new Integer[]{subnet_bits,host_bits};
        return sub_host_bits;
    }


    public static Integer[] get_net_bits__host_bits(String subnet_mask,String ip_class){
        Integer start = 0;
        if (ip_class.equals("A")) start = 1;
        if (ip_class.equals("B")) start = 2;
        if (ip_class.equals("C")) start = 3;
        String[] subnet_values = new String[4];
        String binary_val = "";
        Integer subnet_bits = 0;
        Integer host_bits = 0;
        subnet_values = Valid.split(subnet_mask,'.',4);
        for(Integer i=start; i<4; i++){
            binary_val = decimal_to_binary(Integer.valueOf(subnet_values[i]));
            binary_val = binary_val + str_multiply("0",8-binary_val.length());
            for (Integer i2=0; i2<binary_val.length(); i2++){
                if(binary_val.charAt(i2) == '1') subnet_bits++;
                else if(binary_val.charAt(i2) == '0') host_bits++;
            }
        }
        Integer[] sub_host_bits = new Integer[]{subnet_bits,host_bits};
        return sub_host_bits;
    }


    public static String generate_subnet_mask(String ip_class, Integer subnet_bits, Integer host_bits){
        String sub1 = "255";
        String sub2 = "";
        String sub3 = "";
        String sub4 = "";

        String sub2_bin, sub3_bin, sub4_bin ;
        sub2_bin = "";
        sub3_bin = "";
        sub4_bin = "";
        if (ip_class.equals("C")){
            sub2 = "255";
            sub3 = "255";
            sub4_bin = str_multiply("1",subnet_bits) + str_multiply("0",host_bits);
            sub4 = binary_to_decimal(sub4_bin);
        }
        else if (ip_class.equals("B")) {
            sub2 = "255";
            if (subnet_bits >= 8) {
                sub3_bin = str_multiply("1", 8);
                sub4_bin = str_multiply("1", subnet_bits - 8) + str_multiply("0", host_bits);
                sub3 = binary_to_decimal(sub3_bin);
                sub4 = binary_to_decimal(sub4_bin);
            }
            else {
                sub3_bin = str_multiply("1", subnet_bits) + str_multiply("0", 8 - subnet_bits);
                sub4_bin = str_multiply("0", subnet_bits);
                sub3 = binary_to_decimal(sub3_bin);
                sub4 = binary_to_decimal(sub4_bin);
            }
        }
        else if (ip_class.equals("A")){
            if (subnet_bits>=16){
                sub2_bin = str_multiply("1", 8);
                sub3_bin = str_multiply("1", 8);
                sub4_bin = str_multiply("1", subnet_bits-16) + str_multiply("0",host_bits);
                sub2 = binary_to_decimal(sub2_bin);
                sub3 = binary_to_decimal(sub3_bin);
                sub4 = binary_to_decimal(sub4_bin);
            }
            else if (subnet_bits>=8){
                sub2_bin = str_multiply("1", 8);
                sub3_bin = str_multiply("1",subnet_bits-8) + str_multiply("0",host_bits-8);
                sub4_bin = str_multiply("0",8);
                sub2 = binary_to_decimal(sub2_bin);
                sub3 = binary_to_decimal(sub3_bin);
                sub4 = binary_to_decimal(sub4_bin);
            }
            else{
                sub2_bin = str_multiply("1",subnet_bits) + str_multiply("0",8-subnet_bits);
                sub3_bin = str_multiply("0",8);
                sub4_bin = str_multiply("0",8);
                sub2 = binary_to_decimal(sub2_bin);
                sub3 = binary_to_decimal(sub3_bin);
                sub4 = binary_to_decimal(sub4_bin);
            }
        }
        return (sub1+"."+sub2+"."+sub3+"."+sub4);
    }

    /*
    public static void generate_range_and_display(String subnet_mask, String ip_class, String ip_address, Integer array_size,
                                                  TextView[][] ip_ranges_display_text_views, TableLayout ip_range_output_table, TableRow[] ip_ranges_display_table_rows){
        Double block_size_double = 0.0;
        Integer block_bits = 0;
        Integer block_get = 1;
        Integer block_size = 0;
        String[] subnet_values = new String[4];
        String binary_val = "";
        subnet_values = Valid.split(subnet_mask,'.',4);
        for(Integer i=0; i<4; i++){
            binary_val = decimal_to_binary(Integer.valueOf(subnet_values[i]));
            binary_val = binary_val + str_multiply("0",8-binary_val.length());
            for (Integer i2=0; i2<binary_val.length(); i2++){
                if(binary_val.charAt(i2) == '0') block_bits++;
            }
            if (!(block_bits==0)){
                break;
            }
            block_get ++;
        }
       
        block_size_double =  Math.pow(2,block_bits);
        block_size = block_size_double.intValue();
        //String[][] ip_range = new String[array_size][2];

        String[] ip_address_values = Valid.split(ip_address,'.',4);
        String start_ip = "";
        String end_ip = "";
        String ip_temp = "";
        Integer index = 0 ;
        if (ip_class.equals("C")) {
            ip_temp = ip_address_values[0] + "." + ip_address_values[1] + "." + ip_address_values[2];
            for (Integer ip4=0; ip4<256; ip4+=block_size){
                if (index>=array_size)break;
                start_ip = ip_temp + "." + String.valueOf(ip4);
                end_ip = ip_temp + "." + String.valueOf(ip4+block_size-1);
                ip_ranges_display_text_views[index][0].setText(start_ip);
                ip_ranges_display_text_views[index][2].setText(end_ip);
                ip_range_output_table.addView(ip_ranges_display_table_rows[index]);
                //ip_range[index] = new String[]{start_ip,end_ip};
                index ++;
            }
        }
        if (ip_class.equals("B")) {
            ip_temp = ip_address_values[0] + "." + ip_address_values[1] ;

            if(block_get==3){
                for (Integer ip3=0; ip3<256; ip3+=block_size){
                    if (index>=array_size)break;
                    start_ip = ip_temp + "." + String.valueOf(ip3) + "." + "0";
                    end_ip = ip_temp + "." + String.valueOf(ip3+block_size-1) + "." + "255";
                    ip_ranges_display_text_views[index][0].setText(start_ip);
                    ip_ranges_display_text_views[index][2].setText(end_ip);
                    ip_range_output_table.addView(ip_ranges_display_table_rows[index]);
                    //ip_range[index] = new String[]{start_ip,end_ip};
                    index ++;
                }
            }
            if(block_get==4) {
                for (Integer ip3=0; ip3 < 256; ip3++) {
                    if (index>=array_size) break;
                    for (Integer ip4=0; ip4<256 ; ip4+=block_size){
                        if (index>=array_size) break;
                        start_ip = ip_temp + "." + String.valueOf(ip3) + "." + String.valueOf(ip4);
                        end_ip = ip_temp + "." + String.valueOf(ip3) + "." + String.valueOf(ip4+block_size-1);
                        ip_ranges_display_text_views[index][0].setText(start_ip);
                        ip_ranges_display_text_views[index][2].setText(end_ip);
                        ip_range_output_table.addView(ip_ranges_display_table_rows[index]);
                        //ip_range[index] = new String[]{start_ip,end_ip};
                        index ++;
                    }
                }
            }
        }
        if (ip_class.equals("A")){
            ip_temp = ip_address_values[0];
            if (block_get==2){
                for (Integer ip2=0; ip2<256; ip2+=block_size){
                    if (index>=array_size) break;
                    start_ip = ip_temp + "." + String.valueOf(ip2) + "." + "0" + "." + "0";
                    end_ip = ip_temp + "." + String.valueOf(ip2+block_size-1) + "." + "255" + "." + "255";
                    ip_ranges_display_text_views[index][0].setText(start_ip);
                    ip_ranges_display_text_views[index][2].setText(end_ip);
                    ip_range_output_table.addView(ip_ranges_display_table_rows[index]);
                    //ip_range[index] = new String[]{start_ip,end_ip};
                    index ++;
                }
            }
            if (block_get==3){
                for (Integer ip2=0; ip2<256; ip2++){
                    if (index>=array_size) break;
                    for (Integer ip3=0; ip3<256; ip3+=block_size){
                        if (index>=array_size) break;
                        start_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3) + "." + "0";
                        end_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3+block_size-1) + "." + "255";
                        ip_ranges_display_text_views[index][0].setText(start_ip);
                        ip_ranges_display_text_views[index][2].setText(end_ip);
                        ip_range_output_table.addView(ip_ranges_display_table_rows[index]);
                        //ip_range[index] = new String[]{start_ip,end_ip};
                        index ++;
                    }
                }
            }
            if (block_get==4){
                for (Integer ip2=0; ip2<256; ip2++){
                    if (index>=array_size) break;
                    for (Integer ip3=0; ip3<256; ip3++){
                        if (index>=array_size) break;
                        for (Integer ip4=0; ip4<256; ip4+=block_size ){
                            if (index>=array_size) break;
                            start_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3) + "." + String.valueOf(ip4);
                            end_ip = ip_temp + "." + String.valueOf(ip2) + "." + String.valueOf(ip3) + "." + String.valueOf(ip4+block_size-1);
                            ip_ranges_display_text_views[index][0].setText(start_ip);
                            ip_ranges_display_text_views[index][2].setText(end_ip);
                            ip_range_output_table.addView(ip_ranges_display_table_rows[index]);
                            //ip_range[index] = new String[]{start_ip,end_ip};
                            index ++;
                        }
                    }
                }
            }
        }
    }*/
}
