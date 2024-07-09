package model;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class Appointment {
	private int appointmentId;
    private int customerId;
    private int serviceId;
    private Date dated;
    private Time timet;
    private LocalDate date;
    private LocalTime time;
	private LocalDateTime appointmentDateTime;
	public Appointment(int appointmentId,int customerId, int serviceId, LocalDate date, LocalTime time) {
	   this.appointmentId=appointmentId;
	    this.customerId = customerId;
	    this.serviceId = serviceId;
	    this.date = date;
	    this.time = time;
	}
    public Appointment(int customerId,int serviceId, LocalDateTime appointmentDateTime) {
    	this.appointmentDateTime=appointmentDateTime;
        this.customerId = customerId;
        this.serviceId = serviceId;
    }
	public Appointment(int appointmentId, int customerId, int serviceId, Date dated, Time timet) {
		this.appointmentId=appointmentId;
		this.customerId=customerId;
		this.serviceId=serviceId;
		this.dated=dated;
		this.timet=timet;
	}
	public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public int getServiceId() {
        return serviceId;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public boolean isFinished() {
        if (date == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        return date.isBefore(currentDate);
    }
    public boolean isIncoming() {
        if (date == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        return date.isAfter(currentDate);
    }
}