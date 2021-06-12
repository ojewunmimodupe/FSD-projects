package in.co.air.line.ticket.bean;

import java.util.Date;

public class FlightBean extends BaseBean {
	
	
	private String flightNo;
	private String fightName;
	private String fromCity;
	private String toCity;
	private Date date;
	private String description;
	private String time;
	private String travelDuraion;
	private long ticketPrice;
	private String airPortName;
	
	

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getFightName() {
		return fightName;
	}

	public void setFightName(String fightName) {
		this.fightName = fightName;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTravelDuraion() {
		return travelDuraion;
	}

	public void setTravelDuraion(String travelDuraion) {
		this.travelDuraion = travelDuraion;
	}

	public long getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(long ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getAirPortName() {
		return airPortName;
	}

	public void setAirPortName(String airPortName) {
		this.airPortName = airPortName;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
