package org.census.commons.dto.personnel.employees;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for EmployeeDto domain object.
 * Created by vinnypuhh on 10.08.2016.
 */

public class EmployeeDtoTest {

    @Test
    public void testFullNameConstructor() {
        // # case 1
        EmployeeDto employee1 = new EmployeeDto(0, "Иванов    Иван  Иванович");
        assertTrue("employee full name #1", "Иванов".equals(employee1.getFamily()));
        assertTrue("employee full name #2", "Иван".equals(employee1.getName()));
        assertTrue("employee full name #3", "Иванович".equals(employee1.getPatronymic()));

        // # case 2
        EmployeeDto employee2 = new EmployeeDto(0, "   Саласар    Хуан Карлос      Мартинес");
        assertTrue("employee full name #4", "Саласар".equals(employee2.getFamily()));
        assertTrue("employee full name #5", "Хуан".equals(employee2.getName()));
        assertTrue("employee full name #6", "Карлос Мартинес".equals(employee2.getPatronymic()));

        // todo: add tests cases with: 1. just name 2. name + family 3. other...
    }

}
