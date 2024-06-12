package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.TeknonymyService;
import com.premiumminds.internship.teknonymy.Person;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class TeknonymyServiceTest {

  /**
   * The corresponding implementations to test.
   *
   * If you want, you can make others :)
   *
   */
  public TeknonymyServiceTest() {
  };

  @Test
  public void PersonNoChildrenTest() {
    Person person = new Person("John",'M',null, LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "";
    assertEquals(result, expected);
  }

  @Test
  public void PersonOneChildTest() {
    Person person = new Person(
        "John",
        'M',
        new Person[]{ new Person("Holy",'F', null, LocalDateTime.of(1046, 1, 1, 0, 0)) },
        LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "father of Holy";
    assertEquals(result, expected);
  }

  @Test
  public void PersonMultipleChildrenTest() {
    Person person = new Person(
        "John",
        'M',
        new Person[]{
            new Person("Holy", 'F', null, LocalDateTime.of(1046, 1, 1, 0, 0)),
            new Person("Alex", 'M', null, LocalDateTime.of(1046, 1, 1, 0, 1)),
            new Person("Poly", 'F', null, LocalDateTime.of(1047, 1, 1, 0, 2))
        },
        LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "father of Holy";
    assertEquals(result, expected);
  }

  @Test
  public void PersonNoNameTest() {
    Person person = new Person(
        "",
        'M',
        new Person[]{ new Person("Holy", 'F', null, LocalDateTime.of(1046, 1, 1, 0, 0)) },
        LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "father of Holy";
    assertEquals(result, expected);
  }

  @Test
  public void ThreeGenerationTest() {
    Person child = new Person("Alice", 'F', null, LocalDateTime.of(1100, 1, 1, 0, 0));
    Person parent = new Person("Bob", 'M', new Person[]{child}, LocalDateTime.of(1080, 1, 1, 0, 0));
    Person grandparent = new Person("John", 'M', new Person[]{parent}, LocalDateTime.of(1060, 1, 1, 0, 0));
    
    String result = new TeknonymyService().getTeknonymy(parent);
    String expected = "father of Alice";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(grandparent);
    expected = "grandfather of Alice";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(child);
    expected = "";
    assertEquals(result, expected);
  }

  @Test
  public void FourGenerationTest() {
    Person child1 = new Person("Alice", 'F', null, LocalDateTime.of(1100, 1, 1, 0, 0));
    Person child2 = new Person("Bob", 'M', null, LocalDateTime.of(1100, 1, 1, 0, 1));
    Person parent = new Person("Bob", 'M', new Person[]{child1, child2}, LocalDateTime.of(1080, 1, 1, 0, 0));
    Person grandparent = new Person("John", 'M', new Person[]{parent}, LocalDateTime.of(1060, 1, 1, 0, 0));
    Person greatGrandparent = new Person("Jack", 'M', new Person[]{grandparent}, LocalDateTime.of(1040, 1, 1, 0, 0));
    
    String result = new TeknonymyService().getTeknonymy(parent);
    String expected = "father of Alice";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(grandparent);
    expected = "grandfather of Alice";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(greatGrandparent);
    expected = "great-grandfather of Alice";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(child1);
    expected = "";
    assertEquals(result, expected);
  }

  @Test
  public void ComplicatedGenerationTest() {
    Person child1 = new Person("Alice", 'F', null, LocalDateTime.of(1100, 1, 1, 0, 2));
    Person child2 = new Person("Bob", 'M', null, LocalDateTime.of(1100, 1, 1, 0, 1));
    Person parent1 = new Person("Bob", 'M', new Person[]{child1}, LocalDateTime.of(1080, 1, 1, 0, 0));
    Person parent2 = new Person("Holy", 'F', new Person[]{child2}, LocalDateTime.of(1080, 1, 1, 0, 0));
    Person grandparent = new Person("John", 'M', new Person[]{parent1, parent2}, LocalDateTime.of(1060, 1, 1, 0, 0));
    Person greatGrandparent = new Person("Jack", 'M', new Person[]{grandparent}, LocalDateTime.of(1040, 1, 1, 0, 0));
    
    String result = new TeknonymyService().getTeknonymy(parent1);
    String expected = "father of Alice";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(parent2);
    expected = "mother of Bob";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(grandparent);
    expected = "grandfather of Bob";
    assertEquals(result, expected);

    result = new TeknonymyService().getTeknonymy(greatGrandparent);
    expected = "great-grandfather of Bob";
    assertEquals(result, expected);
  }
}