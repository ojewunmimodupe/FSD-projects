package in.co.air.line.ticket.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.air.line.ticket.bean.FlightBean;
import in.co.air.line.ticket.bean.UserBean;
import in.co.air.line.ticket.exception.ApplicationException;
import in.co.air.line.ticket.exception.DatabaseException;
import in.co.air.line.ticket.exception.DuplicateRecordException;
import in.co.air.line.ticket.util.JDBCDataSource;

public class FlightModel {

	private static Logger log = Logger.getLogger(FlightModel.class);

	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM A_Flight");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	public long add(FlightBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		FlightBean existbean = findByName(bean.getFightName());

		if (existbean != null) {
			throw new DuplicateRecordException("Flight is already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			// Get auto-generated next primary key
			System.out.println(pk + " in ModelJDBC");
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO A_Flight VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFlightNo());
			pstmt.setString(3, bean.getFightName());
			pstmt.setString(4, bean.getFromCity());
			pstmt.setString(5, bean.getToCity());
			pstmt.setDate(6, new java.sql.Date(bean.getDate().getTime()));
			pstmt.setString(7, bean.getDescription());
			pstmt.setString(8, bean.getTime());
			pstmt.setString(9, bean.getTravelDuraion());
			pstmt.setLong(10, bean.getTicketPrice());
			pstmt.setString(11, bean.getAirPortName());
			pstmt.setString(12, bean.getCreatedBy());
			pstmt.setString(13, bean.getModifiedBy());
			pstmt.setTimestamp(14, bean.getCreatedDatetime());
			pstmt.setTimestamp(15, bean.getModifiedDatetime());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	public FlightBean findByName(String name) throws ApplicationException {
		log.debug("Model findByLogin Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM A_Flight WHERE fightName=?");
		FlightBean bean = null;
		Connection conn = null;
		System.out.println("sql" + sql);

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FlightBean();
				bean.setId(rs.getLong(1));
				bean.setFlightNo(rs.getString(2));
				bean.setFightName(rs.getString(3));
				bean.setFromCity(rs.getString(4));
				bean.setToCity(rs.getString(5));
				bean.setDate(rs.getDate(6));
				bean.setDescription(rs.getString(7));
				bean.setTime(rs.getString(8));
				bean.setTravelDuraion(rs.getString(9));
				bean.setTicketPrice(rs.getLong(10));
				bean.setAirPortName(rs.getString(11));
				bean.setCreatedBy(rs.getString(12));
				bean.setModifiedBy(rs.getString(13));
				bean.setCreatedDatetime(rs.getTimestamp(14));
				bean.setModifiedDatetime(rs.getTimestamp(15));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by login");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByLogin End");
		return bean;
	}

	/**
	 * Find User by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public FlightBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM A_flight WHERE ID=?");
		FlightBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FlightBean();
				bean.setId(rs.getLong(1));
				bean.setFlightNo(rs.getString(2));
				bean.setFightName(rs.getString(3));
				bean.setFromCity(rs.getString(4));
				bean.setToCity(rs.getString(5));
				bean.setDate(rs.getDate(6));
				bean.setDescription(rs.getString(7));
				bean.setTime(rs.getString(8));
				bean.setTravelDuraion(rs.getString(9));
				bean.setTicketPrice(rs.getLong(10));
				bean.setAirPortName(rs.getString(11));
				bean.setCreatedBy(rs.getString(12));
				bean.setModifiedBy(rs.getString(13));
				bean.setCreatedDatetime(rs.getTimestamp(14));
				bean.setModifiedDatetime(rs.getTimestamp(15));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}

	public void delete(FlightBean bean) throws ApplicationException {

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM A_flight WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void update(FlightBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		FlightBean beanExist = findByName(bean.getFightName());
		// Check if updated LoginId already exist
		if (beanExist != null && !(beanExist.getId() == bean.getId())) {
			throw new DuplicateRecordException("Flight is already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE A_Flight SET FlightNo=?,FightName=?,FromCity=?,ToCity=?,Date=?,Description=?,Time=?,TravelDuraion=?,TicketPrice=?,AirPortName=?,"
							+ "CREATEDBY=?,MODIFIEDBY=?,CREATEDDATETIME=?,MODIFIEDDATETIME=? WHERE ID=?");
			pstmt.setString(1, bean.getFlightNo());
			pstmt.setString(2, bean.getFightName());
			pstmt.setString(3, bean.getFromCity());
			pstmt.setString(4, bean.getToCity());
			pstmt.setDate(5, new java.sql.Date(bean.getDate().getTime()));
			pstmt.setString(6, bean.getDescription());
			pstmt.setString(7, bean.getTime());
			pstmt.setString(8, bean.getTravelDuraion());
			pstmt.setLong(9, bean.getTicketPrice());
			pstmt.setString(10, bean.getAirPortName());
			pstmt.setString(11, bean.getCreatedBy());
			pstmt.setString(12, bean.getModifiedBy());
			pstmt.setTimestamp(13, bean.getCreatedDatetime());
			pstmt.setTimestamp(14, bean.getModifiedDatetime());
			pstmt.setLong(15, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating User ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	public List search(FlightBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search User with pagination
	 * 
	 * @return list : List of Users
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * @throws DatabaseException
	 */

	public List search(FlightBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM A_Flight WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getFightName() != null && bean.getFightName().length() > 0) {
				sql.append(" AND FightName like '" + bean.getFightName() + "%'");
			}
			if (bean.getFlightNo() != null && bean.getFlightNo().length() > 0) {
				sql.append(" AND FlightNo like '" + bean.getFlightNo() + "%'");
			}
			if (bean.getToCity() != null && bean.getToCity().length() > 0) {
				sql.append(" AND toCity like '" + bean.getToCity() + "%'");
			}
			if (bean.getFromCity() != null && bean.getFromCity().length() > 0) {
				sql.append(" AND FromCity like '" + bean.getFromCity() + "%'");
			}
			if (bean.getDate() != null && bean.getDate().getDate() > 0) {
				sql.append(" AND Date = "+ new java.sql.Date(bean.getDate().getTime()));
			}

		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("user model search  :" + sql);
		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FlightBean();
				bean.setId(rs.getLong(1));
				bean.setFlightNo(rs.getString(2));
				bean.setFightName(rs.getString(3));
				bean.setFromCity(rs.getString(4));
				bean.setToCity(rs.getString(5));
				bean.setDate(rs.getDate(6));
				bean.setDescription(rs.getString(7));
				bean.setTime(rs.getString(8));
				bean.setTravelDuraion(rs.getString(9));
				bean.setTicketPrice(rs.getLong(10));
				bean.setAirPortName(rs.getString(11));
				bean.setCreatedBy(rs.getString(12));
				bean.setModifiedBy(rs.getString(13));
				bean.setCreatedDatetime(rs.getTimestamp(14));
				bean.setModifiedDatetime(rs.getTimestamp(15));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of User with pagination
	 * 
	 * @return list : List of users
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from A_Flight");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("sql in list user :" + sql);
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				FlightBean bean = new FlightBean();
				bean.setId(rs.getLong(1));
				bean.setFlightNo(rs.getString(2));
				bean.setFightName(rs.getString(3));
				bean.setFromCity(rs.getString(4));
				bean.setToCity(rs.getString(5));
				bean.setDate(rs.getDate(6));
				bean.setDescription(rs.getString(7));
				bean.setTime(rs.getString(8));
				bean.setTravelDuraion(rs.getString(9));
				bean.setTicketPrice(rs.getLong(10));
				bean.setAirPortName(rs.getString(11));
				bean.setCreatedBy(rs.getString(12));
				bean.setModifiedBy(rs.getString(13));
				bean.setCreatedDatetime(rs.getTimestamp(14));
				bean.setModifiedDatetime(rs.getTimestamp(15));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}

}
