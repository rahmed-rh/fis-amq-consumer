package com.rahmed.redhat.demo.rest;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "activemq")
public class AMQSetup {

	final static Logger LOG = LoggerFactory.getLogger(AMQSetup.class);
	static String keyStorePath = "amq-client.jks";
	static String keyStorePassword = "passw0rd";
	static String keyStoreType = "JKS";
	static String trustStorePath = "amq-client.jks";
	static String trustStorePassword = "passw0rd";
	static String trustStoreType = "JKS";

	private String host;
	private Integer port;
	private String username;
	private String password;
	private String clientId;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	private String getBrokerUrl() {
		// return "tcp://" + host + ":" + port;
		return "failover://ssl://" + host + ":" + port;
	}

	/*
	 * @Bean ActiveMQConnectionFactory amqConnectionFactory() { LOG.info("host=" +
	 * host + ", port=" + port + ", username=" + username + ", password=" +
	 * password); return new ActiveMQConnectionFactory(username, password,
	 * getBrokerUrl());
	 * 
	 * }
	 */

	@Bean
	ActiveMQConnectionFactory amqConnectionFactory() {
		LOG.info("host=" + host + ", port=" + port + ", username=" + username + ", password=" + password);

		ActiveMQSslConnectionFactory activeMQSslConnectionFactory = new ActiveMQSslConnectionFactory(getBrokerUrl());

		activeMQSslConnectionFactory.setUserName(username);
		activeMQSslConnectionFactory.setPassword(password);

		try {
			activeMQSslConnectionFactory.setKeyStore(keyStorePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activeMQSslConnectionFactory.setKeyStorePassword(keyStorePassword);
		activeMQSslConnectionFactory.setKeyStoreType(keyStoreType);

		try {
			activeMQSslConnectionFactory.setTrustStore(trustStorePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activeMQSslConnectionFactory.setTrustStorePassword(trustStorePassword);
		activeMQSslConnectionFactory.setTrustStoreType(trustStoreType);
		// In case we are going to autoscale in OCP I used the downward API to get namespace_pod-name
		activeMQSslConnectionFactory.setClientID(getClientId());
		return activeMQSslConnectionFactory;

	}

	@Bean
	JmsTransactionManager jmsTransactionManager(@Autowired ActiveMQConnectionFactory amqConnectionFactory) {
		return new JmsTransactionManager(amqConnectionFactory);

	}

	@Bean
	ActiveMQComponent activemq(@Autowired JmsTransactionManager jmsTransactionManager,
			@Autowired ActiveMQConnectionFactory connectionFactory) {
		ActiveMQComponent activemq = new ActiveMQComponent();
		activemq.setTransactionManager(jmsTransactionManager);
		activemq.setConnectionFactory(connectionFactory);

		// activemq.setBrokerURL(getBrokerUrl());
		// activemq.setUserName(username);
		// activemq.setPassword(password);
		return activemq;

	}

}
