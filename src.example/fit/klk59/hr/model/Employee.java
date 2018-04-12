package klk59.hr.model;

import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import klk59.hr.model.reports.EmployeesByNameReport;


/**
 * Represent a employee.
 * @author ThuHuong
 *
 */
@DClass(schema="hr")
public class Employee {
	public static final String A_id = "id";
	public static final String A_name = "name";
	public static final String A_dob = "dob";
	public static final String A_email = "email";
	public static final String A_phonenumber = "phonenumber";
	public static final String A_identification = "identification";
	public static final String A_socialInsuranceNo = "socialInsuranceNo";
	public static final String A_rptEmployeesByName = "rptEmployeesByName";
	
	//attributes of a employee
	@DAttr(name=A_id, id=true, type=Type.String, auto=true, length=6, mutable=false, optional=false )
	private String id;
	
	//static variable to keep track of employee id
	private static int idCounter = 0;
	
	@DAttr(name=A_name, type=Type.String, length = 30, optional = false)
	private String name;
	
	@DAttr(name = A_dob, type = Type.String, length = 15, optional = false)
	private String dob;
	
	@DAttr(name = A_email, type = Type.String, length = 30, optional = false)
	private String email;
	
	@DAttr(name = A_phonenumber, type = Type.String, length = 30, optional = false)
	private String phonenumber;
	
	@DAttr(name = A_identification, type = Type.String, length = 30, optional = false)
	private String identification;
	
	@DAttr(name = A_socialInsuranceNo, type = Type.String, length = 30, optional = false)
	private String socialInsuranceNo;
	
	@DAttr(name="city", type=Type.Domain, length=6)
	@DAssoc(ascName="employee-has-city", role="employee", 
			ascType=AssocType.One2Many, endType=AssocEndType.Many,
			associate=@Associate(type=City.class, cardMin=1, cardMax=1))
	private City city;
	
	@DAttr(name="department", type=Type.Domain, length=6)
	@DAssoc(ascName="department-has-employee", role="employee", 
			ascType=AssocType.One2Many, endType=AssocEndType.Many,
			associate=@Associate(type=Department.class, cardMin=1, cardMax=1))
	private Department department;
	
	@DAttr(name="position", type=Type.Domain, length=6)
	@DAssoc(ascName="employee-has-position", role="employee", 
			ascType=AssocType.One2Many, endType=AssocEndType.Many,
			associate=@Associate(type=JobQualification.class, cardMin=1, cardMax=1))
	private JobQualification position;
	
	@DAttr(name="dailyTimeKeeping",type=Type.Collection,optional = false,
			serialisable=false,filter=@Select(clazz=DailyTimeKeeping.class))
	@DAssoc(ascName="employee-has-dailyTimeKeeping",role="employee",
			ascType=AssocType.One2Many,endType=AssocEndType.One,
		    associate=@Associate(type=DailyTimeKeeping.class,cardMin=0,cardMax=400))
	private List<DailyTimeKeeping> dailyTimeKeeping;
	
	private int dailyTimeKeepingCount;
	
	@DAttr(name=A_rptEmployeesByName,type=Type.Domain, serialisable=false, 
		      // IMPORTANT: set virtual=true to exclude this attribute from the object state
		      // (avoiding the view having to load this attribute's value from data source)
		      virtual=true)
	private EmployeesByNameReport rptEmployeesByName;
	
	//constructor methods
	//for creating in the application
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public Employee(@AttrRef("name") String name, 
			@AttrRef("dob") String dob,
			@AttrRef("email") String email,
			@AttrRef("phonenumber") String phonenumber,
			@AttrRef("identification") String identification,
			@AttrRef("socialInsuranceNo") String socialInsuranceNo) {
	    this(null, name, dob, email, phonenumber, identification, socialInsuranceNo, null, null, null);
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public Employee(@AttrRef("name") String name, 
			@AttrRef("dob") String dob,
			@AttrRef("email") String email,
			@AttrRef("phonenumber") String phonenumber,
			@AttrRef("identification") String identification,
			@AttrRef("socialInsuranceNo") String socialInsuranceNo,
			@AttrRef("city") City city, 
			@AttrRef("department") Department department,
			@AttrRef("position") JobQualification position) {
	    this(null, name, dob, email, phonenumber, identification, socialInsuranceNo, city, department, position);
	}
	
	
	// a shared constructor that is invoked by other constructors
	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public Employee(String id, String name, String dob, String email, String phonenumber, String identification, String socialInsuranceNo, City city, Department department, JobQualification position) 
	throws ConstraintViolationException {
	    // generate an id
	    this.id = nextID(id);

	    // assign other values
	    this.name = name;
	    this.dob = dob;
	    this.email = email;
	    this.phonenumber = phonenumber;   
	    this.identification = identification; 
	    this.socialInsuranceNo = socialInsuranceNo; 
	    this.city = city; 
	    this.department = department; 
	    this.position = position; 
	}
	
	//setter methods
	public void setName(String name) {
	    this.name = name;
	}
	
	public void setDob(String dob) {
	    this.dob = dob;
	}
	
	public void setEmail(String email) {
	    this.email = email;
	}
	
	public void setPhonenumber(String phonenumber) {
	    this.phonenumber = phonenumber;
	}
	
	public void setIdentification(String identification) {
	    this.identification = identification;
	}
	
	public void setSocialInsuranceNo(String socialInsuranceNo) {
	    this.socialInsuranceNo = socialInsuranceNo;
	}
	
	public void setCity(City city) {
	    this.city = city;
	}
	
	public void setDepartment(Department department) {
	    this.department = department;
	}
	
	public void setPosition(JobQualification position) {
	    this.position = position;
	}
	
	@DOpt(type=DOpt.Type.LinkAdder)
	  //only need to do this for reflexive association: @MemberRef(name="dailyTimeKeeping")
	public boolean addDailyTimeKeeping(DailyTimeKeeping d) {
	    if (!dailyTimeKeeping.contains(d))
	      dailyTimeKeeping.add(d);
	    return false;
	}
	
	@DOpt(type=DOpt.Type.LinkAdderNew)
	public boolean addNewDailyTimeKeeping(DailyTimeKeeping d) {
	    dailyTimeKeeping.add(d);
	    
	    dailyTimeKeepingCount++;
 	    
	    // no other attributes changed (average mark is not serialisable!!!)
	    return false; 
	}
	
	@DOpt(type=DOpt.Type.LinkAdder)
	  //@MemberRef(name="dailyTimeKeeping")
	public boolean addDailyTimeKeeping(List<DailyTimeKeeping> dailyTK) {
	    boolean added = false;
	    for (DailyTimeKeeping d : dailyTK) {
	      if (!dailyTimeKeeping.contains(d)) {
	        if (!added) added = true;
	        dailyTimeKeeping.add(d);
	      }
	    }
	    return false;
	}
	
	@DOpt(type=DOpt.Type.LinkAdderNew)
	public boolean addNewDailyTimeKeeping(List<DailyTimeKeeping> dailyTK) {
	    dailyTimeKeeping.addAll(dailyTK);
	    dailyTimeKeepingCount+=dailyTK.size();

	    // no other attributes changed (average mark is not serialisable!!!)
	    return false; 
	}
	
	@DOpt(type=DOpt.Type.LinkRemover)
	  //@MemberRef(name="dailyTimeKeeping")
	public boolean removeDailyTimeKeeping(DailyTimeKeeping d) {
	    boolean removed = dailyTimeKeeping.remove(d);
	    
	    if (removed) {
	      dailyTimeKeepingCount--;
	    }
	    // no other attributes changed
	    return false; 
	}
	
	
	public void setDailyTimeKeeping(List<DailyTimeKeeping> dltk) {
	    this.dailyTimeKeeping = dltk;
	    dailyTimeKeepingCount = dltk.size();
	}
	
	// getter methods
	  public String getId() {
	    return id;
	  }

	  public String getName() {
	    return name;
	  }

	  public String getDob() {
	    return dob;
	  }

	  public String getEmail() {
	    return email;
	  }

	  public String getPhonenumber() {
	    return phonenumber;
	  }

	  public String getIdentification() {
	    return identification;
	  }
	  
	  public String getSocialInsuranceNo() {
		    return socialInsuranceNo;
	  }
	  
	  public City getCity() {
		    return city;
	  }
	  
	  public Department getDepartment() {
		    return department;
	  }
	  
	  public JobQualification getPosition() {
		    return position;
	  }
	  
	  public List<DailyTimeKeeping> getDailyTimeKeeping() {
		    return dailyTimeKeeping;
	  }
	  
	  @DOpt(type=DOpt.Type.LinkCountGetter)
	  public Integer getDailyTimeKeepingCount() {
	    return dailyTimeKeepingCount;
	  }

	  @DOpt(type=DOpt.Type.LinkCountSetter)
	  public void setDailyTimeKeepingCount(int count) {
	    dailyTimeKeepingCount = count;
	  }
	  
	  
	  /**
	   * @effects return rptEmployeeByName
	   */
	  public EmployeesByNameReport getRptEmployeeByName() {
	    return rptEmployeesByName;
	  }
	  
	  
	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
	  }

	  private String nextID(String id) throws ConstraintViolationException {
		    if (id == null) { // generate a new id
		      idCounter++;
		      return "TSDV" + idCounter;
		    } else {
		      // update id
		      int num;
		      try {
		        num = Integer.parseInt(id.substring(1));
		      } catch (RuntimeException e) {
		        throw new ConstraintViolationException(
		            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
		      }
		      
		      if (num > idCounter) {
		        idCounter = num;
		      }
		      
		      return id;
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

	      String maxId = (String) maxVal;
	      
	      try {
	        int maxIdNum = Integer.parseInt(maxId.substring(1));
	        
	        if (maxIdNum > idCounter) // extra check
	          idCounter = maxIdNum;
	        
	      } catch (RuntimeException e) {
	        throw new ConstraintViolationException(
	            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] {maxId});
	      }
	    }
	  }
}
