package com.obbo.edu.upostulez.hateoas.listener;

import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.obbo.edu.upostulez.hateoas.LinkUtil;
import com.obbo.edu.upostulez.hateoas.event.PaginatedResultsRetrievedEvent;

@SuppressWarnings({ "rawtypes" })
@Component
class PaginatedResultsRetrievedDiscoverabilityListener implements ApplicationListener<PaginatedResultsRetrievedEvent> {

	private static final String PAGE = "page";
	private static final String SUB_DOMAIN = "/";

	public PaginatedResultsRetrievedDiscoverabilityListener() {
		super();
	}

	// API

	@Override
	public final void onApplicationEvent(final PaginatedResultsRetrievedEvent ev) {
		Objects.requireNonNull(ev, "PaginatedResultsRetrievedEvent is null");

		addLinkHeaderOnPagedResourceRetrieval(ev.getUriBuilder(), ev.getResponse(), ev.getClazz(), ev.getPage(),
				ev.getTotalPages(), ev.getPageSize());
	}

	// - note: at this point, the URI is transformed into plural (added `s`) in a
	// hardcoded way - this will change in the future
	final void addLinkHeaderOnPagedResourceRetrieval(final UriComponentsBuilder uriBuilder,
			final HttpServletResponse response, final Class clazz, final int page, final int totalPages,
			final int pageSize) {
		plural(uriBuilder, clazz);

		final StringBuilder linkHeader = new StringBuilder();
		if (hasNextPage(page, totalPages)) {
			final String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
			linkHeader.append(LinkUtil.createLinkHeader(uriForNextPage, LinkUtil.REL_NEXT));
		}
		if (hasPreviousPage(page)) {
			final String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
			appendCommaIfNecessary(linkHeader);
			linkHeader.append(LinkUtil.createLinkHeader(uriForPrevPage, LinkUtil.REL_PREV));
		}
		if (hasFirstPage(page)) {
			final String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
			appendCommaIfNecessary(linkHeader);
			linkHeader.append(LinkUtil.createLinkHeader(uriForFirstPage, LinkUtil.REL_FIRST));
		}
		if (hasLastPage(page, totalPages)) {
			final String uriForLastPage = constructLastPageUri(uriBuilder, totalPages, pageSize);
			appendCommaIfNecessary(linkHeader);
			linkHeader.append(LinkUtil.createLinkHeader(uriForLastPage, LinkUtil.REL_LAST));
		}

		if (linkHeader.length() > 0) {
			response.addHeader(HttpHeaders.LINK, linkHeader.toString());
		}
	}

	final String constructNextPageUri(final UriComponentsBuilder uriBuilder, final int page, final int size) {
		return uriBuilder.replaceQueryParam(PAGE, page + 1).replaceQueryParam("size", size).build().encode()
				.toUriString();
	}

	final String constructPrevPageUri(final UriComponentsBuilder uriBuilder, final int page, final int size) {
		return uriBuilder.replaceQueryParam(PAGE, page - 1).replaceQueryParam("size", size).build().encode()
				.toUriString();
	}

	final String constructFirstPageUri(final UriComponentsBuilder uriBuilder, final int size) {
		return uriBuilder.replaceQueryParam(PAGE, 0).replaceQueryParam("size", size).build().encode().toUriString();
	}

	final String constructLastPageUri(final UriComponentsBuilder uriBuilder, final int totalPages, final int size) {
		return uriBuilder.replaceQueryParam(PAGE, totalPages).replaceQueryParam("size", size).build().encode()
				.toUriString();
	}

	final boolean hasNextPage(final int page, final int totalPages) {
		return page < (totalPages - 1);
	}

	final boolean hasPreviousPage(final int page) {
		return page > 0;
	}

	final boolean hasFirstPage(final int page) {
		return hasPreviousPage(page);
	}

	final boolean hasLastPage(final int page, final int totalPages) {
		return (totalPages > 1) && hasNextPage(page, totalPages);
	}

	final void appendCommaIfNecessary(final StringBuilder linkHeader) {
		if (linkHeader.length() > 0) {
			linkHeader.append(", ");
		}
	}

	// template

	protected void plural(final UriComponentsBuilder uriBuilder, final Class clazz) {
		final String resourceName = clazz.getSimpleName().toLowerCase() + "s";
		uriBuilder.path(SUB_DOMAIN + resourceName);
	}

}