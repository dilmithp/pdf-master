package com.pdfmaster.notification.domain;

import java.util.Map;

/** Async payload published on the {@code notifications} exchange and consumed by this service. */
public record NotificationEvent(
    String templateCode, String locale, String recipient, Map<String, String> vars) {}
