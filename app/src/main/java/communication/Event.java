package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Event implements EventSource{
	private EventSource source;
	private HashMap<String, Serializable> map;
	public String type;
	
	public Event(EventSource s, HashMap<String, Serializable> m, String t){
		source = s;
		map = m;
		type = t;
	}
	
	public Event(EventSource s, HashMap<String, Serializable> m){
		source = s;
		type = (String) m.remove(Fields.TYPE);
		map = m;
	}
	
	public Serializable get(String key) {
		return map.get(key);
	}
	
	public HashMap<String, Serializable> getMap(){
		return map;
	}

	@Override
	public Event getEvent() throws IOException, ClassNotFoundException {
		return source.getEvent();
	}

	@Override
	public void putEvent(Event e) throws IOException {
		source.putEvent(e);
	}
}
