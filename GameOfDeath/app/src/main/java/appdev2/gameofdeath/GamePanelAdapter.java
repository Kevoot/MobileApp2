package appdev2.gameofdeath;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Jessica on 2/13/18.
 */



public class GamePanelAdapter extends RecyclerView.Adapter<GamePanelAdapter.ViewHolder>{
    private LayoutInflater mInflater;

    private ClickListener listener;
    private List<Bitmap> SeedList;


    public GamePanelAdapter(List<Bitmap> SeedList, ClickListener listener) {
        this.listener = listener;
        this.SeedList = SeedList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView seed_map;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View view, ClickListener listener) {
            super(view);

            listenerRef = new WeakReference<>(listener);
            seed_map = view.findViewById(R.id.seed_map);

            view.setOnClickListener(this);
            seed_map.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if (v.getId() == seed_map.getId()) {
                    Toast.makeText(v.getContext(), "BOX SELECTED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }


            listenerRef.get().onPositionClicked(getAdapterPosition());

        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gamepanel_item, parent, false), listener);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap seed = SeedList.get(position);
        holder.seed_map.setImageBitmap(seed);
    }

    @Override
    public int getItemCount() {
        return SeedList.size();
    }

}

