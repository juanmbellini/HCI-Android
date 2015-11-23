package hci.tiendapp.constants;

/**
 * Class representing constants
 *
 * Created by JuanMarcos on 18/11/15.
 */
public class Constants {

    public static final String goHome = "goHome";
    public static final String noReEstablishInformation = "noReEstablishInformation";
    public static final String noResponseFromGettingProductTitles = "noResponseFromGettingProductTitles";
    public static final String wrongParameters = "wrongParameters";


    public static final String genderSelection = "genderSelection";

    public static final String menCategory = "menCategory";
    public static final String womenCategory = "womenCategory";
    public static final String kidsCategory = "kidsCategory";
    public static final String babiesCategory = "babiesCategory";

    public static final String categorySelectionId = "categorySelectionId";
    public static final String categorySelectionName = "categorySelectionName";

    public static final String subCategorySelectionId = "subCategorySelectionId";
    public static final String subCategorySelectionName = "subCategorySelectionName";

    public static final String comingFrom = "comingFrom";
    public static final String comingFromNoWhere = "comingFromNoWhere";
    public static final String comingFromSearchBar = "comingFromSearchBar";
    public static final String comingFromGender = "comingFromGender";
    public static final String comingFromCategories = "comingFromCategories";
    public static final String comingFromSubCategories = "comingFromSubCategories";
    public static final String comingFromHome = "comingFromHome";



    public static final String searchQuery = "searchQuery";


    public static final String productId = "productId";
    public static final String productName = "productName";

    public static final String[] sectionFilters = {
            "[ {\"id\": 1, \"value\": \"Masculino\"}, {\"id\": 2, \"value\": \"Adulto\"}]",     // Men request
            "[ {\"id\": 1, \"value\": \"Femenino\"}]",                                          // Women request
            "[ {\"id\": 2, \"value\": \"Infantil\"}]",                                          // Kids request
            "[ {\"id\": 2, \"value\": \"Bebe\"}]"                                               // Babies request
    };

}
