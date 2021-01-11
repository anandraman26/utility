package com.nv.hclutility.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.nv.hclutility.util.PropertyFileConstants;
import com.nv.hclutility.util.PropertyUtil;

public class DBConnectionUtil {

	private static Logger LOGGER = Logger.getLogger(DBConnectionUtil.class);
	private static DBConnectionUtil thisInstance;

	private static ComboPooledDataSource cpdsObjDB = null;

	public static final String CONECTION = "connection";

	private DBConnectionUtil() {
		makeComboPooledDataSourceObject();
	}

	private void makeComboPooledDataSourceObject() {
		try {
			cpdsObjDB = new ComboPooledDataSource();
			cpdsObjDB.setDriverClass(PropertyUtil.getInstance().getValueForKey(PropertyFileConstants.DB_DRIVER));
			cpdsObjDB.setJdbcUrl(PropertyUtil.getInstance().getValueForKey(PropertyFileConstants.DB_URL));
			cpdsObjDB.setUser(PropertyUtil.getInstance().getValueForKey(PropertyFileConstants.DB_USERNAME));
			cpdsObjDB.setPassword(PropertyUtil.getInstance().getValueForKey(PropertyFileConstants.DB_PASSWORD));
			cpdsObjDB.setMaxPoolSize(200);
			cpdsObjDB.setMinPoolSize(10);
			cpdsObjDB.setMaxIdleTime(300000); // 5 Min
			cpdsObjDB.setCheckoutTimeout(30000);// 30 sec
			cpdsObjDB.setMaxStatements(100);
			cpdsObjDB.setMaxConnectionAge(0);// not closing connection by force
			cpdsObjDB.setAcquireRetryAttempts(2);
			cpdsObjDB.setAcquireRetryDelay(1000);
			cpdsObjDB.setPreferredTestQuery("SELECT 1");
			LOGGER.info(cpdsObjDB);
		} catch (Exception e) {
			LOGGER.error("ERROR WHILE CONNECTION TO DATA SOURCE :", e);
		}
	}

	public static DBConnectionUtil getInstance() {
		if (thisInstance == null) {
			synchronized (DBConnectionUtil.class) {
				if (thisInstance == null) {
					thisInstance = new DBConnectionUtil();
				}
			}
		}
		return thisInstance;
	}

	public Connection getConnection(String type) {
		Connection con = null;
		con = getIagentDBConnection();
		return con;
	}

	public Connection getIagentDBConnection() {
		Connection connection = null;
		try {
			connection = cpdsObjDB.getConnection();
			LOGGER.info("Connection is " + connection);
			System.out.println(connection);
		} catch (Throwable e) {
			LOGGER.error("ERROR WHILE CONNECTION TO DATA SOURCE : " + (cpdsObjDB != null
					? ("db_url = " + cpdsObjDB.getJdbcUrl() + " db_userName = " + cpdsObjDB.getUser())
					: ""), e);
		}
		return connection;
	}

	public void closeTheConnection(Connection con, PreparedStatement pst, ResultSet resultSet) {
		if (pst != null) {
			try {
				pst.close();
			} catch (Exception e) {
				LOGGER.error("error in closeTheConnection while closing resources connections  ");
			}
		}

		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				LOGGER.error("error in closeTheConnection while closing resources connections  ");
			}
		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e) {
				LOGGER.error("error in closeTheConnection while closing resources connections  ");
			}

		}
	}

}