package com.rushucloud.eip.config;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * Register this with the DispatcherServlet in a ServletInitializer class like:
 * dispatcherServlet.setContextInitializers(new PropertiesInitializer());
 */
public class PropertiesInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {
	private static final Logger log = LogManager
			.getLogger(PropertiesInitializer.class);

	/**
	 * Runs as appInitializer so properties are wired before spring beans
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment env = applicationContext.getEnvironment();

		String[] activeProfiles = getActiveProfiles(env);

		for (String profileName : activeProfiles) {
			log.info("Loading properties for Spring Active Profile: {}",
					profileName);
			try {
				ResourcePropertySource propertySource = new ResourcePropertySource(
						profileName + "EnvProperties", "classpath:application-"
								+ profileName + ".properties");

				env.getPropertySources().addLast(propertySource);
			} catch (IOException e) {
				log.error(
						"ERROR during environment properties setup - TRYING TO LOAD: "
								+ profileName, e);

				// Okay to silently fail here, as we might have profiles that do
				// not have properties files (like dev1, dev2, etc)
			}
		}
	}

	/**
	 * Returns either the ActiveProfiles, or if empty, then the DefaultProfiles
	 * from Spring
	 */
	protected String[] getActiveProfiles(ConfigurableEnvironment env) {
		String[] activeProfiles = env.getActiveProfiles();
		if (activeProfiles.length > 0) {
			log.info("Using registered Spring Active Profiles: {}",
					StringUtils.join(activeProfiles, ", "));
			return activeProfiles;
		}

		String[] defaultProfiles = env.getDefaultProfiles();
		log.info(
				"No Active Profiles found, using Spring Default Profiles: {}",
				StringUtils.join(defaultProfiles, ", "));
		return defaultProfiles;
	}

}
