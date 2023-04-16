package com.example.zavrsniradv3;

import android.graphics.Color;
import android.widget.Button;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MojeMetode {

    public static String kojiMjesec(DateTimeFormatter f, LocalDateTime d){
        if(f.format(d).equals("01")){
            return "siječnja";
        }
        else if(f.format(d).equals("02")){
            return "veljače";
        }
        else if(f.format(d).equals("03")){
            return "ožujka";
        }
        else if(f.format(d).equals("04")){
            return "travnja";
        }
        else if(f.format(d).equals("05")){
            return "svibnja";
        }
        else if(f.format(d).equals("06")){
            return "lipnja";
        }
        else if(f.format(d).equals("07")){
            return "srpnja";
        }
        else if(f.format(d).equals("08")){
            return "kolovoza";
        }
        else if(f.format(d).equals("09")){
            return "rujna";
        }
        else if(f.format(d).equals("10")){
            return "listopada";
        }
        else if(f.format(d).equals("11")){
            return "studenog";
        }
        else if(f.format(d).equals("12")){
            return "prosinca";
        }
        return "";
    }
    public static String makniNule(String num)
    {
        for(int i=0;i<num.length();i++){
            if(num.charAt(i)!='0'){
                return num.substring(i);
            }
        }
        return "0";
    }
    public static void zaprati(Button btn){
        btn.setText("PRATIM");
        btn.setBackgroundColor(Color.WHITE);
        btn.setTextColor(Color.parseColor("#FF5722"));
        btn.setContentDescription(""+1);
    }

}
