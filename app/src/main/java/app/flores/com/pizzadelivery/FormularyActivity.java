package app.flores.com.pizzadelivery;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FormularyActivity extends AppCompatActivity {

    private Spinner spinner1;
    private EditText direccion ;
    private CheckBox extra_queso ;
    private CheckBox extra_jamon ;
    private RadioGroup tipo_masa;

    int precioPizza=0;
    int precioAdicionalJamon=0;
    int precioAdicionalQueso=0;
    int precioTotal=0;

    int americana = 40;
    int meetlover = 60;
    int hawaina = 45;
    int superSuprema = 65;
    int quesoextra = 8;
    int jamonextra = 12;
    String tipoMasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulary);


        spinner1 = (Spinner) findViewById(R.id.spinner_tipo);
        direccion = (EditText) findViewById(R.id.direccion);
        extra_queso = (CheckBox) findViewById(R.id.extra_queso);
        extra_jamon = (CheckBox) findViewById(R.id.extra_jamon);
        tipo_masa =(RadioGroup) findViewById(R.id.tipo_masa);
        extra_queso.setChecked(false);
        extra_jamon.setChecked(false);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String pizza = parent.getItemAtPosition(pos).toString();
                Toast.makeText(parent.getContext(),
                        "Eligió: "+pizza ,
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg) {
            }
        });

        //Se menciona la activacion de complementos
        if(extra_queso.isChecked())
            Toast.makeText(this, "Usted ha elegido Queso Extra!", Toast.LENGTH_SHORT).show();

        if(extra_jamon.isChecked())
            Toast.makeText(this, "Usted ha elegido Jamón Extra!", Toast.LENGTH_SHORT).show();
    }

    public void ordenar(View view) {
        String getDireccion = direccion.getText().toString(); // Resultado: Lo que escribas pinche prro
        String item = (String)spinner1.getSelectedItem(); //Resultado: Pizza Americana
        int radioButtonId = tipo_masa.getCheckedRadioButtonId(); //Resultado : 1
        if (!item.equals("Seleccione un tipo de pizza")){
            if (radioButtonId != -1){
                if(getDireccion.length() != 0){
                    if(item.equals("Pizza Americana")){
                        precioPizza=americana;
                    }else if(item.equals("Pizza MeetLovert")){
                        precioPizza=meetlover;
                    }else if(item.equals("Pizza Hawaina")){
                        precioPizza=hawaina;
                    }else if(item.equals("Pizza Americana")){
                        precioPizza=superSuprema;
                    }

                    if(radioButtonId==1){
                        tipoMasa = "Masa gruesa";
                    }else if(radioButtonId==2){
                        tipoMasa = "Masa delgada";
                    }else{
                        tipoMasa = "Masa artesanal";
                    }

                    if(extra_queso.isChecked()){
                        precioAdicionalQueso = quesoextra;
                    }
                    if(extra_jamon.isChecked()){
                        precioAdicionalJamon = jamonextra;
                    }
                    precioTotal = precioPizza + precioAdicionalQueso + precioAdicionalJamon;
                    //Se confiugura la alerta cuando se da click en el boton Ordenar
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Confirmacion de pedido");
                    alertDialog.setMessage("Su pedido de " +item+ " con "+
                            tipoMasa + " a S/. "+precioTotal+" + IGV a la direccion: "+ getDireccion +" Esta en proceso de envio");
                    // Alert dialog button
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Alert dialog action goes here
                                    extra_queso.setChecked(false);
                                    extra_jamon.setChecked(false);
                                    direccion.setText("");
                                }
                            });
                    alertDialog.show();
                    //////////////////////////////////////////////////////
                    //Enviar la notificacion despues de 10 segundos:
                    Intent  intent = new Intent(FormularyActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(FormularyActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    // Notification
                    final Notification notification = new NotificationCompat.Builder(FormularyActivity.this)
                            .setContentTitle("Pizzeria Jorge")
                            .setContentText("Su pedido está a punto de llegar")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                            //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();

                    // Notification manager
                    final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    // Esperar 10 segundos ...
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            notificationManager.notify(0, notification);
                        }
                    }, 10000);

                    //displayErrors.setText("Su pedido de " +item+ " con "+ tipoMasa + " a S/. "+precioTotal+" + IGV a la direccion: "+ getDireccion +" Esta en proceso de envio");
                }else {
                    Toast.makeText(this, "Debes ingresar una direccion!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Debes elegir un tipo de masa!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Debes elegir un tipo de pizza!", Toast.LENGTH_SHORT).show();
        }
    }
}
