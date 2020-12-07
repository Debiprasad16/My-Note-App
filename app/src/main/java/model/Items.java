package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Items {
    public boolean isChecked;
    public String itemName;
    public int id;

    public static String KEY_ID = "id";
    public static String KEY_ITEM_NAME = "item_name";
    public static String KEY_CHECKED = "is_checked";

    public static String convertArrayListToJSONArrayString(ArrayList<Items> items){
        JSONArray itemsArray = new JSONArray();
        for(Items newItemVal : items){
            try {
                JSONObject newObj = new JSONObject();
                newObj.put(KEY_ID, newItemVal.id);
                newObj.put(KEY_ITEM_NAME, newItemVal.itemName);
                newObj.put(KEY_CHECKED, newItemVal.isChecked);

                itemsArray.put(newObj);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return itemsArray.toString();
    }

    public static ArrayList<Items> covertJSONArrayStringToArrayList(String itemsArrayString){
        ArrayList<Items> items = new ArrayList<>();
        try{
            JSONArray itemsArray = new JSONArray(itemsArrayString);
            if(itemsArray.length() > 0){
                for(int i=0; i<itemsArray.length(); i++){
                    Items newItemVal = new Items();
                    JSONObject jObj = itemsArray.optJSONObject(i);
                    newItemVal.id = jObj.optInt(KEY_ID);
                    newItemVal.itemName = jObj.optString(KEY_ITEM_NAME);
                    newItemVal.isChecked = jObj.optBoolean(KEY_CHECKED);
                    items.add(newItemVal);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }
}
