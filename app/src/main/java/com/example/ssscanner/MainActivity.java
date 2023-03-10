package com.example.ssscanner;

import static android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;
import static android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;
import static android.content.pm.PermissionInfo.PROTECTION_SIGNATURE;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

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

        //LogAM Button
        Button button_logAM = (Button) findViewById(R.id.button_logAM);
        setup_button(button_logAM, "log AM");
        View.OnClickListener onClickListener_logAM = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logAM(textView_log);
            }
        };
        button_logAM.setOnClickListener(onClickListener_logAM);

        //Scan Button
        Button button_scan = (Button) findViewById(R.id.button_scan);
        setup_button(button_scan, "SCAN");
        View.OnClickListener onClickListener_scan = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanPermission(textView_log);
            }
        };
        button_scan.setOnClickListener(onClickListener_scan);

        //CLEAR BUTTON
        Button button_clear = (Button) findViewById(R.id.button_clear);
        setup_button(button_clear, "CLEAR");
        View.OnClickListener onClickListener_clear = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print_banner(textView_log);
            }
        };
        button_clear.setOnClickListener(onClickListener_clear);




    }

    protected void setup_textView(TextView textView){
        textView.setMovementMethod(new ScrollingMovementMethod());

    }

    protected void print_banner(TextView textView){
        textView.setText("         _.._..,_,_\n");
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

    protected void logAM(TextView textView) {
        textView.append("\nQuery Accessibility Services\n\n");
        final AccessibilityManager accessibilityManager = (AccessibilityManager) getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        if (runningServices.isEmpty()){
            textView.append("There is no accessibility service\n");
        } else{
            for (AccessibilityServiceInfo service : runningServices){
                textView.append(service.toString()+"\n");

            }
        }

    }

    protected void scanPermission(TextView textView){
        final boolean foo, bar;
        textView.append("\nScan function was invoked!\n\n");
        textView.append("Scanning for overlay permission\n");
        textView.append("Result: ");
        foo = overlayCheck(textView);
        bar = accessibilityCheck();
        if(foo){
            textView.append("android.permission.SYSTEM_ALERT_WINDOW is enabled\n");
        } else{
            textView.append("android.permission.SYSTEM_ALERT_WINDOW is disabled\n");
        }
        textView.append("\nScanning for accessibility permission\n");
        textView.append("Result: ");
        if(bar){
            textView.append("Accessibility is enabled\n");
        } else{
            textView.append("Accessibility is disabled\n");
        }

    }

    protected boolean accessibilityCheck(){
        AccessibilityManager accessibilityManager = (AccessibilityManager) getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            // if (ids.contains(service.getId())) {
            // Compare your whitelist with the service ID
            //}
            return true;
        }
        return false;
    }

    protected boolean overlayCheck(TextView textView){
        PackageManager packageManager = getPackageManager();

        // Get a list of all installed applications on the device
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Loop through each application and check if it has the SYSTEM_WINDOW_ALERT permission
        boolean isPermissionSet = false;
        for (ApplicationInfo application : applications) {
            try {
                // Check if the application has the SYSTEM_WINDOW_ALERT permission
                if (packageManager.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", application.packageName) == PackageManager.PERMISSION_GRANTED) {
                    isPermissionSet = true;
                    textView.append("\nPackage name: "+application.packageName+"\n");
                    break;
                }
            } catch (Exception e) {
                // Handle exceptions here
            }
        }
        return isPermissionSet;
    }


}