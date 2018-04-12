package klk59.hr.model.reports;

import java.util.Collection;
import java.util.Map;

import domainapp.basics.core.dodm.dsm.DSMBasic;
import domainapp.basics.core.dodm.qrm.QRM;
import domainapp.basics.exceptions.DataSourceException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.Oid;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.query.Expression.Op;
import domainapp.basics.model.query.Query;
import domainapp.basics.model.query.QueryToolKit;
import domainapp.modules.report.model.meta.Output;
import klk59.hr.model.Employee;

/**
 * Represent the reports about employees by name
 * 
 * @author ThuHuong
 *
 */
@DClass(schema="hr",serialisable=false)
public class EmployeesByNameReport {
	@DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
	private int id;
	private static int idCounter = 0;

	/**input: employee name */
	@DAttr(name = "name", type = Type.String, length = 30, optional = false)
	private String name;
	  
	  /**output: employees whose names match {@link #name} */
	  @DAttr(name="employees",type=Type.Collection,optional=false, mutable=false,
	      serialisable=false,filter=@Select(clazz=Employee.class, 
	      attributes={Employee.A_id, Employee.A_name, Employee.A_dob, Employee.A_email, 
	    		  Employee.A_phonenumber, Employee.A_identification, Employee.A_socialInsuranceNo, Employee.A_rptEmployeesByName}))
	  @DAssoc(ascName="employees-by-name-report-has-employees",role="report",
	      ascType=AssocType.One2Many,endType=AssocEndType.One,
	    associate=@Associate(type=Employee.class,cardMin=0,cardMax=MetaConstants.CARD_MORE))
	  
	  @Output
	  private Collection<Employee> employees;

	  /**output: number of employees found (if any), derived from {@link #employees} */
	  @DAttr(name = "numEmployees", type = Type.Integer, length = 20, auto=true, mutable=false)
	  @Output
	  private int numEmployees;
	  
	  /**
	   * @effects 
	   *  initialise this with <tt>name</tt> and use {@link QRM} to retrieve from data source 
	   *  all {@link Employee} whose names match <tt>name</tt>.
	   *  initialise {@link #employees} with the result if any.
	   *  
	   *  <p>throws NotPossibleException if failed to generate data source query; 
	   *  DataSourceException if fails to read from the data source
	   * 
	   */
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  @DOpt(type=DOpt.Type.RequiredConstructor)
	  public EmployeesByNameReport(@AttrRef("name") String name) throws NotPossibleException, DataSourceException {
	    this.id=++idCounter;
	    
	    this.name = name;
	    
	    doReportQuery();
	  }
	  
	  /**
	   * @effects return name
	   */
	  public String getName() {
	    return name;
	  }

	  /**
	   * @effects <pre>
	   *  set this.name = name
	   *  if name is changed
	   *    invoke {@link #doReportQuery()} to update the output attribute value
	   *    throws NotPossibleException if failed to generate data source query; 
	   *    DataSourceException if fails to read from the data source.
	   *  </pre>
	   */
	  public void setName(String name) throws NotPossibleException, DataSourceException {
	    boolean doReportQuery = (name != null && !name.equals(this.name));
	    
	    this.name = name;
	    
	    if (doReportQuery) {
	      doReportQuery();
	    }
	  }

	  /**
	   * This method is invoked when the report input has be set by the user. 
	   * 
	   * @effects <pre>
	   *   formulate the object query
	   *   execute the query to retrieve from the data source the domain objects that satisfy it 
	   *   update the output attributes accordingly.
	   *  
	   *  <p>throws NotPossibleException if failed to generate data source query; 
	   *  DataSourceException if fails to read from the data source. </pre>
	   */
	  private void doReportQuery() throws NotPossibleException, DataSourceException {
	    // the query manager instance
	    
	    QRM qrm = QRM.getInstance();
	    
	    // create a query to look up Employee from the data source
	    // and then populate the output attribute (employees) with the result
	    DSMBasic dsm = qrm.getDsm();
	    
	    //TODO: to conserve memory cache the query and only change the query parameter value(s)
	    Query q = QueryToolKit.createSearchQuery(dsm, Employee.class, 
	        new String[] {Employee.A_name}, 
	        new Op[] {Op.MATCH}, 
	        new Object[] {"%"+name+"%"});
	    
	    Map<Oid, Employee> result = qrm.getDom().retrieveObjects(Employee.class, q);
	    
	    if (result != null) {
	      // update the main output data 
	      employees = result.values();
	      
	      // update other output (if any)
	      numEmployees = employees.size();
	    } else {
	      // no data found: reset output
	      resetOutput();
	    }
	  }

	  /**
	   * @effects 
	   *  reset all output attributes to their initial values
	   */
	  private void resetOutput() {
	    employees = null;
	    numEmployees = 0;
	  }

	  /**
	   * A link-adder method for {@link #employees}, required for the object form to function.
	   * However, this method is empty because employees have already be recorded in the attribute {@link #employees}.
	   */
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addEmployee(Collection<Employee> employees) {
	    // do nothing
	    return false;
	  }
	  
	  /**
	   * @effects return employees
	   */
	  public Collection<Employee> getEmployees() {
	    return employees;
	  }
	  
	  /**
	   * @effects return numEmployees
	   */
	  public int getNumEmployees() {
	    return numEmployees;
	  }

	  /**
	   * @effects return id
	   */
	  public int getId() {
	    return id;
	  }

	  /* (non-Javadoc)
	   * @see java.lang.Object#hashCode()
	   */
	  /**
	   * @effects 
	   * 
	   * @version 
	   */
	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + id;
	    return result;
	  }

	  /* (non-Javadoc)
	   * @see java.lang.Object#equals(java.lang.Object)
	   */
	  /**
	   * @effects 
	   * 
	   * @version 
	   */
	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    EmployeesByNameReport other = (EmployeesByNameReport) obj;
	    if (id != other.id)
	      return false;
	    return true;
	  }

	  /* (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	  /**
	   * @effects 
	   * 
	   * @version 
	   */
	  @Override
	  public String toString() {
	    return "EmployeesByNameReport (" + id + ", " + name + ")";
	  }

}
