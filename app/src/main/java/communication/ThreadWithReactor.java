package communication;

import java.io.IOException;

public class ThreadWithReactor extends Thread implements ReactorInterface {
	private boolean running;
	private EventSource source;
	private Reactor reactor;

	public ThreadWithReactor(EventSource source) {
		this.source = source;
		this.running = false;
		this.reactor = new Reactor();
	}

	public ThreadWithReactor(EventSource source, Reactor reactor) {
		this.source = source;
		this.running = false;
		this.reactor = reactor;
	}

	public void quit() {
		running = false;
	}

	public void run() {
		System.out.println("thread running");
		running = source != null;
		while (running) {
			Event event;
			try {
				System.out.println("Getting event");
				event = source.getEvent();
				System.out.println("Event got");
				if (event != null) {
					System.out.println("in");
					try {
						dispatch(event);
					} catch (NoEventHandler e) {
						System.out.println("none");
						running = false;
					}
				} else
					quit();
			} catch (IOException e1) {
				quit();
			} catch (ClassNotFoundException e1) {
				quit();
			}
		}
	}

	@Override
	public void register(String type, EventHandler event) {
		reactor.register(type, event);
	}

	@Override
	public void deregister(String type) {
		reactor.deregister(type);
	}

	@Override
	public void dispatch(Event event) throws NoEventHandler {
		reactor.dispatch(event);
	}

	public EventSource getEventSource() {
		return source;
	}
}
