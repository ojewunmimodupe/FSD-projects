package in.co.air.line.ticket.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import in.co.air.line.ticket.bean.BaseBean;
import in.co.air.line.ticket.bean.BookBean;
import in.co.air.line.ticket.bean.FlightBean;
import in.co.air.line.ticket.exception.ApplicationException;
import in.co.air.line.ticket.exception.DuplicateRecordException;
import in.co.air.line.ticket.model.BookModel;
import in.co.air.line.ticket.model.FlightModel;
import in.co.air.line.ticket.util.DataUtility;
import in.co.air.line.ticket.util.DataValidator;
import in.co.air.line.ticket.util.PropertyReader;
import in.co.air.line.ticket.util.ServletUtility;



/**
 * Servlet implementation class BookCtl
 */
@WebServlet(name = "BookCtl", urlPatterns = { "/BookCtl" })
public class BookCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
       
	private static Logger log = Logger.getLogger(BookCtl.class);

	/**
	 * Validate input Data Entered By User
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("BookCtl validate method start");
		boolean pass = true;
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		if (DataValidator.isNull(request.getParameter("bookDate"))) {
			request.setAttribute("bookDate", PropertyReader.getValue("error.require", "Date"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("emailId"))) {
			request.setAttribute("emailId", PropertyReader.getValue("error.require", "Email ID"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("firstName"))) {
		
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "Mobile No"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("address"))) {
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("noP"))) {
			request.setAttribute("noP", PropertyReader.getValue("error.require", "No Of Person"));
			pass = false;
		}

	
		
		if(OP_PAYMENT_BOOK.equalsIgnoreCase(op)) {
			pass=true;
		}
		
		log.debug("BookCtl validate method end");
		return pass;
	}
	
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("BookCtl populateBean method start");
		BookBean bean = new BookBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setBookDate(DataUtility.getDate(request.getParameter("bookDate")));
		bean.setNoOfPerson(DataUtility.getLong(request.getParameter("noP")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobile")));
		bean.setEmailId(DataUtility.getString(request.getParameter("emailId")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setFlightId(DataUtility.getLong(request.getParameter("flightId")));
		populateDTO(bean, request);
		log.debug("BookCtl populateBean method end");
		return bean;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("BookCtl doGet method start");
		String op = DataUtility.getString(request.getParameter("operation"));

		HttpSession session=request.getSession();
		
		long Mid=DataUtility.getLong(request.getParameter("bId"));
		session.setAttribute("boId",Mid);
		
		BookModel model = new BookModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		ServletUtility.setOpration("Add", request);
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			BookBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setOpration("Edit", request);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("BookCtl doGet method end");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("BookCtl doPost method start");
		String op = DataUtility.getString(request.getParameter("operation"));
		BookModel model = new BookModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		HttpSession session=request.getSession();
		if (OP_PAYMENT.equalsIgnoreCase(op)) {

			BookBean bean = (BookBean) populateBean(request);	
			bean.setFlightId(bean.getFlightId());
			FlightModel fModel=new FlightModel();
			FlightBean fBean;
			try {
				fBean = fModel.findByPK(bean.getFlightId());
				bean.setFinalPrice(fBean.getTicketPrice()*bean.getNoOfPerson());
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			session.setAttribute("BookB", bean);
			
			ServletUtility.forward(ATBView.PAYMENT_VIEW, request, response);
		
		
		}else if(OP_PAYMENT_BOOK.equalsIgnoreCase(op)) {
			long pk=0;
			BookBean bBean=(BookBean)session.getAttribute("BookB");
			try {
				 pk=model.add(bBean);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DuplicateRecordException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("msg","Ticket is Successfully Booked");
			ServletUtility.forward(ATBView.PAYMENT_VIEW, request, response);
		}else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ATBView.INDEX_CTL, request, response);
			return;
	} 

		ServletUtility.forward(getView(), request, response);
		log.debug("BookCtl doPost method end");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ATBView.BOOK_VIEW;
	}

}
