package com.company.univercity_bot.controller;

import com.company.univercity_bot.container.CompanantContainer;
import com.company.univercity_bot.model.Student;
import com.company.univercity_bot.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileController {
    @Autowired
    private StudentRepository studentRepository;

    public  File registrationAbiturinet () {

        File file = new File(CompanantContainer.PATH + "file/reg/AbiturientlarRoyxati.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();


        try (FileOutputStream out = new FileOutputStream(file)) {

            XSSFSheet sheet = workbook.createSheet("Abiturientlar_ro'yxati");
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Jinsi");
            row0.createCell(1).setCellValue("F.I.Sh");
            row0.createCell(2).setCellValue("Tug'ilgan sanasi");
            row0.createCell(3).setCellValue("Passport seria va raqami");
            row0.createCell(4).setCellValue("Passport berilgan sana");
            row0.createCell(5).setCellValue("Yashash manzili");
            row0.createCell(6).setCellValue("Telefon raqami");
            row0.createCell(7).setCellValue("Ota/ona telefon raqami");
            row0.createCell(8).setCellValue("Bitirgan o'rta/oliy ta'lim maskani");
            row0.createCell(9).setCellValue("Tanlagan Hudud");
            row0.createCell(10).setCellValue("Ta'lim darajasi");
            row0.createCell(11).setCellValue("Ta'lim yo'nalishi");
            row0.createCell(12).setCellValue("Ta'lim shakli");

            List<Student> studentList = studentRepository.findAll();

            for (int i = 0; i < studentList.size(); i++) {

                List<Student> students = studentRepository.findAll();

                XSSFRow row = sheet.createRow(i + 1);


                row.createCell(0).setCellValue(students.get(i).getGender());
                row.createCell(1).setCellValue(students.get(i).getName());
                row.createCell(2).setCellValue(students.get(i).getBirthday());
                row.createCell(3).setCellValue(students.get(i).getPassportNumber());
                row.createCell(4).setCellValue(students.get(i).getTakePassportDay());
                row.createCell(5).setCellValue(students.get(i).getManzil());
                row.createCell(6).setCellValue(students.get(i).getPhone());
                row.createCell(7).setCellValue(students.get(i).getFathPhone());
                row.createCell(8).setCellValue(students.get(i).getSchoolType());
                row.createCell(9).setCellValue(students.get(i).getEdu_location());
                row.createCell(10).setCellValue(students.get(i).getEducationDegree());
                row.createCell(11).setCellValue(students.get(i).getEducationDirectory().getNameUZ());
                row.createCell(12).setCellValue(students.get(i).getEducationType());




            }
            for (int i = 0; i < 19; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            workbook.close();
            return file;


        } catch (
                IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return null;
}
}

