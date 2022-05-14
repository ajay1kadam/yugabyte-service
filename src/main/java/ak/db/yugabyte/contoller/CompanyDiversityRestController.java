package ak.db.yugabyte.contoller;

import ak.db.yugabyte.entity.CompanyDiversityInfo;
import ak.db.yugabyte.poi.DiversityInclusion;
import ak.db.yugabyte.poi.DiversityInclusionFactory;
import ak.db.yugabyte.repo.CompanyDiversityInfoRepository;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;
import ak.db.yugabyte.service.DiversityInclusionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CompanyDiversityRestController {
    private static Logger LOGGER = LoggerFactory.getLogger(CompanyDiversityRestController.class);

    private final CompanyDiversityInfoRepository companyDiversityInfoRepository;

    private final LeaderDiversityInfoRepository leaderDiversityInfoRepository;
    @Autowired
    DiversityInclusionService diversityInclusionService;

    public CompanyDiversityRestController(CompanyDiversityInfoRepository companyDiversityInfoRepository, LeaderDiversityInfoRepository leaderDiversityInfoRepository) {
        this.companyDiversityInfoRepository = companyDiversityInfoRepository;
        this.leaderDiversityInfoRepository = leaderDiversityInfoRepository;
    }

    @PostMapping("/api/CompanyDiversityInfo/add")
    public ResponseEntity<String> addCompanyDiversityInfo(@RequestBody CompanyDiversityInfo companyDiversityInfo) {

        long st_time = System.currentTimeMillis();

        try {
            companyDiversityInfoRepository.save(companyDiversityInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
             LOGGER.info("addEmployee() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/CompanyDiversityInfo/getAll")
    public List<CompanyDiversityInfo> getAllCompanyDiversityInfo() {

        long st_time = System.currentTimeMillis();
        try {
            LOGGER.info("getAllCompanyDiversityInfo() invoked !");

            var deptIterable = companyDiversityInfoRepository.findAll();

            List<CompanyDiversityInfo> employeeList =
                    StreamSupport.stream(deptIterable.spliterator(), false)
                            .collect(Collectors.toList());
            return employeeList;
        } finally {
            LOGGER.info("getAllCompanyDiversityInfo() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
    }

    @GetMapping("/api/LoadFromFile/{from}/{to}")
    public ResponseEntity<String> LoadFromFile(@PathVariable Integer from, @PathVariable Integer to) {

        long st_time = System.currentTimeMillis();
        try {
            LOGGER.info("LoadFromFile() invoked !, Range, " +  from + ", " +to);

            DiversityInclusion objDAndI = DiversityInclusionFactory.createDiversityInclusion("DI");
            objDAndI.readDiversityOwnedData(companyDiversityInfoRepository, leaderDiversityInfoRepository,
                    from, to);

            //diversityInclusionService.saveCompanyDiversityInfo(objDAndI.getCompanies());
      //      diversityInclusionService.saveLeaderDiversityInfo(objDAndI.getLeaders());

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            LOGGER.info("LoadFromFile() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
