package ir.bahonar.nama;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {
    int REQUEST_ENABLE_BT = 0;
    public static TextView TV;
    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        TV = findViewById(R.id.textView2);
        EditText data = findViewById(R.id.editTextTextPersonName);
        Button send = findViewById(R.id.button3);
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String uuid = Settings.Secure.ANDROID_ID;
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String imei = tManager.getImei();
            } else {
                String imei = tManager.getDeviceId();
            }*/
            UUID uuidOBJ = UUID.nameUUIDFromBytes(uuid.getBytes());
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
                TV.setText(TV.getText() + "\nnot support");
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    TV.setText(TV.getText() + "\nnot enabled");
                } else {
                    TV.setText(TV.getText() + "\nenabled");
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    // Register for broadcasts when a device is discovered.
                    //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    //registerReceiver(receiver, filter);
                    //Toast.makeText(getApplicationContext(), "Enable : "+bluetoothAdapter.startDiscovery(), Toast.LENGTH_SHORT).show();


                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        TV.setText(TV.getText() + "\nsize :" + pairedDevices.size());
                        // There are paired devices. Get the name and address of each paired device.
                        //Toast.makeText(getApplicationContext(),"size : "+pairedDevices.size() , Toast.LENGTH_SHORT).show();

                        for (BluetoothDevice device : pairedDevices) {
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress(); // MAC address

                            TV.setText(TV.getText() + "\ndevice name : " + deviceName);
                            TV.setText(TV.getText() + "\ndevice Add : " + deviceHardwareAddress);

                            BluetoothConnector bc = new BluetoothConnector(device,true,bluetoothAdapter,null);

                            BluetoothConnector.BluetoothSocketWrapper bsw = bc.connect();
                            OutputStream ops = bsw.getOutputStream();
                            send.setOnClickListener(v->{
                                String text = data.getText().toString();
                                char[] c = text.toCharArray();
                                byte[] bytes = new byte[c.length];
                                for (int i = 0; i < c.length; i++)
                                    bytes[i] = (byte) c[i];
                                try {
                                    ops.write(bytes);
                                    TV.setText(TV.getText() + "\nwrote : " +data+ deviceName);
                                } catch (IOException e) {
                                    TV.setText(TV.getText() + "\ndata : " +ops.toString()+ deviceName);
                                }
                            });
                            TV.setText(TV.getText() + "\nops : " +ops.toString()+ deviceName);
                            //ConnectThread ct = new ConnectThread(device, bluetoothAdapter, uuidOBJ,this);
                            //ct.start();
                            //Toast.makeText(getApplicationContext(),"deviceName : " + deviceName +" \ndeviceHardwareAddress : "+deviceHardwareAddress, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "zero", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        }catch (Exception e){
            TV.setText(TV.getText() + "\nno view , Exception : " + e.toString());
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(getApplicationContext(),
                    "receive : " + action, Toast.LENGTH_SHORT).show();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Toast.makeText(getApplicationContext(),
                        "deviceName : " + deviceName +" \ndeviceHardwareAddress : "+deviceHardwareAddress, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }
}