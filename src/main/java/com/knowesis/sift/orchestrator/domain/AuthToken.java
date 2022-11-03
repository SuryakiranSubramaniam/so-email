package com.knowesis.sift.orchestrator.domain;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for process the response from AUTH API
 *
 */
public class AuthToken {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private String encryptAlgorithm;
	private String authTokenRequest;
	private String encryptPassword;
	/**
	 * authToken 
	 */
	private String authToken;
	/**
	 *  rest url
	 */
	private String restURL;
	/**
	 * This variable stores the timestamp at which the token expires
	 */
	private long tokenExpiryEpoch;
	
	@PostConstruct
	public void onPostConstruct() {
		authTokenRequest = System.getenv("AUTH_TOKEN_REQUEST");
		encryptPassword = System.getenv("CAMEL_ENCRYPTION_PASSWORD");
		encryptAlgorithm = System.getenv("SO_ENCRYPT_ALGORITHM");
	}
	/**
	 * 
	 * @return authToken returned
	 */
	public String getToken() {
		return authToken;
	}

	/**
	 * 
	 * @return restURL returned
	 */
	public String getRestURL() {
		return restURL;
	}
	
	/**
	 * Set authentication token in a string variable authToken
	 * Set rest URL  in a string variable restURL
	 * @param body Map. It is the auth token recieved from AUTH API
	 */
	public void setVariables(@Body Map<String,Object> body) {
		this.authToken = "Bearer " + (String) body.get("access_token");
		this.restURL = StringUtils.substringAfter((String) body.get("rest_instance_url"), "https://") ;
		//expires_in is in seconds, so convert to milliseconds and add current system time to get the time at which the token will expire
		this.tokenExpiryEpoch = Long.valueOf(body.get("expires_in").toString()) * 1000 + System.currentTimeMillis() ; 
		log.debug("Authorization Token,{}","Bearer " + (String) body.get("access_token"));
		log.debug("Rest URL,{}", (String) body.get("rest_instance_url"));
	}

	/**
	 *  set Auth token in a header <code> Authorization </code>
	 *  set rest url in a header <code> RestURL </code>
	 * @param headers Map. Is has all the exchange headers
	 */
	public void setAPIHeaders(@Headers Map<String,Object> headers) {
		headers.put("Authorization", getToken());
		headers.put("RestURL", getRestURL());
		log.debug("Authorization ->{}, RestURL->{}",getToken(),getRestURL());
	}
	
	public void checkTokenExpiry(@Headers Map<String,Object> headers) {
		if (System.currentTimeMillis() > tokenExpiryEpoch)
			headers.put("isTokenExpired", true);
		else
			headers.put("isTokenExpired", false);
	}
	
	/**
	 * Function to decrypt the AuthTokenRequest.
	 * 
	 * @return decrypted Request body.
	 */
	
	public String getAuthTokenRequest () {
		return decrypt(authTokenRequest);
	}
	
	/**
     *  Function to decrypt RequestBody
     */
    private String decrypt(String encodedReq) {
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptPassword);
        encryptor.setAlgorithm(encryptAlgorithm);
        String decryptedReq = encryptor.decrypt(encodedReq);
   	 	return decryptedReq; 
    }
}
