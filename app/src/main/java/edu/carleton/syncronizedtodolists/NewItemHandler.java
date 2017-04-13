package edu.carleton.syncronizedtodolists;

import com.google.gson.Gson;

import communication.Event;
import communication.EventHandler;
import communication.Fields;

/**
 * Created by nicholasrizzo on 2017-04-13.
 */

public class NewItemHandler implements EventHandler {
    @Override
    public void handleEvent(Event event) {
        Gson gson = new Gson();
        MainActivity ma = MainActivity.getInstance();
        int listid = ((Double) event.get(Fields.LIST_ID)).intValue();
        List list = null;
        for(List l: ma.getUser().getLists()){
            if(l.getId() == listid){
                list = l;
                break;
            }
        }
        String itemstr = event.get(Fields.ITEM).toString();
        Item item = gson.fromJson(itemstr, Item.class);
        String creater = item.getCreatedBy();
        if(creater.equals(ma.getUser())){
            list.getItems().get(list.getItems().size()-1).setId(item.getId());
        }
        else{
            list.getItems().add(item);
        }
    }
}
