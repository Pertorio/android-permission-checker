package com.example.ssscanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEXTVIEW SECTION
        TextView textView_log = (TextView) findViewById(R.id.textView_log);
        setup_textView(textView_log);
        print_banner(textView_log);






    }

    protected void setup_textView(TextView textView){
        textView.setMovementMethod(new ScrollingMovementMethod());

    }

    protected void print_banner(TextView textView){
        textView.setText("");
        textView.append("         _.._..,_,_\n");
        textView.append("        (          )\n");
        textView.append("         ]~,\"-.-~~[\n");
        textView.append("       .=])' (;  ([\n");
        textView.append("       | ]:: '    [\n");
        textView.append("       '=]): .)  ([\n");
        textView.append("         |:: '    |\n");
        textView.append("          ~~----~~\n\n");
        textView.append("SSScanner v.1.0.0\n");
    }


}