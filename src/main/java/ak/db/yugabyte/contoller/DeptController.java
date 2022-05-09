package ak.db.yugabyte.contoller;

import ak.db.yugabyte.repo.Dept;
import ak.db.yugabyte.repo.DeptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController()
public class DeptController {
    private static Logger LOGGER = LoggerFactory.getLogger(DeptController.class);

    private final DeptRepository depts;

    public DeptController(DeptRepository deptRepository) {
        this.depts = deptRepository;
    }

    @GetMapping("/api/dept/getAll")
    public List<Dept> getAllDepts() {

        long st_time = System.currentTimeMillis();
        try {
            LOGGER.info("getAllDepts() invoked !");

            var deptIterable = depts.findAll();

            List<Dept> deptList =
                    StreamSupport.stream(deptIterable.spliterator(), false)
                            .collect(Collectors.toList());
            return deptList;
        }
        finally {
            System.out.println("Processing time (ms) : " + (System.currentTimeMillis()-st_time) );
        }
    }

}
