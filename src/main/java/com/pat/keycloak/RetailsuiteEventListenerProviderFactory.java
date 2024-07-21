package com.pat.keycloak;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import com.pat.keycloak.domain.PubSubConfig;

public class RetailsuiteEventListenerProviderFactory
        implements EventListenerProviderFactory {

    private PubSubConfig cfg;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new RetailsuiteEventListenerProvider(keycloakSession, this.cfg);
    }

    @Override
    public void init(Config.Scope scope) {
        this.cfg = PubSubConfig.createFromScope(scope);
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "retailsuite-event-listener";
    }
}
