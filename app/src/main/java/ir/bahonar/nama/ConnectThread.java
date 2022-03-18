package ir.bahonar.nama;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final String TAG = "tag";
    private final BluetoothAdapter bluetoothAdapter;
    private Activity activity;
    public BluetoothSocket getSocket(){
        return mmSocket;
    }

    public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, UUID UUID, Activity activity) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        this.activity = activity;
        BluetoothSocket tmp = null;
        mmDevice = device;
        this.bluetoothAdapter = bluetoothAdapter;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(UUID);
            Bluetooth.TV.setText(Bluetooth.TV.getText()+"\nsocket created");
        } catch (Exception e) {
            Bluetooth.TV.setText(Bluetooth.TV.getText()+"\nSocket's create() method failed,Exception : " + e.toString());
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        try {

            // Cancel discovery because it otherwise slows down the connection.
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();

                activity.runOnUiThread(()-> {
                    Bluetooth.TV.setText(Bluetooth.TV.getText() + "\nconnected");
                });
            } catch (IOException e) {
                // Unable to connect; close the socket and return.
                activity.runOnUiThread(()-> {
                    Bluetooth.TV.setText(Bluetooth.TV.getText() + "\nnot connected,Exception : " + e.toString());
                    Class<?> clazz = mmSocket.getRemoteDevice().getClass();
                    Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};

                    try {
                        Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                        Object[] params = new Object[] {1};

                        //fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
                        //fallbackSocket.connect();
                    } catch (NoSuchMethodException noSuchMethodException) {
                        noSuchMethodException.printStackTrace();
                    }

                });
                try {
                    mmSocket.close();

                    activity.runOnUiThread(()-> {
                        Bluetooth.TV.setText(Bluetooth.TV.getText() + "\nclosed");
                    });
                } catch (IOException ee) {

                    activity.runOnUiThread(()-> {
                                Bluetooth.TV.setText(Bluetooth.TV.getText() + "\nnot closed,Exception : " + ee.toString());
                            });
                    //Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(mmSocket);

        }catch (Exception e){

        }
    }

    private void manageMyConnectedSocket(BluetoothSocket mmSocket){
        Bluetooth.TV.setText(Bluetooth.TV.getText()+"\nplease manage");
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}