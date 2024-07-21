package com.pat.keycloak.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.keycloak.events.admin.AdminEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
@JsonTypeInfo(use = Id.CLASS)
public class PubSubAttributes implements Serializable {

  public static Map<String, String> createMap(final AdminEvent adminEvent) {
    final Map<String, String> map = new HashMap<>();
    map.put("eventType", "ADMIN");
    map.put("realmId", adminEvent.getRealmId());
    map.put("errorStatus", (adminEvent.getError() != null ? "ERROR" : "SUCCESS"));
    map.put("resourceType", adminEvent.getResourceTypeAsString());
    map.put("operationType", adminEvent.getOperationType().toString());
    return map;
  }
}