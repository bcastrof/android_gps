package com.example.bruno.ilernaauto_bruno_castro_v2.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bruno.ilernaauto_bruno_castro_v2.R;
import com.example.bruno.ilernaauto_bruno_castro_v2.entidades.Coche;

import java.util.ArrayList;

//creacion de la clase AdaptadorComprarCoche la cual extiende de RecyclerView.Adapter
public class AdaptadorComprarCoche extends RecyclerView.Adapter<AdaptadorComprarCoche.ComprarCochesViewHolder>{

    //array encargado de poblar los datos en el cardview
    ArrayList<Coche> listaCoches;

   //constructor de la clase AdaptadorComprarCoche a la que le paso como parametro el array
    public AdaptadorComprarCoche(ArrayList<Coche> listaCoches){
        this.listaCoches=listaCoches;
    }

    //subcargar de los diferentes Holder, Un ViewHolder es un objeto que representa un ítem de la lista,
    // el cual almacena las referencias de los views dentro del layout con propósitos de acceso rápido.
    // Este objeto es la comunicación directa entre el LayoutManager y el adaptador, actuando como caché.
    @Override
    //infla el contenido de un nuevo ítem para la lista.
    public ComprarCochesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista,null,false);

        return new ComprarCochesViewHolder(view);
    }

    @Override
    //realiza las modificaciones del contenido de cada ítem.
    public void onBindViewHolder(AdaptadorComprarCoche.ComprarCochesViewHolder holder, int position) {

        holder.marcaIn.setText(listaCoches.get(position).getMarca());
        holder.modeloIn.setText(listaCoches.get(position).getModelo());
        holder.combustibleIn.setText(listaCoches.get(position).getCombustible());
        holder.anoIn.setText(listaCoches.get(position).getAno().toString());
        holder.tiendaIn.setText(listaCoches.get(position).getTienda());
    }

    @Override
    //define cantidad de elementos que se procesarán
    public int getItemCount() {
        return listaCoches.size();
    }

        //enlazo los diferentes elementos del cardview
    public class ComprarCochesViewHolder extends RecyclerView.ViewHolder{

        TextView marcaIn, modeloIn, combustibleIn, anoIn, tiendaIn;

        public ComprarCochesViewHolder(View itemView) {
            super(itemView);

            marcaIn = (TextView) itemView.findViewById(R.id.viewInMarca);
            modeloIn = (TextView) itemView.findViewById(R.id.viewInModelo);
            combustibleIn = (TextView) itemView.findViewById(R.id.viewInCombustible);
            anoIn = (TextView) itemView.findViewById(R.id.viewInAno);
            tiendaIn = (TextView) itemView.findViewById(R.id.viewInTienda);

        }
    }
}
