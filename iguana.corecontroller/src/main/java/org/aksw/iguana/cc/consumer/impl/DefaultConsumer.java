package org.aksw.iguana.cc.consumer.impl;

import org.aksw.iguana.commons.rabbit.RabbitMQUtils;

import java.io.ByteArrayInputStream;

import org.aksw.iguana.cc.config.ConfigManager;
import org.aksw.iguana.commons.consumer.AbstractConsumer;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Consumer will try to consume the received data as a {@link org.apache.commons.configuration.Configuration}
 * 
 * @author f.conrads
 *
 */
public class DefaultConsumer extends AbstractConsumer{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsumer.class);
	
	private ConfigManager cmanager;

	/**
	 * @param cmanager
	 */
	public DefaultConsumer(ConfigManager cmanager){
		this.cmanager=cmanager;
	}
	
	public void consume(byte[] data){
		if (data == null) {
            System.out.println(data);
        } else {
	        Configuration config  = RabbitMQUtils.getObject(data);
	        if(config!=null){
	        	cmanager.receiveData(config);
	        }
	        else {
	        	//is send as string and not as config
	        	config = new PropertiesConfiguration();
	        	//load from byte array 
	        	try {
					((PropertiesConfiguration)config).load(new ByteArrayInputStream(data));
					cmanager.receiveData(config);
	        	} catch (ConfigurationException e) {
					LOGGER.error("Could not read configuration. Must ignore it... Sorry :(", e);
				}
	        }
        }
	}
	
}
