{<#assign deData=body.deData>"definitionKey":"${deData.SFMC_Email_DefinitionKey}","recipient":{"to":"${deData.Customer_Email}","contactKey":"${deData.Subscriberkey1}","attributes":{<#if deData.Title?has_content>"Title":"${deData.Title}",</#if><#if deData.First_Name?has_content>"First_Name":"${deData.First_Name}",</#if><#if deData.Last_Name?has_content>"Last_Name":"${deData.Last_Name}",</#if><#if deData.Mobile_Number?has_content>"Mobile_Number":"${deData.Mobile_Number}",</#if> <#if deData.Mobile_Locale?has_content>"Mobile_Locale":"${deData.Mobile_Locale}",</#if><#if deData.Hash_Key?has_content>"Hash_Key":"${deData.Hash_Key}",</#if> "Offer_Id":"${body.offerId}", <#if deData.Product_Name?has_content>"Product_Name":"${deData.Product_Name}",</#if> <#if deData.Premium_Selected?has_content>"Premium_Selected":"${deData.Premium_Selected}",</#if> <#if deData.Campaign_End_Date?has_content>"Campaign_End_Date":"${deData.Campaign_End_Date}",</#if> <#if deData.Coverage?has_content>"Coverage":"${deData.Coverage}",</#if> "Utm_Source":"${deData.Utm_Source}","Utm_Medium":"${deData.Utm_Medium}","Utm_Campaign":"${deData.Utm_Campaign}",<#if deData.Utm_Content?has_content>"Utm_Content":"${deData.Utm_Content}",</#if> <#if deData.Utm_Term?has_content>"Utm_Term":"${deData.Utm_Term}",</#if> <#if deData.Utm_Src?has_content>"Utm_Src":"${deData.Utm_Src}",</#if> <#if deData.Uuid?has_content>"Uuid":"${deData.Uuid}",</#if><#if deData.Short_Url?has_content>"Short_Url":"${deData.Short_Url}",</#if>"Flow_Id":"${body.flowId}","Subscriberkey1":"${deData.Subscriberkey1}"}}}${request.setHeader('messageKey', '${body.flowId}')}${request.setHeader('CamelHttpMethod', 'POST')}${request.setHeader('Content-Type', 'application/json')}
