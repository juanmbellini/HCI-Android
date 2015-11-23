package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

import java.util.regex.Pattern;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class CreditCard {

    @Expose
    int id;
    @Expose
    String securityCode;
    @Expose
    String expirationDate;
    @Expose
    String number;

    public CreditCard(int id, String securityCode, String expirationDate, String number) {
        this.id = id;
        this.securityCode = securityCode;
        this.expirationDate = expirationDate;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getNumber() {
        return number;
    }

    public String getCardName() {

        if (Pattern.matches("/^(34|37)[0-9]{13}$/",number)) {
            return "American Express";
        }

        if (Pattern.matches("/^(36)[0-9]{14}$/", number)) {
            return "Diners";
        }

        if (Pattern.matches("/^(51|52|53)[0-9]{14}$/", number)) {
            return "Master Card";
        }

        if (Pattern.matches("/^4([0-9]{12}|[0-9]{15})$/", number)) {
            return "Visa";
        }
        return "";

    }
}


