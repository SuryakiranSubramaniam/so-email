package com.knowesis.sift.orchestrator;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.knowesis.sift.orchestrator.domain.Constants;
import com.knowesis.sift.orchestrator.exception.CacheKeyNotFoundException;
import com.knowesis.sift.orchestrator.utils.SOCacheCluster;

import java.math.BigInteger; 
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

/**
 * This class is responsible for creating request of dehash service
 *
 */
public class DHRequestProcessor{

    private Logger log = LoggerFactory.getLogger(DHRequestProcessor.class);
    /**
     * X-AUTH-TOKEN
     */
    public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    
    /**
     * This method prepares the request for dehashing service. It gets the original message from Exchange properties and extracts the PIIs (i.e. hashed values). <br>
     * The PIIs are marked up between <<>>. All the hashed values need to be dehashed using the dehashing service.<br>
     * Sample request for dehashing is: <br>
     * {<br>
	 *		"hashes":[<br>
	 *			"267e473c6e5c925d75cc880e5555ed7aa40c7d68",<br>
	 *			"6b3d386d7548e83b3b6531d66f182bc3200b7793",<br>
	 *			"2f61cb7837b83df50a7fffb58e802b87679cdaff"<br>
	 *			]<br>
	 *	}<br>
     *  Then sets the following headers. <br>
	 * 1. <code>hashes</code> = hashed Lis <br>
	 * 2. <code>DHRequest</code> = Map of dehash request <br>
     * @param headers Map It has all the exchange headers 
     * @param properties Map it has all the exchange properties
     * @return 
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */  
    public Map<String, LinkedList<String>> prepareRequest(@Headers Map<String,Object> headers,@ExchangeProperties Map<String,Object> properties) throws JsonParseException, JsonMappingException, IOException{

        //1. identify and get list of marked up hashes in the body
        LinkedList<String> listOfHashes = getListOfHashes(properties);
        Set<String> withoutDuplicates = new LinkedHashSet<String>(listOfHashes); 
        listOfHashes.clear();
        listOfHashes.addAll(withoutDuplicates);
        //2. put linked list in a map
        Map<String, LinkedList<String>> requestBody = new HashMap<>();
        requestBody.put("hashes", listOfHashes);
        headers.put("hashes", listOfHashes);
        /*headers.put("DHRequest", requestBody);*/
        return requestBody;
       
    }
    /**
     * The following method extracts the list of hashed text from the original message <br>
     * It identifies the hashed text based on <<>> mark up. For example: in the sentence, <br>
     * "Dear <<asdfhapwebp>>, recharge with 10 $ and get 1 GB data free", asdfhapwebp <br>
     * is identified as a hash value because it is marked up in <<>>  <br>
     * @param properties Map it has all the exchange properties
     * @return Hashed List returned
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    private LinkedList<String> getListOfHashes(Map<String, Object> properties) throws JsonParseException, JsonMappingException, IOException {

        String originalMsg= (String) properties.get("OriginalMessage");
        LinkedList<String> listOfHashes = new LinkedList<>();
        String[] hashes = originalMsg.split("<<");
        for (String string : hashes) {
            if(!string.contains(">>"))
                continue;
            string = string.substring(0, string.indexOf(">>"));
            listOfHashes.add(string);
        }
        return listOfHashes;
    }

    public void redisKeyGenerator(@Body String body, @Headers Map<String,Object> headers,@ExchangeProperties Map<String,Object> properties) {
    	log.debug("inside redisKeyGenerator method. body: {}",body);
    	try 
        {
            String hashedKey = toHexString(getSHA(body));
            log.debug("Key generated"  + " : " + hashedKey); 
            properties.put("isDuplicateCacheKey", hashedKey);
            headers.put(Constants.SOCache_dot_Key,hashedKey);
            headers.put(Constants.SOCache_dot_Command,"EXISTS");
           
        }
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            log.debug("Exception thrown for incorrect algorithm: " + e); 
        }
    }
    
    private static byte[] getSHA(String input) throws NoSuchAlgorithmException
    { 
        // Static getInstance method is called with hashing SHA 
        MessageDigest md = MessageDigest.getInstance("SHA-256"); 
  
        // digest() method called 
        // to calculate message digest of an input 
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8)); 
    }
    
    private static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation 
        BigInteger number = new BigInteger(1, hash); 
  
        // Convert message digest into hex value 
        StringBuilder hexString = new StringBuilder(number.toString(16)); 
  
        // Pad with leading zeros
        while (hexString.length() < 32) 
        { 
            hexString.insert(0, '0'); 
        } 
  
        return hexString.toString(); 
    }
    
}
