package ak.db.yugabyte.service;

import ak.db.yugabyte.entity.CompanyDiversityInfo;
import ak.db.yugabyte.entity.LeaderDiversityInfo;
import ak.db.yugabyte.repo.CompanyDiversityInfoRepository;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DiversityInclusionServiceImpl implements DiversityInclusionService{

    @Autowired
    private CompanyDiversityInfoRepository companyRepository;

    @Autowired
    private LeaderDiversityInfoRepository leaderRepository;



    @Override
    public void saveCompanyDiversityInfo(List<CompanyDiversityInfo> lsCompanyDI) {

        companyRepository.saveAll(lsCompanyDI);

     /*   for ( CompanyDiversityInfo cmpInfo : lsCompanyDI) {
            companyRepository.save(cmpInfo);
        }*/
    }

    @Override
    public List<CompanyDiversityInfo> findAllCompanies() {
        //return companyRepository.findAll();
        var deptIterable = companyRepository.findAll();

        List<CompanyDiversityInfo> companyDiversityInfoList =
                StreamSupport.stream(deptIterable.spliterator(), false)
                        .collect(Collectors.toList());

        return companyDiversityInfoList;

    }

    @Override
    public void saveLeaderDiversityInfo(List<LeaderDiversityInfo> lsLeaderDI) {
        leaderRepository.saveAll(lsLeaderDI);
    }
}
