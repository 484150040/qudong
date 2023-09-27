package com.isycores.driver.utils;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import static com.isycores.driver.utils.HttpClientUtil.json2map;

public class UploadUtils {


    @SneakyThrows
    public static void main(String[] args) {
        String json = "{\n" +
                "    \"Status\": \"ok\",\n" +
                "    \"Message\": \"\",\n" +
                "    \"Data\": [\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_001\",\n" +
                "            \"device_name\": \"二楼电池间9#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_002\",\n" +
                "            \"device_name\": \"二楼电池间10#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_003\",\n" +
                "            \"device_name\": \"二楼电池间11#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_004\",\n" +
                "            \"device_name\": \"二楼电池间12#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_005\",\n" +
                "            \"device_name\": \"二楼电池间13#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_006\",\n" +
                "            \"device_name\": \"二楼电池间14#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_007\",\n" +
                "            \"device_name\": \"负一楼电池间15#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_D3S_008\",\n" +
                "            \"device_name\": \"负一楼电池间16#UPS电池组\",\n" +
                "            \"category_name\": \"电池\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_001\",\n" +
                "            \"device_name\": \"二楼西南配电间A1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_002\",\n" +
                "            \"device_name\": \"二楼西南配电间A4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_003\",\n" +
                "            \"device_name\": \"二楼西南配电间A7柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_004\",\n" +
                "            \"device_name\": \"二楼西南配电间A8柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_005\",\n" +
                "            \"device_name\": \"二楼西南配电间A11柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_006\",\n" +
                "            \"device_name\": \"二楼西南配电间A14柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_007\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_008\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D2柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_009\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_010\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D5柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_011\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_012\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_013\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_014\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_015\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_016\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜A13变电所电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_017\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_018\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_019\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_020\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D3柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_021\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_022\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_023\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_024\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_025\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_026\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_027\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜A13变电所电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_028\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_029\",\n" +
                "            \"device_name\": \"二楼A13变电所A13-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_030\",\n" +
                "            \"device_name\": \"二楼北配电间A1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_031\",\n" +
                "            \"device_name\": \"二楼北配电间A4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_032\",\n" +
                "            \"device_name\": \"二楼北配电间A14柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_033\",\n" +
                "            \"device_name\": \"二楼北配电间A8柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_034\",\n" +
                "            \"device_name\": \"二楼北配电间A11柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_035\",\n" +
                "            \"device_name\": \"二楼北配电间A7柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_036\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_037\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D2柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_038\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_039\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D5柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_040\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_041\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_042\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_043\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_044\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_045\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜A12变电所电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_046\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_047\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_048\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_049\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D3柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_050\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_051\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_052\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_053\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_054\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_055\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_056\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜A12变电所电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_057\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_058\",\n" +
                "            \"device_name\": \"一楼A12变电所A12-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_059\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_060\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D2柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_061\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D3柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_062\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D4柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_063\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D5柜A10楼冷冻机房电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_064\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D5柜A10楼UPS15电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_065\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_066\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_067\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_068\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜A10楼精密空调5电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_069\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜A10楼精密空调4电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_070\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜A10楼精密空调3电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_071\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜A10楼精密空调2电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_072\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜A10楼精密空调1电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_073\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_074\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_075\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_076\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_077\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_078\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜A10变电所总高配电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_079\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜A10变电所电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_080\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜A10楼精密空调7电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_081\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜A10楼精密空调6电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_082\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-1D7柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_083\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D1柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_084\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D2柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_085\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D3柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_086\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D4柜A10楼冷冻机房电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_087\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D4柜A10楼UPS16电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_088\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_089\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_090\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_091\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜A10楼精密空调5电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_092\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜A10楼精密空调4电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_093\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜A10楼精密空调3电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_094\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜A10楼精密空调2电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_095\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜A10楼精密空调1电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_096\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_097\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_098\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_099\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_100\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_101\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜A10变电所总高配电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_102\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜A10变电所电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_103\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜精密空调7电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_104\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜精密空调6电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_105\",\n" +
                "            \"device_name\": \"负一楼A11变电所A11-2D6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E43_106\",\n" +
                "            \"device_name\": \"负一楼配电间1NL2a柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_001\",\n" +
                "            \"device_name\": \"二楼西南配电间A2柜UPS9输入电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_002\",\n" +
                "            \"device_name\": \"二楼西南配电间A2柜UPS10输入电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_003\",\n" +
                "            \"device_name\": \"二楼西南配电间A2柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_004\",\n" +
                "            \"device_name\": \"二楼西南配电间A3柜UPS11输入电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_005\",\n" +
                "            \"device_name\": \"二楼西南配电间A3柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_006\",\n" +
                "            \"device_name\": \"二楼西南配电间A5柜UPS9输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_007\",\n" +
                "            \"device_name\": \"二楼西南配电间A5柜UPS10输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_008\",\n" +
                "            \"device_name\": \"二楼西南配电间A5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_009\",\n" +
                "            \"device_name\": \"二楼西南配电间A6柜UPS11输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_010\",\n" +
                "            \"device_name\": \"二楼西南配电间A6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_011\",\n" +
                "            \"device_name\": \"二楼西南配电间A9柜UPS12输入电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_012\",\n" +
                "            \"device_name\": \"二楼西南配电间A9柜UPS13输入电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_013\",\n" +
                "            \"device_name\": \"二楼西南配电间A9柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_014\",\n" +
                "            \"device_name\": \"二楼西南配电间A10柜UPS14输入电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_015\",\n" +
                "            \"device_name\": \"二楼西南配电间A10柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_016\",\n" +
                "            \"device_name\": \"二楼西南配电间A12柜UPS12输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_017\",\n" +
                "            \"device_name\": \"二楼西南配电间A12柜UPS13输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_018\",\n" +
                "            \"device_name\": \"二楼西南配电间A12柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_019\",\n" +
                "            \"device_name\": \"二楼西南配电间A13柜UPS14输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_020\",\n" +
                "            \"device_name\": \"二楼西南配电间A12柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_021\",\n" +
                "            \"device_name\": \"二楼北配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_022\",\n" +
                "            \"device_name\": \"二楼北配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_023\",\n" +
                "            \"device_name\": \"二楼北配电间A2柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_024\",\n" +
                "            \"device_name\": \"二楼北配电间A3柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_025\",\n" +
                "            \"device_name\": \"二楼北配电间A3柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_026\",\n" +
                "            \"device_name\": \"二楼北配电间A3柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_027\",\n" +
                "            \"device_name\": \"二楼北配电间A5柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_028\",\n" +
                "            \"device_name\": \"二楼北配电间A5柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_029\",\n" +
                "            \"device_name\": \"二楼北配电间A5柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_030\",\n" +
                "            \"device_name\": \"二楼北配电间A6柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_031\",\n" +
                "            \"device_name\": \"二楼北配电间A6柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_032\",\n" +
                "            \"device_name\": \"二楼北配电间A6柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_033\",\n" +
                "            \"device_name\": \"二楼北配电间A9柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_034\",\n" +
                "            \"device_name\": \"二楼北配电间A9柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_035\",\n" +
                "            \"device_name\": \"二楼北配电间A9柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_036\",\n" +
                "            \"device_name\": \"二楼北配电间A10柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_037\",\n" +
                "            \"device_name\": \"二楼北配电间A10柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_038\",\n" +
                "            \"device_name\": \"二楼北配电间A10柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_039\",\n" +
                "            \"device_name\": \"二楼北配电间A12柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_040\",\n" +
                "            \"device_name\": \"二楼北配电间A12柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_041\",\n" +
                "            \"device_name\": \"二楼北配电间A12柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_042\",\n" +
                "            \"device_name\": \"二楼北配电间A13柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_043\",\n" +
                "            \"device_name\": \"二楼北配电间A13柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_044\",\n" +
                "            \"device_name\": \"二楼北配电间A13柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"二楼北配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_045\",\n" +
                "            \"device_name\": \"负一楼配电间1NL2d柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_046\",\n" +
                "            \"device_name\": \"负一楼配电间1NL2c柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_047\",\n" +
                "            \"device_name\": \"负一楼配电间1NL2b柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_048\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜(A10)3Pa1电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_049\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜(A10)3Pa2电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_050\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_051\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_052\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_053\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_054\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_055\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_056\",\n" +
                "            \"device_name\": \"负一楼配电间A4柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_057\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜UPS16输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_058\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜冷冻水循环泵电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_059\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜动环监控电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_060\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜运营商机房电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_061\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_062\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_063\",\n" +
                "            \"device_name\": \"负一楼配电间A3柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_064\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜（A10）3Pa1电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_065\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜（A10）3Pa2电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_066\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_067\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_068\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_069\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_070\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_071\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_072\",\n" +
                "            \"device_name\": \"负一楼配电间A2柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_073\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜UPS15输出电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_074\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜冷冻水循环泵电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_075\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜动环监控电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_076\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜运营商机房电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_077\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_078\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_079\",\n" +
                "            \"device_name\": \"负一楼配电间A1柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_080\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1f柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_081\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1e柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_082\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1d柜电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_083\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1c柜预留电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_084\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1c柜备用电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_085\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1b柜（A10）1AC1电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_E4P_086\",\n" +
                "            \"device_name\": \"负一楼配电间1NL1b柜（A10）1AC2电量仪\",\n" +
                "            \"category_name\": \"电量仪\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_HA5_001\",\n" +
                "            \"device_name\": \"二楼西南机房1#氢气传感器\",\n" +
                "            \"category_name\": \"传感器\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_HA5_002\",\n" +
                "            \"device_name\": \"二楼西南机房2#氢气传感器\",\n" +
                "            \"category_name\": \"传感器\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_HA5_003\",\n" +
                "            \"device_name\": \"二楼西南机房3#氢气传感器\",\n" +
                "            \"category_name\": \"传感器\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_HA5_004\",\n" +
                "            \"device_name\": \"二楼西南机房4#氢气传感器\",\n" +
                "            \"category_name\": \"传感器\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_HA5_005\",\n" +
                "            \"device_name\": \"二楼西南机房5#氢气传感器\",\n" +
                "            \"category_name\": \"传感器\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_HA5_006\",\n" +
                "            \"device_name\": \"负一楼电池间氢气传感器\",\n" +
                "            \"category_name\": \"传感器\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_001\",\n" +
                "            \"device_name\": \"三楼中心机房1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_002\",\n" +
                "            \"device_name\": \"三楼中心机房2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_003\",\n" +
                "            \"device_name\": \"三楼中心机房3#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_004\",\n" +
                "            \"device_name\": \"三楼中心机房4#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_005\",\n" +
                "            \"device_name\": \"三楼中心机房5#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_006\",\n" +
                "            \"device_name\": \"三楼中心机房6#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_007\",\n" +
                "            \"device_name\": \"三楼中心机房7#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_008\",\n" +
                "            \"device_name\": \"三楼中心机房8#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_009\",\n" +
                "            \"device_name\": \"三楼中心机房旁小机房温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_010\",\n" +
                "            \"device_name\": \"二楼电池间1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_011\",\n" +
                "            \"device_name\": \"二楼电池间2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_012\",\n" +
                "            \"device_name\": \"二楼电池间3#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_013\",\n" +
                "            \"device_name\": \"二楼电池间4#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_014\",\n" +
                "            \"device_name\": \"二楼电池间5#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_015\",\n" +
                "            \"device_name\": \"二楼西南配电间1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_016\",\n" +
                "            \"device_name\": \"二楼西南配电间2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_017\",\n" +
                "            \"device_name\": \"二楼西南配电间3#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_018\",\n" +
                "            \"device_name\": \"二楼西南配电间4#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_019\",\n" +
                "            \"device_name\": \"二楼A13变电所1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_020\",\n" +
                "            \"device_name\": \"二楼A13变电所2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_021\",\n" +
                "            \"device_name\": \"二楼A13变电所3#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_022\",\n" +
                "            \"device_name\": \"二楼北机房1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_023\",\n" +
                "            \"device_name\": \"二楼北机房2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_024\",\n" +
                "            \"device_name\": \"二楼北机房3#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_025\",\n" +
                "            \"device_name\": \"二楼北机房4#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_026\",\n" +
                "            \"device_name\": \"一楼A12变电所1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_027\",\n" +
                "            \"device_name\": \"一楼A12变电所2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_028\",\n" +
                "            \"device_name\": \"一楼A12变电所3#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_029\",\n" +
                "            \"device_name\": \"负一楼电池间温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_030\",\n" +
                "            \"device_name\": \"负一楼配电间1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_031\",\n" +
                "            \"device_name\": \"负一楼配电间2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_032\",\n" +
                "            \"device_name\": \"负一楼A11变电所1#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_K25_033\",\n" +
                "            \"device_name\": \"负一楼A11变电所2#温湿度\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_001\",\n" +
                "            \"device_name\": \"二楼北配电间1#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_002\",\n" +
                "            \"device_name\": \"二楼北配电间2#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_003\",\n" +
                "            \"device_name\": \"二楼北配电间3#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_004\",\n" +
                "            \"device_name\": \"二楼北配电间4#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_005\",\n" +
                "            \"device_name\": \"二楼西南配电间1#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_006\",\n" +
                "            \"device_name\": \"二楼西南配电间2#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_007\",\n" +
                "            \"device_name\": \"二楼西南配电间3#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KA5_008\",\n" +
                "            \"device_name\": \"二楼西南配电间4#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_001\",\n" +
                "            \"device_name\": \"三楼中心机房西侧冷通道1#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_002\",\n" +
                "            \"device_name\": \"三楼中心机房西侧冷通道2#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_003\",\n" +
                "            \"device_name\": \"三楼中心机房西侧冷通道3#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_004\",\n" +
                "            \"device_name\": \"三楼中心机房西侧冷通道4#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_005\",\n" +
                "            \"device_name\": \"三楼中心机房东侧冷通道1#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_006\",\n" +
                "            \"device_name\": \"三楼中心机房东侧冷通道2#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_007\",\n" +
                "            \"device_name\": \"三楼中心机房东侧冷通道3#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_KT3_008\",\n" +
                "            \"device_name\": \"三楼中心机房东侧冷通道4#空调\",\n" +
                "            \"category_name\": \"空调\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_L25_001\",\n" +
                "            \"device_name\": \"二楼西南配电间1#、2#空调下漏水\",\n" +
                "            \"category_name\": \"漏水检测\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_L25_002\",\n" +
                "            \"device_name\": \"二楼西南配电间3#空调下漏水\",\n" +
                "            \"category_name\": \"漏水检测\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_L25_003\",\n" +
                "            \"device_name\": \"二楼西南配电间4#空调下漏水\",\n" +
                "            \"category_name\": \"漏水检测\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_L25_004\",\n" +
                "            \"device_name\": \"二楼北配电间1#、2#空调下漏水\",\n" +
                "            \"category_name\": \"漏水检测\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_L25_005\",\n" +
                "            \"device_name\": \"二楼北配电间3#、4#空调下漏水\",\n" +
                "            \"category_name\": \"漏水检测\",\n" +
                "            \"area_name\": \"二楼西南配电间\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_001\",\n" +
                "            \"device_name\": \"二楼西南配电间9#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_002\",\n" +
                "            \"device_name\": \"二楼西南配电间10#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_003\",\n" +
                "            \"device_name\": \"二楼西南配电间11#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_004\",\n" +
                "            \"device_name\": \"二楼西南配电间12#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_005\",\n" +
                "            \"device_name\": \"二楼西南配电间13#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_006\",\n" +
                "            \"device_name\": \"二楼西南配电间14#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"二楼A13变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_007\",\n" +
                "            \"device_name\": \"负一楼配电间15#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"D_U78_008\",\n" +
                "            \"device_name\": \"负一楼配电间16#UPS \",\n" +
                "            \"category_name\": \"UPS\",\n" +
                "            \"area_name\": \"负一楼A11变电所\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_HAQ_001\",\n" +
                "            \"device_name\": \"东侧冷通道多功能传感器1\",\n" +
                "            \"category_name\": \"多功能传感器\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_HAQ_002\",\n" +
                "            \"device_name\": \"西侧冷通道多功能传感器1\",\n" +
                "            \"category_name\": \"多功能传感器\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_HAR_002\",\n" +
                "            \"device_name\": \"西侧冷通道多功能传感器2\",\n" +
                "            \"category_name\": \"其它\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_HAS_001\",\n" +
                "            \"device_name\": \"东侧冷通道多功能传感器2\",\n" +
                "            \"category_name\": \"温湿度\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_L2F_001\",\n" +
                "            \"device_name\": \"东侧冷通道ECC800\",\n" +
                "            \"category_name\": \"ECC800\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_L2F_002\",\n" +
                "            \"device_name\": \"西侧冷通道ECC800\",\n" +
                "            \"category_name\": \"ECC800\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4B_002\",\n" +
                "            \"device_name\": \"西侧冷通道精密配电柜\",\n" +
                "            \"category_name\": \"配电柜\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4C_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能ETH插座2\",\n" +
                "            \"category_name\": \"配电\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4C_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能ETH插座1\",\n" +
                "            \"category_name\": \"配电\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4D_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能ETH插座3\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4D_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能ETH插座2\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4E_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能ETH插座4\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4E_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能ETH插座3\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4F_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能ETH插座5\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4F_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能ETH插座4\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4G_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能ETH插座6\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4G_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能ETH插座5\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4H_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能ETH插座6\",\n" +
                "            \"category_name\": \"智能ETH插座\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4J_001\",\n" +
                "            \"device_name\": \"东侧冷通道电源模块1\",\n" +
                "            \"category_name\": \"电源模块\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4J_002\",\n" +
                "            \"device_name\": \"西侧冷通道电源模块1\",\n" +
                "            \"category_name\": \"电源模块\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4K_001\",\n" +
                "            \"device_name\": \"东侧冷通道电源模块2\",\n" +
                "            \"category_name\": \"电源模块\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4K_002\",\n" +
                "            \"device_name\": \"西侧冷通道电源模块2\",\n" +
                "            \"category_name\": \"电源模块\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4M_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能微模块执行器1\",\n" +
                "            \"category_name\": \"智能微模块执行器\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4N_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能微模块执行器2\",\n" +
                "            \"category_name\": \"智能微模块执行器\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4N_002\",\n" +
                "            \"device_name\": \"西侧冷通道智能微模块执行器2\",\n" +
                "            \"category_name\": \"智能微模块执行器\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4Q_001\",\n" +
                "            \"device_name\": \"东侧冷通道精密配电柜\",\n" +
                "            \"category_name\": \"配电柜\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4R_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能ETH插座1\",\n" +
                "            \"category_name\": \"配电\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"S_P4S_001\",\n" +
                "            \"device_name\": \"东侧冷通道智能微模块执行器1\",\n" +
                "            \"category_name\": \"配电\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"device_id\": \"T_DAHUAADT_001\",\n" +
                "            \"device_name\": \"1#大华报警主机\",\n" +
                "            \"category_name\": \"报警主机\",\n" +
                "            \"area_name\": \"三楼中心机房\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Map<String,Object> objectMap = json2map(json);
        List<Map<String,String>> mapList = (List<Map<String, String>>) objectMap.get("Data");
// 创建工作簿对象

        Workbook workbook = new XSSFWorkbook();

// 创建工作表对象

        Sheet sheet = workbook.createSheet("动环数据");
        for (int i = 0; i <=mapList.size() ; i++) {
            // 创建行和单元格对象
            Row row = sheet.createRow(i);
            if (i==0){
                Cell cell0 = row.createCell(0);
                cell0.setCellValue("设备id");
                Cell cell1 = row.createCell(1);
                cell1.setCellValue("设备名称");
                Cell cell2 = row.createCell(2);
                cell2.setCellValue("类别名称");
                Cell cell3 = row.createCell(3);
                cell3.setCellValue("位置");
                continue;
            }
            int cent = 0;
            for (String s : mapList.get(i-1).keySet()) {
                // 创建行和单元格对象
                Cell cell = row.createCell(cent);
                cell.setCellValue(mapList.get(i-1).get(s));
                cent++;
            }

        }

// 向单元格中写入数据

// 导出Excel文件

        FileOutputStream outputStream = new FileOutputStream("动环数据.xlsx");

        workbook.write(outputStream);

        workbook.close();

    }

}
