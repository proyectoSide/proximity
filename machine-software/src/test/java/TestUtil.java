import org.springframework.http.HttpEntity;

import com.google.gson.Gson;

public class TestUtil {

	public static HttpEntity extracted(Class c, String text) {
		Gson g = new Gson();
		return new HttpEntity<>(g.fromJson(text.replaceAll("'", "\""), c));
	}
	
}
