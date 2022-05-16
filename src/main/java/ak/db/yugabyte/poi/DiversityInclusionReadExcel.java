package ak.db.yugabyte.poi;

import ak.db.yugabyte.entity.CompanyDiversityInfo;
import ak.db.yugabyte.entity.LeaderDiversityInfo;
import ak.db.yugabyte.repo.CompanyDiversityInfoRepository;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class DiversityInclusionReadExcel implements DiversityInclusion {

    private static Logger LOGGER = LoggerFactory.getLogger(DiversityInclusionReadExcel.class);

    public void readDiversityOwnedData(CompanyDiversityInfoRepository companyDiversityInfoRepository,
                                       LeaderDiversityInfoRepository leaderDiversityInfoRepository,
                                       Integer from, Integer to) throws IOException {

        try {
         //   FileInputStream file = new FileInputStream(getFile("Hackathon_Data.xlsx"));
            //new FileInputStream(new File("C:\\devl\\wfb\\hackathon\\docs\\Hackathon_Data_MinorityWomenOwned_2022 v1.xlsx"));

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:Hackathon_Data.xlsx");

            InputStream inputStream = null;
            for(Resource r: resources) {
                 inputStream = r.getInputStream();
                LOGGER.info("file opened : " + r.getFilename()  );
            }


            List<CompanyDiversityInfo> companyDiversityInfoList = new ArrayList<>();
            List<LeaderDiversityInfo> leaderDiversityInfoList = new ArrayList<>();

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            LOGGER.info("XSSFWorkbook opened ! " );

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int rowCounter = -1;
            //String strValue = null;
            //int iColumnIndex = 0;

            final int batchSize = 100;
            int batchCounter = 0;

            while (rowIterator.hasNext()) {

                CompanyDiversityInfo objCDI = new CompanyDiversityInfo();
                LeaderDiversityInfo objLDI1 = new LeaderDiversityInfo();
                LeaderDiversityInfo objLDI2 = new LeaderDiversityInfo();
                Set<LeaderDiversityInfo> setOfLeaders = new HashSet<>();

                Row row = rowIterator.next();
                rowCounter++;
                //For each row, iterate through all the columns
                //if (row.getRowNum() == 0) { // skip header row..
                if (rowCounter == 0) { // skip header row..
                    continue;
                }

                if (rowCounter < from) continue; // skip initial rows

                //LOGGER.info("Start processing from record number, " + rowCounter );
                if (rowCounter >= to) {

                    if (batchCounter > 0) {
                        saveToDB(companyDiversityInfoList, leaderDiversityInfoList,
                                companyDiversityInfoRepository, leaderDiversityInfoRepository);
                    }
                    break;
                }

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    int iColumnIndex = cell.getColumnIndex();

                    String strValue;
                    if (cell.getCellType() == CellType.NUMERIC) {
                        strValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                    } else {
                        strValue = cell.getStringCellValue();
                    }


                    if (iColumnIndex == 0) {
                        objCDI.setDunsNumber(strValue);
                    } else if (iColumnIndex == 1) {
                        objCDI.setDunsName(strValue);
                    } else if (iColumnIndex == 2) {
                        objCDI.setCounty(strValue);
                    } else if (iColumnIndex == 3) {
                        objCDI.setStreetAddress(strValue);
                    } else if (iColumnIndex == 4) {
                        objCDI.setCity(strValue);
                    } else if (iColumnIndex == 5) {
                        objCDI.setState(strValue);
                    } else if (iColumnIndex == 6) {
                        objCDI.setZipCode(strValue);
                    } else if (iColumnIndex == 7) {
                        objCDI.setPhone(strValue);
                    } else if (iColumnIndex == 8 && StringUtils.hasLength(strValue)) { //executiveContact1
                        objLDI1.setName(strValue);
                        setOfLeaders.add(objLDI1);
                        /*if (objLDI1.getName() != null && objLDI1.getName().length() > 0) {
                            setOfLeaders.add(objLDI1);
                        }*/
                    } else if (iColumnIndex == 9 && StringUtils.hasLength(strValue)) { //executiveContact1 - gender
                        objLDI1.setGender(strValue);
                    } else if (iColumnIndex == 10 && StringUtils.hasLength(strValue)) { //executiveContact1 - ethnicity
                        objLDI1.setEthnicity(strValue);
                    }
                    else if (iColumnIndex == 11 && StringUtils.hasLength(strValue)) { //executiveContact1 - lgbt
                        objLDI1.setIsLgbt(strValue);
                    }
                    else if (iColumnIndex == 12 && StringUtils.hasLength(strValue)) { //executiveContact1 - veteran
                        objLDI1.setIsVeteran(strValue);
                    }else if (iColumnIndex == 13 && StringUtils.hasLength(strValue)) { //executiveContact1 - disabled
                        objLDI1.setIsDisable(strValue);
                    }else if (iColumnIndex == 14 && StringUtils.hasLength(strValue)) { //executiveContact1 - share_percent
                        objLDI1.setSharePercentage(parseSharePercentage(strValue));
                    }

                    /***********/
                    //else if (iColumnIndex == 9) { //executiveContact2
                    else if (iColumnIndex == 15 && StringUtils.hasLength(strValue)) { //executiveContact2
                        objLDI2.setName(strValue);
                        setOfLeaders.add(objLDI2);
                        /*if (objLDI2.getName() != null && objLDI2.getName().length() > 0) {
                            setOfLeaders.add(objLDI2);
                        }*/
                    }  else if (iColumnIndex == 16 && StringUtils.hasLength(strValue)) { //executiveContact2 - gender
                        objLDI2.setGender(strValue);
                    } else if (iColumnIndex == 17 && StringUtils.hasLength(strValue)) { //executiveContact2 - ethnicity
                        objLDI2.setEthnicity(strValue);
                    }
                    else if (iColumnIndex == 18 && StringUtils.hasLength(strValue)) { //executiveContact2 - lgbt
                        objLDI2.setIsLgbt(strValue);
                    }
                    else if (iColumnIndex == 19 && StringUtils.hasLength(strValue)) { //executiveContact2 - veteran
                        objLDI2.setIsVeteran(strValue);
                    }else if (iColumnIndex == 20 && StringUtils.hasLength(strValue)) { //executiveContact2 - disabled
                        objLDI2.setIsDisable(strValue);
                    }else if (iColumnIndex == 21 && StringUtils.hasLength(strValue)) { //executiveContact2 - share_percent
                        objLDI2.setSharePercentage(parseSharePercentage(strValue));
                    }
                    //else if (iColumnIndex == 10) {
                    else if (iColumnIndex == 22) {

                    //} else if (iColumnIndex == 11) {
                    } else if (iColumnIndex == 23) {
                        //objLDI1.setCompany(objCDI);
                        //objLDI2.setCompany(objCDI);
                        objCDI.setLeaders(setOfLeaders);
                    }
                }

                companyDiversityInfoList.add(objCDI);
                if (objLDI1.getName() != null && objLDI1.getName().length() > 0) {
                    leaderDiversityInfoList.add(objLDI1);
                }
                if (objLDI2.getName() != null && objLDI2.getName().length() > 0) {
                    leaderDiversityInfoList.add(objLDI2);
                }

                batchCounter++;
                if (batchCounter == batchSize) {

                    saveToDB(companyDiversityInfoList, leaderDiversityInfoList,
                            companyDiversityInfoRepository, leaderDiversityInfoRepository);

                    batchCounter = 0;
                }
            } // while row iter

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private long parseSharePercentage(String strValue) {
        try {
            return Integer.parseInt(strValue);
        }
        catch (Exception ex) {
            LOGGER.error("Error parsing share percentage : " + strValue);
            return 0;
        }
    }

    private void saveToDB(List<CompanyDiversityInfo> companyDiversityInfoList,
                          List<LeaderDiversityInfo> leaderDiversityInfoList,
                          CompanyDiversityInfoRepository companyDiversityInfoRepository,
                          LeaderDiversityInfoRepository leaderDiversityInfoRepository) {

        LOGGER.info("#################### :saveToDB() start ");
        long st_time = System.currentTimeMillis();
        try {

            companyDiversityInfoRepository.saveAll(companyDiversityInfoList);
            leaderDiversityInfoRepository.saveAll(leaderDiversityInfoList);
        } finally {

            LOGGER.info("#################### :saveToDB(), time (ms) :"
                    + (System.currentTimeMillis() - st_time));
            companyDiversityInfoList.clear();
            leaderDiversityInfoList.clear();
        }
    }

    private File getFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }


   /* @Override
    public List<LeaderDiversityInfo> getLeaders() {
        return lsLDI;
    }

    @Override
    public List<CompanyDiversityInfo> getCompanies() {
        return lsCDI;
    }
*/
}
