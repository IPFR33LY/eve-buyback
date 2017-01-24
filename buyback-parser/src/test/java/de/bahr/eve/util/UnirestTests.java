package de.bahr.eve.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UnirestTests {

	@Test
	public void projectContainsUnirest() throws UnirestException {
		Unirest.get("http://google.com").asString();
	}

	@Test
	public void unirestCanTalkHttps() throws UnirestException {
		Unirest.get("https://google.com").asString();
	}

	@Test
	public void unirestCanAccessSkyblade() throws UnirestException {
		Unirest.get("https://skyblade.de").asString();
	}

	@Test
	public void unirestCanAccessCrest() throws UnirestException {
		Unirest.get("https://crest-tq.eveonline.com").asString();
	}

	@Test
	public void unirestCanAccessXmlApi() throws UnirestException {
		Unirest.get("https://api.eveonline.com/server/ServerStatus.xml.aspx").asString();
	}

}
