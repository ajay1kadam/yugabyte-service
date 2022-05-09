package ak.db.yugabyte.contoller;

import ak.db.yugabyte.entity.Employee;
import ak.db.yugabyte.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController()
public class EmployeeController {
    private static Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeRepository employees;

    public EmployeeController(EmployeeRepository employees) {
        this.employees = employees;
    }

    @GetMapping("/api/employees/getAll")
    public List<Employee> getAllEmployees() {

        long st_time = System.currentTimeMillis();
        try {
            LOGGER.info("getAllEmployees() invoked !");

            var deptIterable = employees.findAll();

            List<Employee> employeeList =
                    StreamSupport.stream(deptIterable.spliterator(), false)
                            .collect(Collectors.toList());
            return employeeList;
        } finally {
            LOGGER.info("getAllEmployees() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
    }


    @PostMapping("/api/employees/add")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        try {
            employees.save(employee);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
