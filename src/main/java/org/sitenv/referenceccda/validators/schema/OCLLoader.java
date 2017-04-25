package org.sitenv.referenceccda.validators.schema;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

public class OCLLoader {

	private static ThreadLocal<Boolean> wasInit = new ThreadLocal<Boolean>();
	private static String operations = "org.openhealthtools.mdht.uml.cda.consol.operations";
	private static final Logger logger = Logger.getLogger(OCLLoader.class);

	public static Map<String, String> loadocl() {

		Map<String, String> r = new HashMap<String, String>();

		if (System.getProperty("mdht-skip-ocl-init") != null) {
			logger.info("Skipping OCLLoader.loadocl() call!!");
			return r;
		}

		String tn = Thread.currentThread().getName();
		r.put("Thread", tn);

		if (wasInit == null) {
			wasInit = new ThreadLocal<Boolean>();
			r.put("TLS", "Initialized");
		} else {
			r.put("TLS", "Already initialized");
		}

		if (wasInit.get() == null) {
			wasInit.set(Boolean.FALSE);
			r.put("TLS-Boolean", "Initialized");
		} else {
			r.put("TLS-Boolean", "Already initialized");
		}

		if (wasInit.get()) {
			logger.info(tn + " Load OCL done already...");
			r.put("Status", "0");
			r.put("Message", "Load OCL done already...");
			r.put("Elapsed", "0");
			return r;
		}

		logger.info(tn + " Loading OCLs...");
		long startTime = System.currentTimeMillis();

		try {
			final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
					false);
			// add include filters which matches all the classes (or use your
			// own)
			provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

			// get matching classes defined in the package
			final Set<BeanDefinition> classes = provider
					.findCandidateComponents("org.openhealthtools.mdht.uml.cda.consol.operations");

			for (BeanDefinition bean : classes) {

				Class<?> c2 = Class.forName(bean.getBeanClassName());

				for (Method method : c2.getDeclaredMethods()) {
					// System.out.println(method.getName());
					if (method.getName().startsWith("validate") && Modifier.isStatic(method.getModifiers())) {
						try {
							logger.debug("Pre-loading: " + c2.getName() + " Method:" + method.getName());
							method.invoke(null, null, null, null);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
					}
					// method.invoke(null, "whatever");
				}

				// System.out.println(c2.getCanonicalName());
			}
		} catch (Exception e) {
			r.put("Error", e.getMessage());
			logger.error("Error loading OCLs.", e);
		}

		long endTime = System.currentTimeMillis();
		long elaps = endTime - startTime;
		wasInit.set(Boolean.TRUE);
		r.put("Status", "1");
		r.put("Message", "Loaded OCL expressions.");
		r.put("Elapsed", String.valueOf(elaps));
		logger.warn(
				tn + " Thread NOT previously initialized. OCLLoader.init() Execution Time " + elaps + " milliseconds");
		return r;
	}

}
