package com.rahmed.redhat.demo.rest;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "topic")
public class ConsumerTopic extends RouteBuilder {
	private String name;
	private String subcsribtionName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubcsribtionName() {
		return subcsribtionName;
	}

	public void setSubcsribtionName(String subcsribtionName) {
		this.subcsribtionName = subcsribtionName;
	}

	@Override
	public void configure() {
		from("activemq:topic:"+getName()+"?durableSubscriptionName="+getSubcsribtionName())
		.tracing().log("Message body is ${body}").end();

	}
}
