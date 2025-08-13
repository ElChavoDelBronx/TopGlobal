package com.topglobal.dailyorder.models.objects;

import java.time.LocalDate;

public class Assignment {
    private int idAssignment;
    private int idEmployee;
    private int idTable;
    private String shift;
    private LocalDate assignDate;

    public Assignment(int idAssignment, int idEmployee, int idTable, String shift, LocalDate assignDate) {
        this.idAssignment = idAssignment;
        this.idEmployee = idEmployee;
        this.idTable = idTable;
        this.shift = shift;
        this.assignDate = assignDate;
    }

    // Getters y setters
    public int getIdAssignment() { return idAssignment; }
    public int getIdEmployee() { return idEmployee; }
    public int getIdTable() { return idTable; }
    public String getShift() { return shift; }
    public LocalDate getAssignDate() { return assignDate; }

    public void setIdAssignment(int idAssignment) { this.idAssignment = idAssignment; }
    public void setIdEmployee(int idEmployee) { this.idEmployee = idEmployee; }
    public void setIdTable(int idTable) { this.idTable = idTable; }
    public void setShift(String shift) { this.shift = shift; }
    public void setAssignDate(LocalDate assignDate) { this.assignDate = assignDate; }
}

