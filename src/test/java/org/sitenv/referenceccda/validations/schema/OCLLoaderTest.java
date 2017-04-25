package org.sitenv.referenceccda.validations.schema;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.sitenv.referenceccda.validators.schema.OCLLoader;

public class OCLLoaderTest {

	@Test
	public void testOCLLoader() {
		// First time call
		OCLLoader.loadocl();
		// Second time call.
		Map<String,String> result = OCLLoader.loadocl();
		assertEquals("Second time should say already done","0", result.get("Status"));
	}
}
