package com.knowesis.sift.orchestrator;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.PropertyInject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowesis.sift.orchestrator.domain.Constants;
import com.knowesis.sift.orchestrator.exception.MessageResolutionException;



public class SFMCMessagingProcessor {
  
	@PropertyInject("{{so.email.successCodes}}")
	private String successCode;
	
   ObjectMapper mapper;
   List<String> successCodeList;
   
   @PostConstruct
	public void onPostConstruct() {
		
		mapper = new ObjectMapper();
		String[] errorCodes = StringUtils.split(successCode, ",");
		successCodeList = Arrays.asList(errorCodes);
	}
   private final Logger log = LoggerFactory.getLogger(getClass());
   /**
    * Check http response codes in the success list
    * Sets status, statusCode, action and response fields in the message <br>
    * Following attributes are set in the body for database logging:<br>
    * <ol>
    * <li>status</li>
    * <li>statusCode</li>
    * <li>action</li>
    * <li>response</li>
    * </ol>
    * @param body Map representation of JSON body
    * @param headers MAP representation of headers
    * @param properties MAP representation of properties
 * @return MAP OriginalMessage
 * @throws IOException 
 * @throws JsonMappingException 
 * @throws JsonParseException 
    */
   public Map<String, Object> processMessagingAPIResponse (@Body Map<String,Object> body,@Headers Map<String,Object> headers,@ExchangeProperties  Map<String,Object> properties) throws JsonParseException, JsonMappingException, IOException  {
	   	  
		  Map<String, Object> originalMsg = mapper.readValue((String) properties.get(Constants.OriginalMessage), new TypeReference<Map<String, Object>>(){});
          int httpResponseCode=(int) headers.get("CamelHttpResponseCode");
          if(successCodeList.contains(String.valueOf(httpResponseCode))) {
        	  originalMsg.put(Constants.status,"SUCCESS" );
        	  originalMsg.put(Constants.response, body.get("tokenId"));
          }else {
        	  originalMsg.put(Constants.status,"FAILURE" );
          }
          originalMsg.put(Constants.statusCode, httpResponseCode);
          
          String action = (String) originalMsg.getOrDefault(Constants.action, "OFFERNOTIFY");
  		if ( action.equals("CORENOTIFICATION") ) {
  			originalMsg.put(Constants.action, "OFFERNOTIFY");
  		}else if ( action.equals("COREREMINDER") ) {
  			originalMsg.put(Constants.action, "REMINDERNOTIFY");
  		}
          
          originalMsg.replace(Constants.actionStatus,originalMsg.get(Constants.status));
          originalMsg.replace(Constants.actionResponse,originalMsg.get(Constants.statusCode)+":"+ originalMsg.get(Constants.status));
          return originalMsg;
   }
   



   public Map<String, Object> updateTrigger (@Body Map<String,Object> body,@Headers Map<String,Object> headers) throws JsonParseException, JsonMappingException, IOException, MessageResolutionException {
	   	
		
	   String msisdn=(String) body.get(Constants.msisdn);
	   Map<String, Object> dedata = (Map<String, Object>) body.get("deData");
	   String Customer_Email = (String) dedata.get(Constants.Customer_Email);
	   headers.put(Constants.Customer_Email, Customer_Email);
	   return body;
   }
   
   
  
   public Map<String, Object> checkContactWindow (@Body Map<String,Object> body,@Headers Map<String,Object> headers) {
		
	   if(!body.get("contactWindows").equals("")) {
		Map<String, ArrayList<String> > contactWindows = (Map<String,ArrayList<String> >) body.get("contactWindows");
		Format dayFormat = new SimpleDateFormat("EEEE");
		String contactWindowDay = dayFormat.format(new Date());
		String startTime = contactWindows.get(contactWindowDay).get(0);
		String endTime = contactWindows.get(contactWindowDay).get(1);
		LocalTime currentTime = LocalTime.now();
		 		Boolean isContacble = (currentTime.isAfter( LocalTime.parse( startTime ) ) && currentTime.isBefore( LocalTime.parse(endTime ))) ; 
		 		headers.put("isContactable",isContacble);
		 		if (!isContacble) {
		 			body.replace(Constants.actionStatus,"FAILURE" );
		}
		else { 
			headers.put("isContactable",true);
			}
		return body;
		
		
	}else {
		headers.put("isContactable",true);
	}
		return body;
   }}