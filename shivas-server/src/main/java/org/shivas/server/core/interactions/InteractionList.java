package org.shivas.server.core.interactions;

import java.util.List;

import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.NewInteractionEvent;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Lists;

public class InteractionList {
	
	private GameClient client;
	private List<Interaction> interactions = Lists.newArrayList();
	
	protected final EventDispatcher event = EventDispatchers.create();

	public InteractionList(GameClient client) {
		this.client = client;
	}

	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	public void unsubscribe(EventListener listener) {
		event.unsubscribe(listener);
	}
	
	public int size() {
		return interactions.size();
	}
	
	private void onAdded(Interaction interaction, boolean checkInvitation) {
		event.publish(new NewInteractionEvent(client, interaction));
		
		if (checkInvitation && interaction instanceof Invitation) {
			InteractionList target = ((Invitation) interaction).getTarget().interactions();
			target.onAdded(interaction, false);
		}
	}
	
	public <T extends Interaction> T push(final T action) {
		interactions.add(action);
		onAdded(action, true);
		
		return action;
	}
	
	public <T extends Interaction> T front(final T action) {
		interactions.add(0, action);
		onAdded(action, true);
		
		return action;
	}
	
	public Interaction current() {
		return interactions.get(interactions.size() - 1);
	}
	
	public <T extends Interaction> T current(Class<T> clazz) {
		return clazz.cast(current());
	}
	
	public Interaction remove() {
		Interaction interaction = interactions.remove(interactions.size() - 1);
		
		if (interaction instanceof Invitation) {
			Invitation invitation = (Invitation) interaction;
			invitation.getTarget().interactions().interactions.remove(interaction);
		}
		
		return interaction;
	}
	
	public <T extends Interaction> T remove(Class<T> clazz) {
		return clazz.cast(remove());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Interaction> T removeIf(InteractionType... types) {
		Interaction current = current();
		for (InteractionType type : types) {
			if (current.getInteractionType() == type) {
				return (T) remove();
			}
		}
		throw new ClassCastException();
	}
	
}