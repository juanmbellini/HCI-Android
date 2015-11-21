package hci.tiendapp.constants;

/**
 * Class representing constants
 *
 * Created by JuanMarcos on 18/11/15.
 */
public class Constants {

    public static final String genderSelection = "genderSelection";

    public static final String menCategory = "menCategory";
    public static final String womenCategory = "womenCategory";
    public static final String kidsCategory = "kidsCategory";
    public static final String babiesCategory = "babiesCategory";

    public static final String categorySelectionId = "categorySelectionId";
    public static final String categorySelectionName = "categorySelectionName";

    public static final String[] sectionFilters = {
            "[ {\"id\": 1, \"value\": \"Masculino\"}, {\"id\": 2, \"value\": \"Adulto\"}]",     // Men request
            "[ {\"id\": 1, \"value\": \"Femenino\"}]",                                          // Women request
            "[ {\"id\": 2, \"value\": \"Infantil\"}]",                                          // Kids request
            "[ {\"id\": 2, \"value\": \"Bebe\"}]"                                               // Babies request
    };

}
