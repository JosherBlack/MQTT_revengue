package com.example.mqtt_revengue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import java.text.DecimalFormat;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity
{

    public static String MQTTHOST = "tcp://lossolos.hopto.org:1883";
    public static String USERNAME ="";
    public static String PASSWORD = "";

    MqttAndroidClient client;

    // Textos

    public TextView tv1;
    public TextView tv2;
    public TextView tv3;
    public TextView tv4;

    //gauges

    CustomGauge gauge1;
    CustomGauge gauge2;
    CustomGauge gauge3;
    CustomGauge gauge4;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);

        gauge1 = (CustomGauge)findViewById(R.id.gauge1);
        gauge2 = (CustomGauge)findViewById(R.id.gauge2);
        gauge3 = (CustomGauge)findViewById(R.id.gauge3);
        gauge4 = (CustomGauge)findViewById(R.id.gauge4);


        String clientID = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), MQTTHOST, clientID);
        final MqttConnectOptions options = new MqttConnectOptions();

        //options.setUserName(USERNAME); //unused
        //options.setPassword(PASSWORD.toCharArray()); //unused

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Estamos Conecatados
                    Toast.makeText(getBaseContext(), "Sea ha conectado.", Toast.LENGTH_LONG).show();
                    subscribetopics();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // SSi pasan problemas
                    Toast.makeText(getBaseContext(), "Existe un error.", Toast.LENGTH_LONG).show();
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

                // gauges

                if(topic.matches("solos2_humedad")){

                    tv1.setText(new String(message.getPayload()));

                    //gauge1.setValue(Integer.parseInt(new String(message.getPayload())));

                    String SCA = (String) tv1.getText();
                    int cantidadA = 3;
                    int m = Math.max(0,SCA.length()-cantidadA);
                    gauge1.setValue(Integer.parseInt(String.valueOf(m)));

                }

                if(topic.matches("solos2_temperatura")){
                    tv2.setText(new String(message.getPayload()));

                    //gauge2.setValue(Integer.parseInt(new String(message.getPayload())));

                    String SCB = (String) tv2.getText();
                    int cantidadB = 3;
                    int m = Math.max(0,SCB.length()-cantidadB);
                    gauge2.setValue(Integer.parseInt(String.valueOf(m)));

                }

                if(topic.matches("solos2_higrometro")){
                    gauge3.setValue(Integer.parseInt(new String(message.getPayload())));
                    tv3.setText(new String(message.getPayload()));
                }

                if(topic.matches("solos2_luz")){
                    gauge4.setValue(Integer.parseInt(new String(message.getPayload())));
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

