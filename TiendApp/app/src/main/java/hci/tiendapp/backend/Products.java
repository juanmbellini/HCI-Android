package hci.tiendapp.backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.ConnectionPendingException;

import static hci.tiendapp.backend.Service.connect;

/**
 * Created by Julian on 11/18/2015.
 */
public class Products {
    JSONObject products;

    public Products()throws ConnectionPendingException {
        try {
            products = connect("http://eiffel.itba.edu.ar/hci/service3/Catalog.groovy?method=GetAllProducts\n").getJSONObject("products");
        }
        catch (ConnectionPendingException | JSONException e) {
            e.printStackTrace();
        }
    }


}
