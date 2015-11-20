package hci.tiendapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

import hci.tiendapp.R;
import hci.tiendapp.constants.Constants;

/**
 * Abstract Class representing an Activity with the Navigation Drawer
 * It provides all the set up of the drawer.
 *
 * Created by JuanMarcos on 18/11/15.
 */
public abstract class MyDrawerActivity extends AppCompatActivity{

    private RecyclerView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private int layoutId;
    private int childId;

    private Activity childActivity;

    public MyDrawerActivity(int layoutId, int childId) {
        this.layoutId = layoutId;
        this.childId = childId;
    }

    public void setContext(Activity childActivity) {
        this.childActivity = childActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(layoutId);
        View childView = findViewById(childId);
        setContentView(R.layout.navigation_drawer);

        // Sets up the action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets up the navigation drawer

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawerList = (RecyclerView) findViewById(R.id.left_drawer);
        RecyclerView.Adapter drawerAdapter = new MyAdapter(childActivity);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setLayoutManager(new LinearLayoutManager(childActivity));

        drawerToggle = new ActionBarDrawerToggle(childActivity, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setFitsSystemWindows(true);
        drawerToggle.setDrawerIndicatorEnabled(childActivity.getClass() == HomeActivity.class);
        drawerLayout.addView(childView, 0);



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO Agregar funcionalidad
            // Do something
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getIntent();
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
        // IF the view under inflation and population is header or Item
        private static final int TYPE_ITEM = 1;

        Context context;


        // Creating a ViewHolder which extends the RecyclerView View Holder
        // ViewHolder are used to to store the inflated views in order to recycle them

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            int Holderid;

            TextView textView;      // Drawer option name
            ImageView imageView;    // Drawer option icon
            ImageView profile;      // Profile pic
            TextView Name;          // User's Name and Last Name
            TextView email;         // User's e-Mail (might change to username)
            Context context;

            public ViewHolder(View itemView,int ViewType, Context context) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
                super(itemView);
                this.context = context;
                itemView.setClickable(true);
                itemView.setOnClickListener(this);

                // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

                if(ViewType == TYPE_ITEM) {
                    textView = (TextView) itemView.findViewById(R.id.drawer_option_name);
                    imageView = (ImageView) itemView.findViewById(R.id.drawer_option_icon);
                    Holderid = 1; // Holder Id for items
                }
                else{


                    Name = (TextView) itemView.findViewById(R.id.drawer_name);
                    email = (TextView) itemView.findViewById(R.id.drawer_email);
                    profile = (ImageView) itemView.findViewById(R.id.user_image);
                    Holderid = 0; // Holder Id for header
                }
            }


            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.drawer_header) {
                    return;
                }
                // TODO crear el intent que sea necesario
                //Toast.makeText(context, "The Item Clicked is: " +  getLayoutPosition(), Toast.LENGTH_LONG).show();

                String option = null;
                int position = getLayoutPosition();


                switch (position) {
                    case 1:
                        if (context.getClass() != HomeActivity.class) {
                            context.startActivity(new Intent(context, HomeActivity.class));
                        }
                        return;
                    case 2:
                        option = Constants.menCategory;
                        break;
                    case 3:
                        option = Constants.womenCategory;
                        break;
                    case 4:
                        option = Constants.kidsCategory;
                        break;
                    case 5:
                        option = Constants.babiesCategory;
                        break;
                    case 6:
                        return;
                    case 7:
                        Intent intent = new Intent(context, SettingsActivity.class);
                        context.startActivity(intent);
                        return;
                    default:
                        return;
                }

                ((MyDrawerActivity)context).drawerLayout.closeDrawers();

                Intent intent = new Intent(context, CategoriesActivity.class);
                intent.putExtra(Constants.genderSelection,option);
                context.startActivity(intent);

            }

        }



        MyAdapter(Context passedContext){
            //super();

            this.context =  passedContext;
        }



        //Below first we override the method onCreateViewHolder which is called when the ViewHolder is
        //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
        // if the viewType is TYPE_HEADER
        // and pass it to the view holder

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_option,parent,false); //Inflating the layout

                ViewHolder vhItem = new ViewHolder(v,viewType, context); //Creating ViewHolder and passing the object of type view

                return vhItem; // Returning the created object

                //inflate your layout and pass it to view holder

            } else if (viewType == TYPE_HEADER) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_header,parent,false); //Inflating the layout

                ViewHolder vhHeader = new ViewHolder(v,viewType, context); //Creating ViewHolder and passing the object of type view

                return vhHeader; //returning the object created


            }
            return null;

        }

        //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
        // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
        // which view type is being created 1 for item row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(holder.Holderid ==1) {

                // Sets the option name
                holder.textView.setText(NavigationDrawerOptions.getInstance().getValues()[position - 1].navigationDrawerOptionName);

                if (NavigationDrawerOptions.getInstance().getValues()[position - 1].getClass() == NavigationDrawerOptions.NavigationDrawerMainOption.class) {
                    NavigationDrawerOptions.NavigationDrawerMainOption aux = null;
                    aux = (NavigationDrawerOptions.NavigationDrawerMainOption) NavigationDrawerOptions.getInstance().getValues()[position - 1];
                    holder.imageView.setImageResource(aux.navigationDrawerOptionIcon);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }

            }
            else{

                holder.profile.setImageBitmap(NavigationDrawerHeader.getInstance().profile);
                holder.Name.setText(NavigationDrawerHeader.getInstance().name);
                holder.email.setText(NavigationDrawerHeader.getInstance().email);

            }
        }

        // This method returns the number of items present in the list
        @Override
        public int getItemCount() {
            return NavigationDrawerOptions.getInstance().getValues().length + 1; // the number of items in the list will be +1 the titles including the header view.
        }


        // With the following method we check what type of view is being passed
        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {

            return position == 0;
        }


        public void setUserForHeader(String name, String email, String profilePic) {
            NavigationDrawerHeader.setUser(name, email, profilePic);
        }


        private static class NavigationDrawerHeader {

            private static NavigationDrawerHeader ourInstance = new NavigationDrawerHeader();

            private String name;
            private String email;
            private Bitmap profile;

            private NavigationDrawerHeader() {

                name = "Juan Marcos Bellini";
                email = "jbellini@itba.edu.ar";
                profile = null;//R.drawable.logo2;
            }

            private NavigationDrawerHeader(String name, String email, Bitmap profile) {
                this.name = name;
                this.email = email;
                this.profile = profile;
            }

            public static NavigationDrawerHeader getInstance() {
                return ourInstance;
            }

            public static void setUser(String name, String email, String profilePic) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(profilePic).getContent());
                } catch (MalformedInputException e) {
                    e.printStackTrace();
                    bitmap = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    bitmap = null;
                } finally {
                    ourInstance = new NavigationDrawerHeader(name, email, bitmap);
                }

            }


        }


        /**
         * Inner Class representing the navigation drawer options
         */
        private static class NavigationDrawerOptions {

            /**
             * Singleton
             */
            private static NavigationDrawerOptions ourInstance = new NavigationDrawerOptions();

            // Stores options for Navigation Drawer
            private List<NavigationDrawerOption> options;


            /**
             * Gets the singleton
             *
             * @return The only instance of the Navigation Drawer Options Class
             */
            public static NavigationDrawerOptions getInstance() {
                return ourInstance;
            }

            // Private constructor
            private NavigationDrawerOptions() {

                options = new ArrayList<NavigationDrawerOption>();

                fillOptions(options);

            }


            // Method to fill the list
            private void fillOptions(List<NavigationDrawerOption> list) {

                Integer currentOption = null;
                Integer currentIcon = null;


                // Now adding Home
                currentOption = R.string.drawer_option_home;
                currentIcon = R.drawable.ic_home;
                list.add(new NavigationDrawerMainOption(1, currentOption, currentIcon));


                // Now adding Men
                currentOption = R.string.drawer_option_men;
                currentIcon = R.drawable.ic_men_category;
                list.add(new NavigationDrawerMainOption(2, currentOption, currentIcon));

                // Now adding Women
                currentOption = R.string.drawer_option_women;
                currentIcon = R.drawable.ic_women_category;
                list.add(new NavigationDrawerMainOption(3, currentOption, currentIcon));

                // Now adding Kids
                currentOption = R.string.drawer_option_kids;
                currentIcon = R.drawable.ic_kids_category;
                list.add(new NavigationDrawerMainOption(4, currentOption, currentIcon));

                // Now adding Babies
                currentOption = R.string.drawer_option_babies;
                currentIcon = R.drawable.ic_babies_category;
                list.add(new NavigationDrawerMainOption(5, currentOption, currentIcon));

                // Now adding Orders
                currentOption = R.string.drawer_option_orders;
                currentIcon = R.drawable.ic_orders;
                list.add(new NavigationDrawerMainOption(6, currentOption, currentIcon));

                // Now adding Settings
                currentOption = R.string.drawer_option_settings;
                currentIcon = R.drawable.ic_settings_black_24dp;
                list.add(new NavigationDrawerMainOption(7, currentOption, currentIcon));
            }

            /**
             * Gets the options as an array
             *
             * @return An array of NavigationDrawerOption that contains each navigationDrawerOptionName for the Navigation Drawer
             */
            public NavigationDrawerOption[] getValues() {

                NavigationDrawerOption[] array = new NavigationDrawerOption[options.size()];
                return options.toArray(array);
            }

            // Inner Class representing a Navigation Drawer Option
            private static class NavigationDrawerOption {

                int position;
                int navigationDrawerOptionName;

                public NavigationDrawerOption(int position, int navigationDrawerOptionName) {
                    this.position = position;
                    this.navigationDrawerOptionName = navigationDrawerOptionName;
                }

            }

            // Inner Class representing a Navigation Drawer Option that contains an navigationDrawerOptionIcon
            private static class NavigationDrawerMainOption extends NavigationDrawerOption {

                int navigationDrawerOptionIcon;

                NavigationDrawerMainOption(int position, int option, int navigationDrawerOptionIcon) {
                    super(position, option);
                    this.navigationDrawerOptionIcon = navigationDrawerOptionIcon;
                }
            }
        }
    }
}
