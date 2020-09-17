package com.project.assessment.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
	public HeaderMapRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	private Map<String, String> headerMap = new HashMap<String, String>();

	public void addHeader(String name, String value) {
		this.headerMap.put(name, value);
	}

	@Override
	public String getHeader(String name) {
		String headerValue = super.getHeader(name);
		if (this.headerMap.containsKey(name)) {
			headerValue = this.headerMap.get(name);
		}
		return headerValue;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		ArrayList<String> names = Collections.list(super.getHeaderNames());
		for (String name : this.headerMap.keySet()) {
			names.add(name);
		}
		return Collections.enumeration(names);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		ArrayList<String> values = Collections.list(super.getHeaders(name));
		if (this.headerMap.containsKey(name)) {
			values.add(this.headerMap.get(name));
		}
		return Collections.enumeration(values);
	}
}
