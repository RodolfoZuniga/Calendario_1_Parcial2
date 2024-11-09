package com.example.calendario_1_parcial2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import androidx.annotation.Nullable;


import java.util.Calendar;

public class CalendarioControl extends LinearLayout {
    private TextView txtDia, txtMes, txtAno, txtDIA;
    private EditText genAnio;
    private Spinner genMes;
    private Button btnPrevDia, btnNextDia;

    private int dia = 1;
    private int mes = 1;
    private int anio = Calendar.getInstance().get(Calendar.YEAR);

    private OnFechaSeleccionadaListener listener;

    public CalendarioControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.fecha_control, this, true);

        txtDia = findViewById(R.id.txtDia);
        txtMes = findViewById(R.id.txtMes);
        txtAno = findViewById(R.id.txtAno);
        txtDIA = findViewById(R.id.txtDIA);
        genAnio = findViewById(R.id.genAnio);
        genMes = findViewById(R.id.genMes);
        btnPrevDia = findViewById(R.id.btn1d);
        btnNextDia = findViewById(R.id.btn2d);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.meses_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genMes.setAdapter(adapter);

        setupListeners();
        actualizarFecha();
    }

    private void setupListeners() {
        btnPrevDia.setOnClickListener(view -> {
            if (dia > 1) dia--;
            actualizarFecha();
        });

        btnNextDia.setOnClickListener(view -> {
            if (dia < obtenerMaximoDias(mes, anio)) dia++;
            actualizarFecha();
        });

        genMes.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                mes = position + 1;
                actualizarFecha();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        genAnio.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                try {
                    anio = Integer.parseInt(genAnio.getText().toString());
                } catch (NumberFormatException e) {
                    anio = Calendar.getInstance().get(Calendar.YEAR);
                }
                actualizarFecha();
            }
        });
    }

    private void actualizarFecha() {
        int maximoDias = obtenerMaximoDias(mes, anio);
        if (dia > maximoDias) {
            dia = maximoDias;
        }

        txtDIA.setText(String.format("%02d", dia));
        txtDia.setText(String.format("%02d", dia));
        txtMes.setText(String.format("%02d", mes));
        txtAno.setText(String.valueOf(anio));

        if (listener != null) {
            listener.onFechaSeleccionada(dia, mes, anio);
        }
    }

    private int obtenerMaximoDias(int mes, int anio) {
        switch (mes) {
            case 4: case 6: case 9: case 11:
                return 30; // Abril, Junio, Septiembre, Noviembre
            case 2:
                return esBisiesto(anio) ? 29 : 28; // Febrero
            default:
                return 31; // Enero, Marzo, Mayo, Julio, Agosto, Octubre, Diciembre
        }
    }

    private boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
    }

    // Método para configurar el listener
    public void setOnFechaSeleccionadaListener(OnFechaSeleccionadaListener listener) {
        this.listener = listener;
    }
}
