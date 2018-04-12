package klk59.hr.model;

import java.util.ArrayList;
import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import domainapp.basics.util.cache.StateHistory;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DOpt;

@DClass(schema="hr")
public class JobQualification {
	private static final String AttributeName_SalaryBasic = "salaryBasic";
	private static final String AttributeName_JobLevelName = "jobLevelName";
	
	//attributes
	@DAttr(name="id",id=true,auto=true,length=6,mutable=false,type=Type.Integer)
	private int id;
	private static int idCounter;
	  
	@DAttr(name=AttributeName_JobLevelName,length=20,type=Type.String,optional=true)
	private String jobLevelName;
	
	@DAttr(name = AttributeName_SalaryBasic, length=20, type=Type.Integer, auto=true,mutable = false,optional = true,
			serialisable=false,
			derivedFrom={AttributeName_JobLevelName})
	private int salaryBasic;
	
	private StateHistory<String, Object> stateHist;
	
	@DAttr(name="employees",type=Type.Collection,
	    serialisable=false,optional=false,
	    filter=@Select(clazz=Employee.class))
	@DAssoc(ascName="employee-has-position",role="jobQualification",
	    ascType=AssocType.One2Many,endType=AssocEndType.One,
	    associate=@Associate(type=Employee.class,
	    cardMin=1,cardMax=100))  
	private List<Employee> employees;
	  
	// derived attributes
	private int employeesCount;
	  
	
	//constructor method
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public JobQualification(@AttrRef("jobLevelName") String jobLevelName) {
	  this(null, jobLevelName);
	}

	// constructor to create objects from data source
	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public JobQualification(Integer id, String jobLevelName) {
	  this.id = nextID(id);
	  this.jobLevelName = jobLevelName;
	    
	  employees = new ArrayList<>();
	  employeesCount = 0;
	  
//	  stateHist = new StateHistory<>();
	  
	  updateSalaryBasic();
	}

	//setter methods
	public void setJobLevelName(String jobLevelName) {
	  this.jobLevelName = jobLevelName;
	  updateSalaryBasic();
	}
	
	@DOpt(type=DOpt.Type.DerivedAttributeUpdater)
	@AttrRef(value=AttributeName_SalaryBasic)
	public void updateSalaryBasic() {
//		stateHist.put(AttributeName_JobLevelName, jobLevelName);
		String se1 = "SE1";
		String se2 = "SE2";
		String se3 = "SE3";
		String se4 = "SE4";
		String se5 = "SE5";
		if(jobLevelName.equals(se1))
			salaryBasic = 20000000;
		else if(jobLevelName.equals(se2))
			salaryBasic = 25000000;
		else if(jobLevelName.equals(se3))
			salaryBasic = 30000000;
		else if(jobLevelName.equals(se4))
			salaryBasic = 35000000;
		else if(jobLevelName.equals(se5))
			salaryBasic = 40000000;
		else
			salaryBasic = 0;
	}

	@DOpt(type=DOpt.Type.LinkAdder)
	//only need to do this for reflexive association: @MemberRef(name="employees")  
	public boolean addEmployee(Employee s) {
	  if (!this.employees.contains(s)) {
	    employees.add(s);
	  }
	    
	  // no other attributes changed
	  return false; 
	}

	@DOpt(type=DOpt.Type.LinkAdderNew)
	public boolean addNewEmployee(Employee s) {
	  employees.add(s);
	  employeesCount++;
	    
	  // no other attributes changed
	  return false; 
	}
	  
	@DOpt(type=DOpt.Type.LinkAdder)
	public boolean addEmployee(List<Employee> employees) {
	  for (Employee s : employees) {
	    if (!this.employees.contains(s)) {
	      this.employees.add(s);
	    }
	  }
	    
	  // no other attributes changed
	  return false; 
	}

	@DOpt(type=DOpt.Type.LinkAdderNew)
	public boolean addNewEmployee(List<Employee> employees) {
	  this.employees.addAll(employees);
	  employeesCount += employees.size();

	  // no other attributes changed
	  return false; 
	}

	@DOpt(type=DOpt.Type.LinkRemover)
	//only need to do this for reflexive association: @MemberRef(name="employees")
	public boolean removeEmployee(Employee s) {
	  boolean removed = employees.remove(s);
	    
	  if (removed) {
	    employeesCount--;
	  }
	    
	  // no other attributes changed
	  return false; 
	}
	  
	public void setEmployees(List<Employee> employees) {
	  this.employees = employees;
	    
	  employeesCount = employees.size();
	}
	    
	/**
	 * @effects 
	 *  return <tt>employeesCount</tt>
	 */
	@DOpt(type=DOpt.Type.LinkCountGetter)
	public Integer getEmployeesCount() {
	  return employeesCount;
	}

	@DOpt(type=DOpt.Type.LinkCountSetter)
	public void setEmployeesCount(int count) {
	  employeesCount = count;
	}
	
	//getter methods
	public String getJobLevelName() {
	  return jobLevelName;
	}
	  
	public List<Employee> getEmployees() {
	  return employees;
	}
	  

	public int getId() {
	  return id;
	}
	
	
	public int getSalaryBasic() {
		return salaryBasic;
	}
	

	@Override
	public String toString() {
	  return "JobQualification("+getId()+","+getJobLevelName()+")";
	}
	  
	@Override
	public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + id;
	  return result;
	}

	@Override
	public boolean equals(Object obj) {
	  if (this == obj)
	    return true;
	  if (obj == null)
	    return false;
	  if (getClass() != obj.getClass())
	    return false;
	  JobQualification other = (JobQualification) obj;
	  if (id != other.id)
	    return false;
	  return true;
	}

	private static int nextID(Integer currID) {
	  if (currID == null) {
	    idCounter++;
	    return idCounter;
	  } else {
	    int num = currID.intValue();
	    if (num > idCounter)
	      idCounter = num;
	      
	    return currID;
	  }
	}
	
	/**
	   * @requires 
	   *  minVal != null /\ maxVal != null
	   * @effects 
	   *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
	   */
	  @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
	  public static void updateAutoGeneratedValue(
	      DAttr attrib,
	      Tuple derivingValue, 
	      Object minVal, 
	      Object maxVal) throws ConstraintViolationException {
	    
	    if (minVal != null && maxVal != null) {
	      //TODO: update this for the correct attribute if there are more than one auto attributes of this class 
	      int maxIdVal = (Integer) maxVal;
	      if (maxIdVal > idCounter)  
	        idCounter = maxIdVal;
	    }
	  }
}
