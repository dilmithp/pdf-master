/**
 * Application layer for the gateway service.
 *
 * <p>Use-cases and orchestration logic that compose domain objects and outbound ports to fulfil
 * inbound requests. This layer may use Spring stereotypes (e.g. {@code @Service}) but must not
 * depend on inbound or outbound adapter implementations directly — it talks only to ports
 * (interfaces) defined either here or in the domain layer.
 */
package com.pdfmaster.gateway.application;
