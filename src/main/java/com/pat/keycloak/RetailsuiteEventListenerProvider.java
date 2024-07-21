package com.pat.keycloak;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

import com.pat.keycloak.domain.Message;
import com.pat.keycloak.domain.PubSub;
import com.pat.keycloak.domain.PubSubAttributes;
import com.pat.keycloak.domain.PubSubConfig;

public class RetailsuiteEventListenerProvider
        implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(RetailsuiteEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;
    private final PubSubConfig cfg;

    public RetailsuiteEventListenerProvider(KeycloakSession session, PubSubConfig cfg) {
        this.session = session;
        this.model = session.realms();
        this.cfg = cfg;
    }

    @Override
    public void onEvent(Event event) {
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        log.infof("onEvent(AdminEvent)");
        log.infof("Resource path: %s", adminEvent.getResourcePath());
        log.infof("Resource type: %s", adminEvent.getResourceType());
        log.infof("Operation type: %s", adminEvent.getOperationType());
        if (ResourceType.USER.equals(adminEvent.getResourceType())) {
            RealmModel realm = this.model.getRealm(adminEvent.getRealmId());
            String userId = adminEvent.getResourcePath().split("/")[1];
            UserModel user = this.session.users().getUserById(realm, userId);
            final String message = Message.toJSON(adminEvent, true);
            // Message message = new
            // Message().base(adminEvent.getOperationType().toString()).addData("id",
            // userId);
            /*
             * if (!(OperationType.DELETE.equals(adminEvent.getOperationType()))) {
             * message.addData("email", user.getEmail());
             * message.addData("active", user.isEnabled() ? "true" : "false");
             * }
             */
            PubSub.publishMessage(log, message, PubSubAttributes.createMap(adminEvent), cfg);
        }
    }

    @Override
    public void close() {
    }
}
