package com.obbo.edu.upostulez.hateoas.listener;

import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.obbo.edu.upostulez.hateoas.LinkUtil;
import com.obbo.edu.upostulez.hateoas.event.SingleResourceRetrievedEvent;

@Component
class SingleResourceRetrievedDiscoverabilityListener implements ApplicationListener<SingleResourceRetrievedEvent> {

	@Override
	public void onApplicationEvent(final SingleResourceRetrievedEvent resourceRetrievedEvent) {
		Objects.requireNonNull(resourceRetrievedEvent, "SingleResourceRetrievedEvent is null");

		final HttpServletResponse response = resourceRetrievedEvent.getResponse();
		addLinkHeaderOnSingleResourceRetrieval(response);
	}

	void addLinkHeaderOnSingleResourceRetrieval(final HttpServletResponse response) {
		final String requestURL = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
		final int positionOfLastSlash = requestURL.lastIndexOf("/");
		final String uriForResourceCreation = requestURL.substring(0, positionOfLastSlash);

		final String linkHeaderValue = LinkUtil.createLinkHeader(uriForResourceCreation, "collection");
		response.addHeader(HttpHeaders.LINK, linkHeaderValue);
	}
}
