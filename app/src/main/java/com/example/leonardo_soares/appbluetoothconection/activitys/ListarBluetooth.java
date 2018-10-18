package com.example.leonardo_soares.appbluetoothconection.activitys;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class ListarBluetooth extends ListActivity {

    private BluetoothAdapter meuBluetoothAdapter2 = null;
    static String ENDERECO_MAC = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //ARMAZENA OS ITENS NA LISTA
        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        meuBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados = meuBluetoothAdapter2.getBondedDevices();

        if (dispositivosPareados.size() > 0) {
            for (BluetoothDevice device : dispositivosPareados) {
                String nomeBluetooth = device.getName();
                String macBluetooth = device.getAddress();
                arrayBluetooth.add(nomeBluetooth + "\n" + macBluetooth);


            }

        }
        setListAdapter(arrayBluetooth);
    }




    // Pega rndereço MAC do dispositivo ja pareado  e exibe em uma lista
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String infoGeral = ((TextView) v).getText().toString();

        //  Toast.makeText(getApplicationContext(), "info: " + infoGeral, Toast.LENGTH_LONG).show();

        String enderecoMac = infoGeral.substring(infoGeral.length() - 17);

        Intent retornaMac = new Intent();
        retornaMac.putExtra(ENDERECO_MAC, enderecoMac);
        setResult(RESULT_OK, retornaMac);
        finish();
    }
}
