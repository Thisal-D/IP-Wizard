package com.example.ipwizard20;

public class Valid{
    public static  String[] split(String value,Character split_by, Integer size){
        String sub_value = "";
        Integer index = 0;
        String[] str_split = new String[size];
        for (Integer i=0; i<value.length(); i++) {
            if (value.charAt(i) == '.') {
                str_split[index] = sub_value;
                index++;
                sub_value = "";
            }
            else {
                sub_value += value.charAt(i);
            }
        }
        str_split[index] = sub_value;
        return  str_split;
    }

    public static Boolean ip_format_is_valid(String ip_address){
        Integer dots = 0;
        for (Integer i=0; i<ip_address.length(); i++) {
            if (ip_address.charAt(i)=='.') {
                dots++;
            }
        }

        String[] ip_address_split_str = new String[4];
        try{
            ip_address_split_str = split(ip_address, '.',4);
        }
        catch (Exception e){
            return false;
        }
        
        Integer index=0;
        Integer[] ip_address_split_int  = new Integer[4];
        index = 0;
        if (dots==3){
            //ip_address_split_str = ip_address.split(".",4);
            for (Integer i=0; i<4; i++) {
                try{
                    Integer int_ip = Integer.parseInt(ip_address_split_str[i]);
                    ip_address_split_int[index] = int_ip;
                    index ++;
                }
                catch (Exception e){
                    return false;
                }
            }
        }
        else{
            return false;
        }
        for (Integer int_ip: ip_address_split_int ){
            if (int_ip>255 || int_ip<0){
                return false;
            }
        }
        if (ip_address_split_int[0]==127 || ip_address_split_int[0] < 1 || ip_address_split_int[0]>223){
            return false;
        }
        else{
            return true;
        }
    };

    public static Boolean is_valid_subnet_mask_value(Integer value){
        Integer[] valid_subnet_mask_values = {255,254,252,248,240,224,192,128,0};
        for (Integer valid_value:valid_subnet_mask_values){
            if (valid_value.equals(value)){
                return true;
            }
        }
        return false;
    }

    public static Boolean is_valid_subnet_mask(String subnet_mask, String ip_class){
        Integer dots = 0;

        for (Integer i=0; i<subnet_mask.length(); i++) {
            if (subnet_mask.charAt(i)=='.') {
                dots++;
            }
        }
        String[] subnet_mask_split_str = new String[4];
        try{
            subnet_mask_split_str = split(subnet_mask, '.',4);
        }
        catch (Exception e){
            return false;
        }
        Integer index=0;
        Integer[] subnet_mask_split_int  = new Integer[4];
        index = 0;
        if (dots==3){
            for (Integer i=0; i<4; i++) {
                try{
                    Integer int_sub_mask = Integer.parseInt(subnet_mask_split_str[i]);
                    subnet_mask_split_int[index] = int_sub_mask;
                    index ++;
                }
                catch (Exception e){
                    return false;
                }
            }
        }
        else{
            return false;
        }
        for (Integer int_sub_mask: subnet_mask_split_int ){
            if (int_sub_mask>255 || int_sub_mask<0){
                return false;
            }
        }

        if (ip_class.equals("C")){
            if (subnet_mask_split_int[0]==255 && subnet_mask_split_int[1]==255 && subnet_mask_split_int[2]==255){
                if (!(is_valid_subnet_mask_value(subnet_mask_split_int[3]))){
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if (ip_class.equals("B")) {
            if (subnet_mask_split_int[0]==255 && subnet_mask_split_int[1]==255) {
                if (subnet_mask_split_int[2]==255){
                    if (!is_valid_subnet_mask_value(subnet_mask_split_int[3])){
                        return false;
                    }
                }
                else if (!(is_valid_subnet_mask_value(subnet_mask_split_int[2]) && subnet_mask_split_int[3]==0)){
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if (ip_class.equals("A")){
            if (subnet_mask_split_int[0]==255){
                if (subnet_mask_split_int[1]==255){
                    if (subnet_mask_split_int[2]==255){
                        if (!is_valid_subnet_mask_value(subnet_mask_split_int[3])){
                            return  false;
                        }
                    }
                    else if (!(is_valid_subnet_mask_value(subnet_mask_split_int[2]) && subnet_mask_split_int[3]==0)){
                        return false;
                    }
                }
                else if (!(is_valid_subnet_mask_value(subnet_mask_split_int[1]) && subnet_mask_split_int[2]==0 && subnet_mask_split_int[3]==0)){
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }

        return  true;
    }

    public static Boolean is_valid_subnet_count(String str_subnet_count, Integer max_subnet_count){
        Integer subnet_count ;
        try {
            subnet_count = Integer.parseInt(str_subnet_count);
        }
        catch (Exception e){
            return false;
        }
        if (!(subnet_count<=max_subnet_count) || !(subnet_count>0)){
            return false;
        }
        return true;
    };

    public static Boolean is_valid_host_count(String str_host_count, Integer max_host_count){
        Integer host_count ;
        try {
            host_count = Integer.parseInt(str_host_count);
        }
        catch (Exception e){
            return false;
        }
        if (!(host_count+2<=max_host_count) || !(host_count>0)){
            return false;
        }
        return true;
    };

    public static  String get_ip_class(String ip_address){
        Integer ip1 = Integer.parseInt(split(ip_address,'.',4 )[0]);
        if (ip1>191){
            return "C";
        } else if (ip1>127) {
            return "B";
        }
        else{
            return "A";
        }
    };

    public static  boolean is_valid_ip_range_count(String str_range_count){
        Integer int_range_count = 0;
        try{
              Integer.parseInt(str_range_count);
        }
        catch (Exception e){
            return false;
        }

        return  true;
    }

}
