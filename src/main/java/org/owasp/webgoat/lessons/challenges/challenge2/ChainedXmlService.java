/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import java.io.StringReader;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class ChainedXmlService {

  private static final int MAX_XML_LENGTH = 4096;
  private static final int MAX_EXTERNAL_ENTITIES = 4;

  private final ChainedFetchService fetchService;

  public ChainedXmlService(ChainedFetchService fetchService) {
    this.fetchService = fetchService;
  }

  String parse(String xml, Challenge2State state, URI importUri) {
    if (xml == null || xml.length() > MAX_XML_LENGTH) {
      throw new IllegalArgumentException("XML must be at most 4096 characters");
    }
    try {
      AtomicBoolean reachedInternalThroughRedirect = new AtomicBoolean();
      InputSource source = new InputSource(new StringReader(xml));
      source.setSystemId(importUri.toString());
      Document document = newDocumentBuilder(state, importUri, reachedInternalThroughRedirect).parse(source);
      if (!"feed".equals(document.getDocumentElement().getTagName())) {
        throw new IllegalArgumentException("Expected a feed document");
      }
      if (reachedInternalThroughRedirect.get()) {
        state.recordXmlChain();
      }
      return document.getDocumentElement().getTextContent().strip();
    } catch (Exception e) {
      throw new IllegalArgumentException("XML could not be processed within this lesson", e);
    }
  }

  private DocumentBuilder newDocumentBuilder(
      Challenge2State state, URI importUri, AtomicBoolean reachedInternalThroughRedirect)
      throws ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setXIncludeAware(false);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", true);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

    DocumentBuilder builder = factory.newDocumentBuilder();
    AtomicInteger entityCount = new AtomicInteger();
    builder.setEntityResolver(
        (publicId, systemId) ->
            resolveEntity(systemId, state, importUri, entityCount, reachedInternalThroughRedirect));
    return builder;
  }

  private InputSource resolveEntity(
      String systemId,
      Challenge2State state,
      URI importUri,
      AtomicInteger entityCount,
      AtomicBoolean reachedInternalThroughRedirect)
      throws SAXException {
    if (entityCount.incrementAndGet() > MAX_EXTERNAL_ENTITIES) {
      throw new SAXException("Too many external entities");
    }
    if (systemId == null) {
      throw new SAXException("External entity needs a lesson URL");
    }

    ChainedFetchService.FetchResult result;
    try {
      result = fetchService.fetch(importUri.resolve(systemId).toString(), state, importUri);
    } catch (IllegalArgumentException e) {
      throw new SAXException("External entity is outside this lesson", e);
    }
    if (result.reachedInternal() && result.redirects() > 0) {
      reachedInternalThroughRedirect.set(true);
    }
    return new InputSource(new StringReader(result.body()));
  }
}
