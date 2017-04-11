package communication;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import edu.carleton.syncronizedtodolists.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

public class JSONEventSource implements EventSource {
	private User user;
	private DataInputStream ois;
	private DataOutputStream oos;
	private Socket socket;
	private Gson gson;

	/*
	 * Allows streams to be created: input followed by output
	 */
	public JSONEventSource(InputStream is, OutputStream os) throws IOException {
		oos = new DataOutputStream(os);
		ois = new DataInputStream(is);
		gson = new Gson();
	}

	/*
	 * Allows streams to be created: output followed by input
	 */
	public JSONEventSource(OutputStream os, InputStream is) throws IOException {
		oos = new DataOutputStream(os);
		ois = new DataInputStream(is);
		gson = new Gson();
	}

	/*
	 * Designed for server-side usage when a socket has been accepted
	 */
	public JSONEventSource(Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream());
		this.socket = s;
		gson = new Gson();
	}

	@Override
	public Event getEvent() throws IOException, ClassNotFoundException {
		String input = ois.readUTF();
		System.out.println("getting: " + input);
		HashMap<String, Serializable> json = gson.fromJson(input, new HashMap<String, Serializable>().getClass());
		Event event = new Event(this, json);
		return event;
	}

	@SuppressWarnings("unchecked")
	public void putEvent(Event e) throws IOException {
		HashMap<String, Serializable> msg = e.getMap();
		msg.put(Fields.TYPE, e.type);
		String json = gson.toJson(msg);
		System.out.println("putting: " + json);
		oos.writeUTF(json);
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (oos != null)
				oos.close();
			if (ois != null)
				ois.close();
			socket = null;
			oos = null;
			ois = null;
		} catch (IOException e) {
			// Fail quietly
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
