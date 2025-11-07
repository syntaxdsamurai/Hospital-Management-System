package com.hospital.dao;

import com.hospital.model.Bill;
import com.hospital.model.Bills;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillDAO {

    private static final String FILE_PATH = "data/bills.xml";
    private File file = new File(FILE_PATH);

    public List<Bill> getAllBills() {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Bills.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Bills billsWrapper = (Bills) unmarshaller.unmarshal(file);

            // --- THIS IS THE CORRECTED LINE ---
            return billsWrapper.getBillList() != null ? billsWrapper.getBillList() : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveAllBills(List<Bill> billList) {
        try {
            Bills billsWrapper = new Bills();
            billsWrapper.setBillList(billList);

            JAXBContext context = JAXBContext.newInstance(Bills.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            marshaller.marshal(billsWrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBill(Bill bill) {
        List<Bill> bills = getAllBills();
        bills.add(bill);
        saveAllBills(bills);
    }

    public Bill getBillByAppointmentId(String appointmentId) {
        return getAllBills().stream()
                .filter(bill -> bill.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }

    public List<Bill> getBillsByPatientId(String patientId) {
        return getAllBills().stream()
                .filter(bill -> bill.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public void updateBillStatus(String billId, String newStatus) {
        List<Bill> bills = getAllBills();
        bills.stream()
                .filter(bill -> bill.getBillId().equals(billId))
                .findFirst()
                .ifPresent(bill -> bill.setStatus(newStatus));
        saveAllBills(bills);
    }
}