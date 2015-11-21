package hci.tiendapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hci.tiendapp.R;

/**
 * Created by JuanMarcos on 20/11/15.
 */
public class CatalogueActivity extends MyDrawerActivity {


    public CatalogueActivity() {
        super(R.layout.activity_catalogue, R.id.catalogue_layout);
        super.setContext(this);
    }


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }



    private class CatalogueAdapter extends BaseAdapter {

        String[] result = {"Hola", "Chau", "Nos vimos"};
        Context context;
        int [] image = {1,2,3};

        private LayoutInflater inflater = null;

        public CatalogueAdapter(Context context) {
            this.context = context;

        }


        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class Holder {
            ImageView image;
            TextView name;
            TextView price;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
