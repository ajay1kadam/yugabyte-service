package ak.db.yugabyte.controller;

import ak.db.yugabyte.entity.Employee;
import ak.db.yugabyte.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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

        long st_time = System.currentTimeMillis();

        try {
            employees.save(employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info("addEmployee() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/getEmployee/{id}")
    public ResponseEntity<Employee> findEmployeeByID(@PathVariable Integer id) {
        long st_time = System.currentTimeMillis();

        try {
            var empObj = employees.findById(id);
            return new ResponseEntity<>(empObj.get(), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info("getEmployee/{id}() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
    }
    @PostMapping("/api/getEmployeeByName/{name}")
    public ResponseEntity<Employee> findEmployeeByName(@PathVariable String name) {
        long st_time = System.currentTimeMillis();

        try {
            var empObj = employees.findByName(name);
            return new ResponseEntity<>(empObj, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info("getEmployeeByName/{name}() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
    }

}
