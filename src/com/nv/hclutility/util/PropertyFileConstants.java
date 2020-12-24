package com.nv.hclutility.util;

public final class PropertyFileConstants {

	/**
	 * #### DATABASE RELATED CONFIGURATION SETTINGS #####
	 * 
	 * db_url=jdbc:sqlite:C:/NovelVox/iagent-server/webapps/ROOT/db/gadgets/
	 * customer-profile-gadget.db3
	 * 
	 * db_userName=
	 * 
	 * db_password=
	 * 
	 * db_driver=org.sqlite.JDBC
	 * 
	 * db_alias_name=callHistoryGadgetDB
	 * 
	 */

	public static final String DB_URL = "db_url";
	public static final String DB_USERNAME = "db_userName";
	public static final String DB_PASSWORD = "db_password";
	public static final String DB_DRIVER = "db_driver";
	public static final String DB_ALIAS_NAME = "db_alias_name";

	/**
	 * #### RESTICTIONS CONFIGURATION SETTINGS #####
	 * 
	 * apply_restrictions = false
	 * 
	 * max_results_reside_in_DB=10
	 * 
	 * total_no_of_calls_result_by_agent_id=5
	 * 
	 * total_no_of_calls_result_by_call_id=5
	 * 
	 * total_no_of_calls_result_by_phone_number=5
	 * 
	 **/

	public static final String APPLY_RESTRICTIONS = "apply_restrictions";
	public static final String MAX_RESULTS_BY_CUSTOMER_AND_PHONE = "max_results_by_customer_and_phone";
	public static final String MAX_RESULTS_RESIDE_IN_DB = "max_results_reside_in_DB";
	public static final String TOTAL_NO_OF_CALLS_RESULT_BY_AGENT_ID = "total_no_of_calls_result_by_agent_id";
	public static final String TOTAL_NO_OF_CALLS_RESULT_BY_CUSTOMER_ID = "total_no_of_calls_result_by_customer_id";
	public static final String TOTAL_NO_OF_CALLS_RESULT_BY_PHONE_NUMBER = "total_no_of_calls_result_by_phone_number";

	/**
	 * #### COLUMN MAPPINGS SETTINGS #####
	 * 
	 * call_id_column_mapping=callId
	 * 
	 * phone_no_column_mapping=ani
	 * 
	 * customer_id_column_mapping=customerId
	 * 
	 **/
	public static final String CALL_ID_COLUMN_MAPPING = "call_id_column_mapping";
	public static final String PHONE_NO_COLUMN_MAPPING = "phone_no_column_mapping";
	public static final String CUSTOMER_ID_COLUMN_MAPPING = "customer_id_column_mapping";

	/**
	 * #### MANUAL CONFIGURATION SETTINGS #####
	 * 
	 * if manual_labelling_on is "true" then the
	 * 
	 * "manual_labelling_prefix" +
	 * "total_no_of_calls_result_by_.."+"manual_labelling_suffix"
	 * 
	 * will be sent to be set
	 * 
	 * otherwise "custom_label_for_last_x_calls_by_.." will be sent
	 * 
	 * #########################################
	 * 
	 * manual_labelling_on=true
	 * 
	 * manual_labelling_prefix=Last
	 * 
	 * manual_labelling_suffix=calls by
	 */
	public static final String MANUAL_LABELLING_ON = "manual_labelling_on";
	public static final String MANUAL_LABELLING_PREFIX = "manual_labelling_prefix";
	public static final String MANUAL_LABELLING_SUFFIX = "manual_labelling_suffix";

	/**
	 * #### CUSTOM CONFIGURATION SETTINGS #####
	 * 
	 * custom_label_for_last_x_calls_by_agent_id = Last calls by agent
	 * 
	 * custom_label_for_last_x_calls_by_customer_id = Last calls by customers
	 * 
	 * custom_label_for_last_x_calls_by_phone_number = Last calls by phone
	 * 
	 * custom_label_for_last_x_calls=Last X calls
	 * 
	 **/
	public static final String CUSTOM_LABEL_FOR_LAST_X_CALLS_BY_AGENT_ID = "custom_label_for_last_x_calls_by_agent_id";
	public static final String CUSTOM_LABEL_FOR_LAST_X_CALLS_BY_CUSTOMER_ID = "custom_label_for_last_x_calls_by_customer_id";
	public static final String CUSTOM_LABEL_FOR_LAST_X_CALLS_BY_PHONE_NUMBER = "custom_label_for_last_x_calls_by_phone_number";
	public static final String CUSTOM_LABEL_FOR_LAST_X_CALLS = "custom_label_for_last_x_calls";
	
	
	public static final String SAVE_CALL_LOG = PropertyUtil.getInstance().getValueForKey("SAVE_CALL_LOG");
	public static final String UPDATE_CALL_REMARK = PropertyUtil.getInstance().getValueForKey("UPDATE_CALL_REMARK");
	public static final String UPDATE_CALL_LOG_ID = PropertyUtil.getInstance().getValueForKey("UPDATE_CALL_LOG_ID");
	public static final String UPDATE_CALL_LOG =  PropertyUtil.getInstance().getValueForKey("UPDATE_CALL_LOG");
	public static final String UPDATE_CALL_LOG_ON_CUSTOMER_SEARCH = PropertyUtil.getInstance().getValueForKey("UPDATE_CALL_LOG_ON_CUSTOMER_SEARCH");
	public static final String GET_LAST_X_CALL_TRANSFERED_TO_AGENT =  PropertyUtil.getInstance().getValueForKey("GET_LAST_X_CALL_TRANSFERED_TO_AGENT");
	public static final String GET_TOTAL_CALL_BY_PHONE_NUMBER = PropertyUtil.getInstance().getValueForKey("GET_TOTAL_CALL_BY_PHONE_NUMBER");
	public static final String GET_LAST_X_CALL_BY_CUSTOMER_ID = PropertyUtil.getInstance().getValueForKey("GET_LAST_X_CALL_BY_CUSTOMER_ID");
	public static final String GET_LAST_X_CALL_BY_PHONE_NO = PropertyUtil.getInstance().getValueForKey("GET_LAST_X_CALL_BY_PHONE_NO");
	public static final String GET_MONTHLY_AGENT_USAGE = PropertyUtil.getInstance().getValueForKey("GET_MONTHLY_AGENT_USAGE");
	public static final String DELETE_RECORD_BY_RESTRICTION = PropertyUtil.getInstance().getValueForKey("DELETE_RECORD_BY_RESTRICTION");
	public static final Object DELETE_RECORD_BY_MAX_RESULT_FROM_DB = PropertyUtil.getInstance().getValueForKey("DELETE_RECORD_BY_MAX_RESULT_FROM_DB");
	public static final String FETCH_MOST_RECENT_RECORDS = PropertyUtil.getInstance().getValueForKey("FETCH_MOST_RECENT_RECORDS");
	public static final String GET_ALL_RECORDS_BY_CUSTOMER_NO_PHONE_NO = PropertyUtil.getInstance().getValueForKey("GET_ALL_RECORDS_BY_CUSTOMER_NO_PHONE_NO");
	public static final String GET_DAILY_CALL_COUNT =  PropertyUtil.getInstance().getValueForKey("GET_DAILY_CALL_COUNT");
	public static final String GET_WEAKLY_CALL_COUNT = PropertyUtil.getInstance().getValueForKey("GET_WEAKLY_CALL_COUNT");
	public static final String GET_MONTHLY_CALL_COUNT = PropertyUtil.getInstance().getValueForKey("GET_MONTHLY_CALL_COUNT");
	public static final String REPORTING_LOGIN_AGENT = PropertyUtil.getInstance().getValueForKey("REPORTING_LOGIN_AGENT");
	public static final String REPORTING_GET_TOTAL_CALLS = PropertyUtil.getInstance().getValueForKey("REPORTING_GET_TOTAL_CALLS");
	public static final String REPORTING_GET_TODAY_CALLS = PropertyUtil.getInstance().getValueForKey("REPORTING_GET_TODAY_CALLS");
	public static final String REPORTING_CALL_HISTORY_REPORT_ON_FILTER = PropertyUtil.getInstance().getValueForKey("REPORTING_CALL_HISTORY_REPORT_ON_FILTER");
	public static final String REPORTING_CALL_HISTORY_REPORT = PropertyUtil.getInstance().getValueForKey("REPORTING_CALL_HISTORY_REPORT");
	public static final String AGENT_LOGIN_STATS = PropertyUtil.getInstance().getValueForKey("AGENT_lOGIN_STATS");
	public static final String AGENT_LOGOUT_STATS = PropertyUtil.getInstance().getValueForKey("AGENT_lOGOUT_STATS");
	public static final String SAVE_MANUAL_SEARCH_VALUE = PropertyUtil.getInstance().getValueForKey("SAVE_MANUAL_SEARCH_VALUE");
	public static final String GET_MANUAL_CUSTOMER_ID = PropertyUtil.getInstance().getValueForKey("GET_MANUAL_CUSTOMER_ID");
	
	
	/**********************************************************************/
	public static final String TOKEN_USERNAME=PropertyUtil.getInstance().getValueForKey("token.username");
	public static final String TOKEN_PASSWORD=PropertyUtil.getInstance().getValueForKey("token.password");
	public static final String TOKEN_CLIENT_ID=PropertyUtil.getInstance().getValueForKey("token.client_id");
	public static final String TOKEN_CLIENT_SECRET=PropertyUtil.getInstance().getValueForKey("token.client_secret");
	public static final String TOKEN_API=PropertyUtil.getInstance().getValueForKey("token.api");
	
	public static final String AUTH_TOKEN="auth_token";
	public static final String PARAMETER="#PARAMETER#";
	public static final String PHONE_PARAMETER="#PHONE_PARAMETER#";
	public static final String MOBILE_PARAMETER="#MOBILE_PARAMETER#";
	
	/****************SEARCH CUSTOMER FIELD ********************************/
	public static final String SEARCH_CUSTOMER=PropertyUtil.getInstance().getValueForKey("search.customer.api");
	public static final String REGEX_EMAIL="^(.+)@(.+)$";
	public static final String CUSTOMER_ON_EMAIL =PropertyUtil.getInstance().getValueForKey("search.customer.emailId");
	public static final String REGEX_PHONE="^[\\d ()+-\\.]+$";
	public static final String CUSTOMER_ON_PHONE =PropertyUtil.getInstance().getValueForKey("search.customer.mobile.phone.number");
	public static final String CUSTOMER_ON_USERNAME ="user_name%3D#PARAMETER#";
	public static final String SEARCH_ON_FILTERS="#SEARCH_ON_FILTERS#";
	public static final String PLUS_FOR_PHONE="+";
	public static final String CUSTOMER_ON_EMPLOYEENUMBER ="employee_number%3D#PARAMETER#";
	public static final String REGEX_EMPNUMBER="^(0|[1-9][0-9]*)$";
	
	/****************INCIDENT CONSTANTS ********************************/
	
	public static final String SEARCH_INCIDENTS_BY_ID=PropertyUtil.getInstance().getValueForKey("search.incident.id");
	public static final String SEARCH_INCIDENTS=PropertyUtil.getInstance().getValueForKey("search.incident");
	
	
	/****************SR CONSTANTS ********************************/
	public static final String SR_LIST="SR_LIST";
	public static final String SEARCH_SR="SEARCH_SR";
	
	
	public static final String FILTER="#FILTER#";
	
	
	public static final String UTF_8="UTF-8";
	
	public static final String OBJECT_VALUE="value";
	public static final String NESTED_JSON_KEY="display_value";
	public static final String NESTED_JSON_NAME="Name";
	public static final String NESTED_OPEN_NAME="Open_By";
	public static final String JSON_RESULT="records";
	

	public static final String PROJECT_NAME="Pernod-Ricard";
	public static final Object JSON_ACCESS_TOKEN = "access_token";
	
	
	public static final Object EMAIL_THREAD = "EMAIL THREAD:\t";
	public static final Object PING_THREAD = "PING THREAD:\t";
	public static final Object TRACE_THREAD = "TRACE THREAD:\t ";
	
}


