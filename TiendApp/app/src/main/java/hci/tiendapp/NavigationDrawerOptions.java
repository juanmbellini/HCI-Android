package hci.tiendapp;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerOptions {

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

        String currentOption = null;
        Drawable currentIcon = null;

        // Now adding Home
        currentOption = Resources.getSystem().getString(R.string.drawer_option_home);
        currentIcon = Resources.getSystem().getDrawable(R.drawable.ic_home);
        list.add(new NavigationDrawerMainOption(0, currentOption, currentIcon));
    }

    /**
     * Gets the options as an array
     *
     * @return An array of NavigationDrawerOption that contains each option for the Navigation Drawer
     */
    public NavigationDrawerOption[] getValues() {

        return (NavigationDrawerOption[])options.toArray();

    }

    // Inner Class representing a Navigation Drawer Option
    private static class NavigationDrawerOption {

        int position;
        String option;

        public NavigationDrawerOption(int position, String option) {
            this.position = position;
            this.option = option;
        }

    }

    // Inner Class representing a Navigation Drawer Option that contains an icon
    private static class NavigationDrawerMainOption extends NavigationDrawerOption {

        Drawable icon;

        NavigationDrawerMainOption(int position, String option, Drawable icon) {
            super(position, option);
            this.icon = icon;
        }
    }
}
