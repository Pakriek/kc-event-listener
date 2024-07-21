package com.pat.keycloak.domain;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.util.JsonSerialization;

public class Message {
  private String action;
  private Map<String, String> data;

  private static final Logger log = Logger.getLogger(Message.class);

  public Message base(String action) {
    this.action = action;
    this.data = new HashMap<String, String>();
    return this;
  }

  public Message addData(String key, String value) {
    this.data.put(key, value);
    return this;
  }

  public String getAction() {
    return this.action;
  }

  public Map<String, String> getData() {
    return this.data;
  }

  public static String toJSON(final Object object, final boolean isPretty) {
    try {
      if (isPretty) {
        return JsonSerialization.writeValueAsPrettyString(object);
      }
      return JsonSerialization.writeValueAsString(object);

    } catch (final Exception ex) {
      log.error("[PUB] could not serialize keycloak Event", ex);
    }

    return "unparsable";
  }
}
