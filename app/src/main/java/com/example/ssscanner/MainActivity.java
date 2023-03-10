package com.example.ssscanner;

import static android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;
import static android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;
import static android.content.pm.PermissionInfo.PROTECTION_SIGNATURE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
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

        //LogPM button
        Button button_logPM = (Button) findViewById(R.id.button_logPM);
        setup_button(button_logPM, "log PM");
        View.OnClickListener onClickListener_logPM = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logPM(textView_log);
            }
        };
        button_logPM.setOnClickListener(onClickListener_logPM);






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

    protected void setup_button(Button button, String string){
        button.setText(string);
        button.setBackgroundColor(Color.BLUE);

    }

    protected void logPM(TextView textView){
        textView.append("\nlogPM function was invoked!\n\n");
        //log information from Package Manager
        final PackageManager packageManager = getPackageManager();
        //Loop each package requesting <manifest> permissions
        for(final PackageInfo packageInfo : packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)){
            final String[] requestedPermission = packageInfo.requestedPermissions;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            textView.append("Process name: "+applicationInfo.processName+"\n");
            textView.append("Package name: "+applicationInfo.packageName+"\n");
            textView.append("Permission Granted: \n");
            if (requestedPermission == null){
                //No permission in the manifest
                textView.append("No permission in this Manifest\n\n\n");
                continue;
            }
            // Loop each <uses-permission> tag to retrieve the permission flag
            for (int i = 0, len = requestedPermission.length; i < len; i++) {
                final String requestedPerm = requestedPermission[i];
                // Retrieve the protection level for each requested permission
                int protLevel;
                try {
                    protLevel = packageManager.getPermissionInfo(requestedPerm, 0).protectionLevel;
                } catch (PackageManager.NameNotFoundException e) {
                    //Log.e(TAG, "Unknown permission: " + requestedPerm, e);
//                    String error_log = "<font color=#cc0029>"+e+"</font>";
//                    textView.append("Error: "+Html.fromHtml(error_log)+"\n");
                    textView.append("Error: "+e+"\n");
                    continue;
                }
                final boolean system = protLevel == PROTECTION_SIGNATURE;
                final boolean dangerous = protLevel == PROTECTION_DANGEROUS;
                final boolean granted = (packageInfo.requestedPermissionsFlags[i]
                        & REQUESTED_PERMISSION_GRANTED) != 0;
                if(granted){
                    textView.append(requestedPerm+"\n");
                }
            }
            //Package spacing
            textView.append("\n\n");

        }

    }



}