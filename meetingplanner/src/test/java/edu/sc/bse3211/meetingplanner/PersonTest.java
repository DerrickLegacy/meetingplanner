package edu.sc.bse3211.meetingplanner;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PersonTest {
    private Person person;
    @Before
    public void setUp() {
        person = new Person("Alice");
    }

    @Test
    public void testDefaultConstructor() {
        Person p = new Person();
        assertEquals("", p.getName());
    }

	
}
