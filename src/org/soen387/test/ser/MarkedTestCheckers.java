package org.soen387.test.ser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class MarkedTestCheckers {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	public final String BASE_URL = FieldMap.current.get().get("BASE_URL");
	XPath xPath =  XPathFactory.newInstance().newXPath();
	
	@ScoreAnnotation(1)
	@Test
	public void testLogIn() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = login("fred", "fred", httpclient);
		
		//Should fail, since I haven't registered them yet
		assertFailure(d);
		
		register("fred", "fred", "fred", "fredson", "fred@fred.com", httpclient);
		
		//I have now registered this user, so it should pass
		d = login("fred", "fred", httpclient);
		assertSuccess(d);
	}

	@ScoreAnnotation(1)
	@Test
	public void testUsernamesUnique() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("fred2", "fred2", "fred2", "fredson2", "fred2@fred.com", httpclient);
		d = register("fred2", "fred3", "fred3", "fredson3", "fred3@fred.com", httpclient);
		assertFailure(d);
	}
	
	@ScoreAnnotation(1)
	@Test
	public void testEmailsUnique() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("fred4", "fred4", "fred4", "fredson4", "fred4@fred.com", httpclient);
		d = register("fred5", "fred5", "fred5", "fredson5", "fred4@fred.com", httpclient);
		assertFailure(d);
	}
	
	@ScoreAnnotation(1)
	@Test
	public void testLogOut() throws SAXException, IOException, XpathException, XPathExpressionException {
		login("fred", "fred", httpclient);
		//A full test might then try to challenge a user that one has not challenged to confirmed that one is not allowed
		//to do so when not logged in.
		Document d = logout(httpclient);
		assertSuccess(d);
	}
	
	@ScoreAnnotation(1)
	@Test
	public void testRegister() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("bob", "bob", "bob", "bobson", "bob@fred.com", httpclient);
		long id = getPlayerId(d);
		XMLAssert.assertXpathExists("/checkers/player[@firstName='bob']", d);
		XMLAssert.assertXpathExists("/checkers/player[@lastName='bobson']", d);
		XMLAssert.assertXpathExists("/checkers/player[@email='bob@fred.com']", d);
		XMLAssert.assertXpathExists("/checkers/player/user[@username='bob']", d);
		d = login("bob", "bob", httpclient);
		assertSuccess(d);
		d = listPlayers(httpclient);
		XMLAssert.assertXpathExists("/checkers/players/player[@id='" + id + "']", d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testListUsers() throws SAXException, IOException, XpathException, XPathExpressionException {
		register("alice", "alice", "alice", "aliceson", "alice@fred.com", httpclient);
		register("dora", "dora", "dora", "dorason", "dora@fred.com", httpclient);
		Document d = listPlayers(httpclient);
		XMLAssert.assertXpathExists("/checkers/players/player[@firstName='alice']", d);
		XMLAssert.assertXpathExists("/checkers/players/player[@firstName='dora']", d);
		assertSuccess(d);
	}


	@ScoreAnnotation(2)
	@Test
	public void testViewUserStats() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("elsa", "elsa", "elsa", "elsason", "elsa@fred.com", httpclient);
		long elsaId = getPlayerId(d);
		register("george", "george", "george", "georgeson", "george@fred.com", httpclient);
		login("george", "george", httpclient);
		
		d = challengePlayer(elsaId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		login("elsa", "elsa", httpclient);
		respondToChallenge(challengeId, challengeVersion, true, httpclient);

		d = viewPlayerStats(elsaId, httpclient);
		
		XMLAssert.assertXpathExists("/checkers/player[@firstName='elsa']/games/game/firstPlayer[@refid='" + elsaId + "'] ", d);
		
		
		assertSuccess(d);
	}
	
	@ScoreAnnotation(3)
	@Test
	public void testChallengeUser() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("hal", "hal", "hal", "halson", "hal@fred.com", httpclient);
		long halId = getPlayerId(d);
		register("igor", "igor", "igor", "igorson", "igor@fred.com", httpclient);
		login("igor", "igor", httpclient);
		
		d = challengePlayer(halId, httpclient);
		long challengeId = getChallengeId(d);

		d = listChallenges(httpclient);
		
		XMLAssert.assertXpathExists("/checkers/challenges/challenge[@id='" + challengeId + "'] ", d);
		
		
		assertSuccess(d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testCannotChallengeSelf() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("hal2", "hal2", "hal2", "halson2", "hal2@fred.com", httpclient);
		long halId = getPlayerId(d);
		login("hal2", "hal2", httpclient);
		
		d = challengePlayer(halId, httpclient);
		assertFailure(d);
	}
	
	@ScoreAnnotation(3)
	@Test
	public void testRespondToChallenge() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse", "jesse", "jesse", "jesseson", "jesse@fred.com", httpclient);
		long jesseId = getPlayerId(d);
		register("kathryn", "kathryn", "kathryn", "kathrynson", "kathryn@fred.com", httpclient);
		login("kathryn", "kathryn", httpclient);
		
		d = challengePlayer(jesseId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		login("jesse", "jesse", httpclient);
		respondToChallenge(challengeId, challengeVersion, false, httpclient);

		d = viewPlayerStats(jesseId, httpclient);
		
		XMLAssert.assertXpathNotExists("/checkers/player[@firstName='jesse']/games/game", d);
		
		d = listChallenges(httpclient);
		
		XMLAssert.assertXpathExists("/checkers/challenges/challenge[@id='" + challengeId + "' and @status='2'] ", d);
		
		assertSuccess(d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testCannotRespondToChallengeYouAreNotPartOf() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse2", "jesse2", "jesse2", "jesseson2", "jesse2@fred.com", httpclient);
		long jesseId = getPlayerId(d);
		register("jesse3", "jesse3", "jesse3", "jesseson3", "jesse3@fred.com", httpclient);
		
		register("kathryn2", "kathryn2", "kathryn2", "kathrynson2", "kathryn2@fred.com", httpclient);
		login("kathryn2", "kathryn2", httpclient);
		
		d = challengePlayer(jesseId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		login("jesse3", "jesse3", httpclient);
		d = respondToChallenge(challengeId, challengeVersion, false, httpclient);

		assertFailure(d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testCannotChallengeWithOpenChallenge() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse4", "jesse4", "jesse4", "jesseson4", "jesse4@fred.com", httpclient);
		long jesseId = getPlayerId(d);
		
		d = register("kathryn3", "kathryn3", "kathryn3", "kathrynson3", "kathryn3@fred.com", httpclient);
		long kathrynId = getPlayerId(d);
		
		login("kathryn3", "kathryn3", httpclient);
		
		d = challengePlayer(jesseId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		logout(httpclient);
		login("jesse4", "jesse4", httpclient);
		d = respondToChallenge(challengeId, challengeVersion, false, httpclient);

		d = challengePlayer(kathrynId, httpclient);
		challengeId = getChallengeId(d);
		challengeVersion = getChallengeVersion(d);
		assertSuccess(d);
		
		logout(httpclient);
		login("kathryn3", "kathryn3", httpclient);
		d = challengePlayer(jesseId, httpclient);
		
		assertFailure(d);
	}
	
	@ScoreAnnotation(1)
	@Test
	public void testCannotChallengeWithOpenGame() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse5", "jesse5", "jesse5", "jesseson5", "jesse5@fred.com", httpclient);
		long jesseId = getPlayerId(d);
		
		d = register("kathryn4", "kathryn4", "kathryn4", "kathrynson4", "kathryn4@fred.com", httpclient);

		login("kathryn4", "kathryn4", httpclient);
		
		d = challengePlayer(jesseId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		logout(httpclient);
		login("jesse5", "jesse5", httpclient);
		d = respondToChallenge(challengeId, challengeVersion, true, httpclient);
		
		logout(httpclient);
		login("kathryn3", "kathryn3", httpclient);
		d = challengePlayer(jesseId, httpclient);
		
		assertFailure(d);
	}
	
	@ScoreAnnotation(1)
	@Test
	public void testLostUpdateOnChallengeAcceptance() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("jesse6", "jesse6", "jesse6", "jesseson6", "jesse6@fred.com", httpclient);
		long jesseId = getPlayerId(d);
		
		d = register("kathryn5", "kathryn5", "kathryn5", "kathrynson5", "kathryn5@fred.com", httpclient);

		login("kathryn5", "kathryn5", httpclient);
		
		d = challengePlayer(jesseId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d)-1;
		
		logout(httpclient);
		login("jesse5", "jesse5", httpclient);
		d = respondToChallenge(challengeId, challengeVersion, true, httpclient);
		
		assertFailure(d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testListGames() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("lisa", "lisa", "lisa", "lisason", "lisa@fred.com", httpclient);
		long lisaId = getPlayerId(d);

		d = register("mason", "mason", "mason", "masonson", "mason@fred.com", httpclient);
		long masonId = getPlayerId(d);
		
		d = register("nancy", "nancy", "nancy", "nancyson", "nancy@fred.com", httpclient);
		long nancyId = getPlayerId(d);
		
		login("nancy", "nancy", httpclient);
		
		d = challengePlayer(lisaId, httpclient);
		long challengeId1 = getChallengeId(d);
		int challengeVersion1 = getChallengeVersion(d);
		
		d = challengePlayer(masonId, httpclient);
		long challengeId2 = getChallengeId(d);
		int challengeVersion2 = getChallengeVersion(d);
		
		login("lisa", "lisa", httpclient);
		respondToChallenge(challengeId1, challengeVersion1, true, httpclient);
		
		login("mason", "mason", httpclient);
		respondToChallenge(challengeId2, challengeVersion2, true, httpclient);

		logout(httpclient);
		
		d = listGames(httpclient);
		
		XMLAssert.assertXpathExists("/checkers/games/game/firstPlayer[@refid='" + lisaId + "'] ", d);
		XMLAssert.assertXpathExists("/checkers/games/game/firstPlayer[@refid='" + masonId + "'] ", d);
		XMLAssert.assertXpathExists("/checkers/games/game/secondPlayer[@refid='" + nancyId + "'] ", d);
		XMLAssert.assertXpathNotExists("/checkers/games/game/firstPlayer[@refid='" + nancyId + "'] ", d);
		
		System.out.println("what");
		assertSuccess(d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testViewGame() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("ollie", "ollie", "ollie", "ollieson", "ollie@fred.com", httpclient);
		long ollieId = getPlayerId(d);
		
		d = register("percy", "percy", "percy", "percyson", "percy@fred.com", httpclient);
		long percyId = getPlayerId(d);
		
		login("percy", "percy", httpclient);
		
		d = challengePlayer(ollieId, httpclient);
		long challengeId = getChallengeId(d);
		int challengeVersion = getChallengeVersion(d);
		
		login("ollie", "ollie", httpclient);
		respondToChallenge(challengeId, challengeVersion, true, httpclient);

		d = listGames(httpclient);
		
		XMLAssert.assertXpathExists("/checkers/games/game/firstPlayer[@refid='" + ollieId + "']/../secondPlayer[@refid='" + percyId + "'] ", d);
		long gameId = getGameId(d, ollieId, percyId);
		
		d = viewGame(gameId, httpclient);
		XMLAssert.assertXpathExists("/checkers/game[@id='" + gameId + "']/firstPlayer[@refid='" + ollieId + "'] ", d);
		XMLAssert.assertXpathExists("/checkers/game[@id='" + gameId + "']/currentPlayer[@refid='" + ollieId + "'] ", d);
		XMLAssert.assertXpathExists("/checkers/game[@id='" + gameId + "']/secondPlayer[@refid='" + percyId + "'] ", d);
		XMLAssert.assertXpathExists("/checkers/game[@id='" + gameId + "']/pieces[.=\"b b b b  b b b bb b b b                  r r r rr r r r  r r r r\"]", d);

		assertSuccess(d);
	}
	
	@ScoreAnnotation(2)
	@Test
	public void testViewChallenges() throws SAXException, IOException, XpathException, XPathExpressionException {
		Document d = register("q", "q", "q", "qson", "q@fred.com", httpclient);
		long qId = getPlayerId(d);
		d = register("ryan", "ryan", "ryan", "ryanson", "ryan@fred.com", httpclient);
		long ryanId = getPlayerId(d);
		login("ryan", "ryan", httpclient);
		d = listChallenges(httpclient);
		XMLAssert.assertXpathNotExists("/checkers/challenges/challenge/challenger[@refid='" + ryanId + "'] ", d);
		XMLAssert.assertXpathNotExists("/checkers/challenges/challenge/challengee[@refid='" + ryanId + "'] ", d);
		
		d = challengePlayer(qId, httpclient);

		d = listChallenges(httpclient);
		
		XMLAssert.assertXpathExists("/checkers/challenges/challenge/challenger[@refid='" + ryanId + "'] ", d);
		XMLAssert.assertXpathNotExists("/checkers/challenges/challenge/challengee[@refid='" + ryanId + "'] ", d);
		
		assertSuccess(d);
	}
	
	public final String LOGIN = BASE_URL+FieldMap.current.get().get("LOGIN_PATH");
	public Document login(String user, String pass, CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LOGIN);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("USERNAME_PARAM"), user));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PASSWORD_PARAM"), pass));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}

	public final String LOGOUT = BASE_URL+FieldMap.current.get().get("LOGOUT_PATH");
	public Document logout(CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LOGOUT);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String REGISTER = BASE_URL+FieldMap.current.get().get("REGISTER_PATH");
	public Document register(String user, String pass, String first, String last, String email, CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(REGISTER);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("USERNAME_PARAM"), user));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PASSWORD_PARAM"), pass));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("FIRSTNAME_PARAM"), first));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("LASTNAME_PARAM"), last));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("EMAIL_PARAM"), email));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_PLAYERS = BASE_URL+FieldMap.current.get().get("LIST_PLAYERS_PATH");
	public Document listPlayers(CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LIST_PLAYERS);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String VIEW_PLAYER_STATS = BASE_URL+FieldMap.current.get().get("VIEW_PLAYER_STATS_PATH");
	public Document viewPlayerStats(long id, CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(VIEW_PLAYER_STATS);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("PLAYER_PARAM"), ""+id));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String CHALLENGE_PLAYER = BASE_URL+FieldMap.current.get().get("CHALLENGE_PLAYER_PATH");
	public Document challengePlayer(long id, CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(CHALLENGE_PLAYER);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("TARGET_PLAYER_PARAM"), ""+id));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String RESPOND_TO_CHALLENGE = BASE_URL+FieldMap.current.get().get("RESPOND_TO_CHALLNGE_PATH");
	public Document respondToChallenge(long challenge, int version, boolean accept, CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(RESPOND_TO_CHALLENGE);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("CHALLENGE_PARAM"), ""+challenge));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("CHALLENGE_VERSION_PARAM"), ""+version));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("CHALLENGE_RESPONSE_PARAM"), accept?FieldMap.current.get().get("CHALLENGE_ACCEPT_VALUE"):FieldMap.current.get().get("CHALLENGE_REFUSE_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}	
	
	public final String LIST_GAMES = BASE_URL+FieldMap.current.get().get("LIST_GAMES_PATH");
	public Document listGames(CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LIST_GAMES);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String VIEW_GAME = BASE_URL+FieldMap.current.get().get("VIEW_GAME_PATH");
	public Document viewGame(long game, CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(VIEW_GAME);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("GAME_PARAM"), ""+game));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	public final String LIST_CHALLENGES = BASE_URL+FieldMap.current.get().get("LIST_CHALLENGES_PATH");
	public Document listChallenges(CloseableHttpClient closeableHttpClient) throws ParseException, ClientProtocolException, IOException, SAXException {
		
		HttpPost httpPost = new HttpPost(LIST_CHALLENGES);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(FieldMap.current.get().get("XML_PARAM"), FieldMap.current.get().get("XML_VALUE")));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse requestResponse = closeableHttpClient.execute(httpPost);
		String response = EntityUtils.toString(requestResponse.getEntity());
		String details = prettyPrintPost(httpPost, nvps, response);
		System.out.println(details);
		requestResponse.close();
		return XMLUnit.buildControlDocument(response);
	}
	
	private String prettyPrintPost(HttpPost httpPost, List<NameValuePair> nvps,
			String response) {
		StringBuilder sb = new StringBuilder();
		sb.append("******Request"+"\n");
		sb.append(httpPost+"\n");
		for(NameValuePair nvp: nvps) {
			sb.append("\t" + nvp.toString()+"\n");
		}
		
		sb.append("******Response"+"\n");
		sb.append(response);
		return  FieldMap.current.get().get("DEBUG").equals("true")?sb.toString():"";
	}

	private long getPlayerId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/checkers/player/@id", d, XPathConstants.STRING));
	}
	
	private long getGameId(Document d, long firstPlayer, long secondPlayer) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/checkers/games/game/firstPlayer[@refid='" + firstPlayer + "']/../secondPlayer[@refid='" + secondPlayer + "']/../@id", d, XPathConstants.STRING));
	}
	
	private long getChallengeId(Document d) throws NumberFormatException, XPathExpressionException {
		return Long.parseLong((String) xPath.evaluate("/checkers/challenge/@id", d, XPathConstants.STRING));
	}
	
	private int getChallengeVersion(Document d) throws NumberFormatException, XPathExpressionException {
		return Integer.parseInt((String) xPath.evaluate("/checkers/challenge/@version", d, XPathConstants.STRING));
	}
	
	public void assertSuccess(Document d) throws XpathException, XPathExpressionException {
		XMLAssert.assertXpathExists("/checkers/status[.=\"success\"]", d);
	}
	
	public void assertFailure(Document d) throws XpathException, XPathExpressionException {
		XMLAssert.assertXpathNotExists("/checkers/status[.=\"success\"]", d);
	}

	
}


