package appdev2.gameofdeath;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Jessica on 2/13/18.
 */

public class GamePanelAdapter extends RecyclerView.Adapter<GamePanelAdapter.ViewHolder>{
    private List<Bitmap> SeedList;
    private LayoutInflater mInflater;

    public GamePanelAdapter(List<Bitmap> SeedList) {
        this.SeedList = SeedList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView seed_map;
        public TextView seed_cost;

        public ViewHolder(View view) {
            super(view);
            seed_map = (ImageView) view.findViewById(R.id.seed_map);
            seed_cost = (TextView) view.findViewById(R.id.seed_cost);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gamepanel_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap seed = SeedList.get(position);
        holder.seed_map.setImageBitmap(seed);
        holder.seed_cost.setText("5");
    }

    @Override
    public int getItemCount() {
        return SeedList.size();
    }

}

