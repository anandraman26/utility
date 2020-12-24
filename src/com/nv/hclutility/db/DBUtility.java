package com.nv.hclutility.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.nv.hclutility.pojo.Destination;
import com.nv.hclutility.pojo.TraceResult;
import com.nv.hclutility.util.PropertyFileConstants;

public class DBUtility {

	private static ArrayList<TraceResult> result = new ArrayList<>();

	private static final Logger LOGGER = Logger.getLogger(DBUtility.class);
	private static volatile DBUtility INSTANCE;

	public static DBUtility getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DBUtility();
		return INSTANCE;
	}

	public void setTraceDetails(TraceResult traceResult) {
		LOGGER.info(PropertyFileConstants.TRACE_THREAD + "" + traceResult.getRtoTime() + " : Setting trace in List "
				+ traceResult.getDestIpAddress());
		try {
			traceResult.setDestination(getDestinationName(traceResult));
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.TRACE_THREAD + "Exception while retriving the Destination Name from DB ",
					e);
		}

		result.add(traceResult);
		LOGGER.info(PropertyFileConstants.TRACE_THREAD + "trace list size is : "+ result.size());
		if (result.size() == 3) {
			try {
				LOGGER.info(PropertyFileConstants.TRACE_THREAD + "trace list size is ==3 which sent to save in DB : "
						+ result.size());
				saveTraceDetailsIntoDB(result);
			} catch (SQLException e) {
				LOGGER.error(PropertyFileConstants.TRACE_THREAD + "Exception while saving the trace list size is ==3 ",
						e);
			}
		} else if (result.size() > 3) {
			try {
				LOGGER.info(PropertyFileConstants.TRACE_THREAD + "trace list size is >3 which sent to save in DB :"
						+ result.size());
				saveTraceDetailsIntoDB(result);
			} catch (SQLException e) {
				LOGGER.error(PropertyFileConstants.TRACE_THREAD + "Exception while saving the trace list size is >3 ",
						e);
			}
		} else {
			LOGGER.info(PropertyFileConstants.TRACE_THREAD + "Current trace list size is which will save in DB : "
					+ result.size());
		}
	}

	private static void saveTraceDetailsIntoDB(ArrayList<TraceResult> trace) throws SQLException {
		Connection connection = null;
		PreparedStatement pStatement = null;
		try {
			String query = "insert into TBL_UTILITY (RTO_TIME,SOURCE_IP,DESTINATION_IP,TRACE_LOGS,APPLICATION_NAME) "
					+ " values (?,?,?,?,?)";

			// creating JDBC Connection to mysql database
			connection = DBConnectionUtil.getInstance().getIagentDBConnection();
			if (connection != null) {
				pStatement = connection.prepareStatement(query);
				long startTime = System.currentTimeMillis();
				for (TraceResult traceResult : trace) {
					pStatement.setTimestamp(1, traceResult.getRtoTime());
					pStatement.setString(2, traceResult.getSourceIpAddress());
					pStatement.setString(3, traceResult.getDestIpAddress());
					pStatement.setString(4, traceResult.getTraceLog().toString());
					pStatement.setString(5, traceResult.getDestination().getApplicationName());
					pStatement.addBatch();
				}
				pStatement.executeBatch();
				long endTime = System.currentTimeMillis();
				long elapsedTime = endTime - startTime; // in seconds
				LOGGER.info(PropertyFileConstants.TRACE_THREAD
						+ "Total time required to execute 3 queries using PreparedStatement with JDBC batch insert is :"
						+ elapsedTime + " ms");
				result = new ArrayList<TraceResult>();
			} else {
				LOGGER.error(PropertyFileConstants.TRACE_THREAD + "Exception not able to connect DB connection is "
						+ connection);
				result.addAll(trace);
			}
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.TRACE_THREAD + "Exception while saving the trace in DB", e);
		} finally {
			DBConnectionUtil.getInstance().closeTheConnection(connection, pStatement, null);
		}

	}

	private static Destination getDestinationName(TraceResult traceResult) {
		Destination destinationIP = null;
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "select DESTINATION_IP,APPLICATION_NAME from TBL_UTILITY_API where DESTINATION_IP=?";
			connection = DBConnectionUtil.getInstance().getIagentDBConnection();
			if (connection != null) {
				pStatement = connection.prepareStatement(query);
				pStatement.setString(1, traceResult.getDestIpAddress());
				resultSet = pStatement.executeQuery();
				if (resultSet != null) {
					destinationIP = new Destination();
				}
				while (resultSet.next()) {
					destinationIP = new Destination();
					destinationIP.setDestinationIP(resultSet.getString(1));
					destinationIP.setApplicationName(resultSet.getString(2));
				}
			} else {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception not able to connect DB connection is "
						+ connection);
			}
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.TRACE_THREAD + "Exception while saving the trace in DB", e);
		} finally {
			DBConnectionUtil.getInstance().closeTheConnection(connection, pStatement, resultSet);
		}
		LOGGER.info(PropertyFileConstants.TRACE_THREAD + "destination from DB : " + destinationIP.toString());
		return destinationIP;
	}

	public TraceResult getRTOListByDestinationIp(String destIp) {
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement pStatement = null;
		// List<Agents> agentList = null;
		ConcurrentHashMap<String, TraceResult> rtoList = null;
		ResultSetMetaData rsmd = null;
		List<LinkedHashMap<String, String>> rtoListPdf = null;
		TraceResult traceResult1 = new TraceResult();
		String query = "select * from TBL_UTILITY where EMAIL_STATUS IS NULL and DESTINATION_IP=? order by RTO_TIME desc";
		try {
			connection = DBConnectionUtil.getInstance().getIagentDBConnection();
			if (connection != null) {
				pStatement = connection.prepareStatement(query);
				pStatement.setString(1, destIp);
				result = pStatement.executeQuery();
				if (result != null) {
					rtoList = new ConcurrentHashMap<String, TraceResult>();

					LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
					rsmd = result.getMetaData();
					rtoListPdf = new ArrayList<LinkedHashMap<String, String>>();
					row.put(rsmd.getColumnName(1), "APPLICATION_NAME");
					row.put(rsmd.getColumnName(2), "RTO_TIME");
					row.put(rsmd.getColumnName(3), "SOURCE_IP");
					row.put(rsmd.getColumnName(4), "DESTINATION_IP");
					row.put(rsmd.getColumnName(5), "TRACE_LOGS");
					rtoListPdf.add(row);
				}
				while (result.next()) {
					TraceResult traceResult = new TraceResult();
					traceResult.setId(result.getString("ID") != null ? result.getString("ID") : "");
					traceResult
							.setRtoTime(result.getString("RTO_TIME") != null ? result.getTimestamp("RTO_TIME") : null);
					traceResult.setSourceIpAddress(
							result.getString("SOURCE_IP") != null ? result.getString("SOURCE_IP") : "");
					traceResult.setTraceLog(
							result.getString("TRACE_LOGS") != null ? new StringBuffer(result.getString("TRACE_LOGS"))
									: null);
					traceResult.setDestIpAddress(
							result.getString("DESTINATION_IP") != null ? result.getString("DESTINATION_IP") : "");
					traceResult.setApplicationName(
							result.getString("APPLICATION_NAME") != null ? result.getString("APPLICATION_NAME") : "");

					LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
					row.put(rsmd.getColumnName(1), traceResult.getApplicationName());
					row.put(rsmd.getColumnName(2), traceResult.getRtoTime().toString());
					row.put(rsmd.getColumnName(3), traceResult.getSourceIpAddress());
					row.put(rsmd.getColumnName(4), traceResult.getDestIpAddress());
					row.put(rsmd.getColumnName(5), traceResult.getTraceLog().toString());
					rtoListPdf.add(row);
					rtoList.put(traceResult.getId(), traceResult);
				}
				traceResult1.setRtoListForEmailList(rtoList);
				traceResult1.setRtoListForEmailPdf(rtoListPdf);

				LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "Response: getRTOListByDestinationIp result rtoList: "
						+ rtoList.size());
				LOGGER.info(PropertyFileConstants.EMAIL_THREAD
						+ "Response: getRTOListByDestinationIp result rtoListPdf: " + rtoListPdf.size());
			} else {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception not able to connect DB connection is "
						+ connection);
			}
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception in getRTOListByDestinationIp", e);
		} finally {
			DBConnectionUtil.getInstance().closeTheConnection(connection, pStatement, result);
		}
		return traceResult1;
	}

	public List<LinkedHashMap<String, String>> getRTOListByDestinationIpForPDF(String destIp) {

		ResultSet result = null;
		Connection connection = null;
		PreparedStatement pStatement = null;
		// List<Agents> agentList = null;
		// ConcurrentHashMap<String, TraceResult> agentList = null;
		ResultSetMetaData rsmd = null;
		String query = "select * from TBL_UTILITY where EMAIL_STATUS IS NULL and DESTINATION_IP=? order by RTO_TIME desc";
		List<LinkedHashMap<String, String>> agentList = null;
		try {
			connection = DBConnectionUtil.getInstance().getIagentDBConnection();
			if (connection != null) {
				pStatement = connection.prepareStatement(query);
				pStatement.setString(1, destIp);
				result = pStatement.executeQuery();
				if (result != null) {
					agentList = new ArrayList<LinkedHashMap<String, String>>();

					rsmd = result.getMetaData();
					LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
					row.put(rsmd.getColumnName(1), "RTO_TIME");
					row.put(rsmd.getColumnName(2), "SOURCE_IP");
					row.put(rsmd.getColumnName(3), "DESTINATION_IP");
					row.put(rsmd.getColumnName(4), "APPLICATION_NAME");
					row.put(rsmd.getColumnName(5), "TRACE_LOGS");
					agentList.add(row);
				}
				while (result.next()) {
					LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
					row.put(rsmd.getColumnName(1),
							result.getString("RTO_TIME") != null ? result.getString("RTO_TIME") : "");
					row.put(rsmd.getColumnName(2),
							result.getString("SOURCE_IP") != null ? result.getString("SOURCE_IP") : "");
					row.put(rsmd.getColumnName(3),
							result.getString("DESTINATION_IP") != null ? result.getString("DESTINATION_IP") : "");
					row.put(rsmd.getColumnName(4),
							result.getString("APPLICATION_NAME") != null ? result.getString("APPLICATION_NAME") : "");
					agentList.add(row);
				}

				LOGGER.info("Response: getRTOListByDestinationIpForPDF For PDF result: " + agentList.size());
			} else {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception not able to connect DB connection is "
						+ connection);
			}

		} catch (Exception e) {
			LOGGER.error("Exception in getRTOListByDestinationIpForPDF For PDF ", e);
		} finally {
			DBConnectionUtil.getInstance().closeTheConnection(connection, pStatement, result);
		}
		return agentList;
	}

	public ArrayList<Destination> getAPIList() {

		ResultSet result = null;
		Connection connection = null;
		PreparedStatement pStatement = null;
		// List<Agents> agentList = null;
		ArrayList<Destination> destinationsList = null;
		String query = "select * from TBL_UTILITY_API";
		try {
			connection = DBConnectionUtil.getInstance().getIagentDBConnection();
			if (connection != null) {
				pStatement = connection.prepareStatement(query);
				// pStatement.setString(1, destIp);
				result = pStatement.executeQuery();
				if (result != null) {
					destinationsList = new ArrayList<Destination>();
				}
				while (result.next()) {
					Destination destination = new Destination();
					destination.setDestinationIP(
							result.getString("DESTINATION_IP") != null ? result.getString("DESTINATION_IP") : "");
					destination.setApplicationName(
							result.getString("APPLICATION_NAME") != null ? result.getString("APPLICATION_NAME") : "");
					destinationsList.add(destination);
				}
			} else {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception not able to connect DB connection is "
						+ connection);
			}

			LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "Response: destinationsList result: "
					+ destinationsList.size());

		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception in getAPIList", e);
		} finally {
			DBConnectionUtil.getInstance().closeTheConnection(connection, pStatement, result);
		}
		return destinationsList;
	}

	public void updateEmailStatus(List<TraceResult> traceResults, String toEmailId,String emailStatus) {

		PreparedStatement pStatement = null;
		Connection connection = null;
		try {
			connection = DBConnectionUtil.getInstance().getIagentDBConnection();
			if (connection != null) {
				String query = "update TBL_UTILITY set EMAIL_STATUS=?,EMAIL_ID=?,EMAIL_TIME=? where ID=?";
				pStatement = connection.prepareStatement(query);
				for (TraceResult traceResult : traceResults) {
					pStatement.setString(1, emailStatus);
					pStatement.setString(2, toEmailId);
					pStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
					pStatement.setString(4, traceResult.getId());
					pStatement.addBatch();
				}
				pStatement.executeBatch();

				LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "updateEmailStatus update operation result ");
			} else {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "Exception not able to connect DB connection is "
						+ connection);
			}

		} catch (SQLException e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "EROOR ON update updateEmailStatus to database ", e);
		} catch (Throwable e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "EROOR ON update updateEmailStatus to database ", e);
		} finally {
			DBConnectionUtil.getInstance().closeTheConnection(connection, pStatement, null);
		}
	}

}
