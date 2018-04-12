package klk59.hr.software;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;
import klk59.hr.model.City;
import klk59.hr.model.DailyTimeKeeping;
import klk59.hr.model.Department;
import klk59.hr.model.Employee;
import klk59.hr.model.JobQualification;
import klk59.hr.model.TypeOfWork;
import klk59.hr.model.reports.EmployeesByNameReport;

public class HRSoftware extends DomainAppToolSoftware{
	// the domain model of software
	  private static final Class[] model = {
	      Employee.class, 
	      City.class, 
	      Department.class,
	      JobQualification.class,
	      TypeOfWork.class,
	      DailyTimeKeeping.class,
	      EmployeesByNameReport.class
	  };
	  
	  /**
	   * @effects 
	   *  return {@link #model}.
	   */
	  @Override
	  protected Class[] getModel() {
	    return model;
	  }

	  /**
	   * The main method
	   * @effects 
	   *  run software with a command specified in args[0] and with the model 
	   *  specified by {@link #getModel()}. 
	   *  
	   *  <br>Throws NotPossibleException if failed for some reasons.
	   */
	  public static void main(String[] args) throws NotPossibleException {
	    new HRSoftware().exec(args);
	  }
}
