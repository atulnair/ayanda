package sample;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sintulabs.ayanda.R;
import sintulabs.p2p.IWifiDirect;
import sintulabs.p2p.Lan;
import sintulabs.p2p.WifiDirect;

/**
 * Created by sabzo on 1/18/18.
 */

public class WifiDirectActivity extends AppCompatActivity {
    private WifiDirect p2p;
    private ListView lvDevices;
    private List peers = new ArrayList();
    private List peerNames = new ArrayList();
    private ArrayAdapter<String> peersAdapter = null;
    private Handler peerHandler;

    private Button btnWdAnnounce;
    private Button btnWdDiscover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
        setListeners();
        setHandler();
        p2p = new WifiDirect(this, new IWifiDirect() {
            @Override
            public void wifiP2pStateChangedAction(Intent intent) {

            }

            @Override
            public void wifiP2pPeersChangedAction() {

            }

            @Override
            public void wifiP2pConnectionChangedAction(Intent intent) {

            }

            @Override
            public void wifiP2pThisDeviceChangedAction(Intent intent) {

            }
        });
        p2p.discover();
    }

    private void createView() {
        setContentView(R.layout.wifidirect_activity);
        lvDevices = (ListView) findViewById(R.id.lvDevices);
        peersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, peerNames);
        lvDevices.setAdapter(peersAdapter);
        btnWdAnnounce = (Button) findViewById(R.id.btnWdAnnounce);
        btnWdDiscover = (Button) findViewById(R.id.btnWdDiscover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setHandler() {
        peerHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                peers.clear();
                // TODO fix error when WiFi off
                peers.addAll((List <WifiP2pDevice>) msg.obj );
                peerNames.clear();
                for (int i = 0; i < peers.size(); i++) {
                    WifiP2pDevice device = (WifiP2pDevice) peers.get(i);
                    peersAdapter.add(device.deviceName);
                }

                return true;
            }
        });
    }

    private void setListeners() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnWdAnnounce:
                        //p2p.announce();
                        break;
                    case R.id.btnWdDiscover:
                        p2p.discover();
                        break;
                }
            }
        };

        btnWdAnnounce.setOnClickListener(clickListener);
        btnWdDiscover.setOnClickListener(clickListener);
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        p2p.registerReceivers();
    }

    /* unregister the broadcast receiver */

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
        p2p.unregisterReceiver();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_direct, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.miBt:
                startActivity(new Intent(this, BluetoothActivity.class ));
                finish();
                break;
            case R.id.miLan:
                startActivity(new Intent(this, LanActivity.class ));
                finish();
                break;
        }
        return true;
    }
}
