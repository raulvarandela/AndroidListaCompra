package com.rvmarra17.listacompra.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rvmarra17.listacompra.R;
import com.rvmarra17.listacompra.core.Item;
import com.rvmarra17.listacompra.core.ItemArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();

        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);
        this.registerForContextMenu(lvItems);

        lvItems.setLongClickable(true);
        this.itemsAdapter = new ItemArrayAdapter(this, items);
        lvItems.setAdapter(this.itemsAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) { //para cuando mantengo pulsado
                /*if (pos >= 0) {
                    MainActivity.this.items.remove(pos);
                    MainActivity.this.itemsAdapter.notifyDataSetChanged();
                    MainActivity.this.updateStatus();
                }*/
                posicion = pos;
                return false;
            }
        });

        /*lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modificar(position);
            }
        });*/

        btAdd.setOnClickListener(new View.OnClickListener() { //para cuando pulso el boton
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();
            }
        });

    }

    private void modificar(final int pos) {
        final EditText edText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar");
        builder.setMessage("Inserta las nuevos cambios");
        builder.setView(edText);
        edText.setText(items.get(pos));


        builder.setNeutralButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String text = edText.getText().toString();
                items.remove(pos);
                items.add(pos, text);
                itemsAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void eliminar(final int pos) {
        final EditText edText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("Seguro que quiere eliminar este elemento de su lista de la compra?");
        //builder.setView(edText); para el punto 2 si

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.items.remove(pos);
                MainActivity.this.itemsAdapter.notifyDataSetChanged();
                MainActivity.this.updateStatus();
            }
        });
        /*builder.setNeutralButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String text = edText.getText().toString();
                items.remove(pos);
                items.add(pos,text);
                itemsAdapter.notifyDataSetChanged();
            }
        });*/
        builder.setNegativeButton("No", null);
        builder.create().show();
    }


    private void onAdd() {
        final EditText edText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A comprar...");
        builder.setMessage("Nombre");
        builder.setView(edText);
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String text = edText.getText().toString();

                MainActivity.this.itemsAdapter.add(text);
                MainActivity.this.updateStatus();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById(R.id.lblNum);
        txtNum.setText(Integer.toString(this.itemsAdapter.getCount()));
    }

    private ItemArrayAdapter itemsAdapter;
    private ArrayList<String> items;


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.lvItems) {
            this.getMenuInflater().inflate(R.menu.menu_contextual, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);


        boolean toret = false;

        switch (item.getItemId()) {
            case R.id.opEliminar:
                eliminar(posicion);
                toret = true;
                break;
            case R.id.opAñadir:
                this.onAdd();
                toret = true;
                break;
            case R.id.opModificar:
                this.modificar(posicion);
                toret = true;
                break;

        }

        return toret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.op_anhadir:
                this.onAdd();
                break;
            case R.id.op_salir:
                System.exit(0);
        }

        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();

        final SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        StringBuilder cadena = new StringBuilder();


        for (int i = 0; i < items.size(); i++) {
            cadena.append(items.get(i));
            cadena.append(" ");

        }
        editor.putString("items", cadena.toString());
        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
        final String itemsStr = pref.getString("items", "");
        final String[] array = itemsStr.split(" ");
        items.removeAll(items);
        itemsAdapter.notifyDataSetChanged();

        if (array.length != 1 || !array[0].equals("")) {
            for (String item : array) {
                itemsAdapter.add(item);
            }
            this.updateStatus();
        }
    }


}