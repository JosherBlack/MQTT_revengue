package com.example.mqtt_revengue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity
{

    public static String MQTTHOST = "tcp://lossolos.hopto.org:1883";
    public static String USERNAME ="";
    public static String PASSWORD = "";

    MqttAndroidClient client;

    public TextView tv1;
    public TextView tv2;
    public TextView tv3;
    public TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2= (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);


        String clientID = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), MQTTHOST, clientID);
        final MqttConnectOptions options = new MqttConnectOptions();

        //options.setUserName(USERNAME);
        //options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(getBaseContext(), "Sea ha conectado", Toast.LENGTH_LONG).show();
                    subscribetopics();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getBaseContext(), "pico ctm", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // aqui llegan los mensajes
                if(topic.matches("solos2_temperatura"))
                {
                    tv1.setText(new String(message.getPayload()));
                }

                if(topic.matches("solos2_humedad"))
                {
                    tv2.setText(new String(message.getPayload()));
                }

                if(topic.matches("solos2_higrometro"))
                {
                    tv3.setText(new String(message.getPayload()));
                }

                if(topic.matches("solos2_luz"))
                {
                    tv4.setText(new String(message.getPayload()));
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }

    private void subscribetopics()
    {
        try{
            client.subscribe("solos2_temperatura", 0);
            client.subscribe("solos2_humedad", 0);
            client.subscribe("solos2_higrometro", 0);
            client.subscribe("solos2_luz", 0);
        }
        catch(MqttException e)
        {
            e.printStackTrace();
        }
    }
}

