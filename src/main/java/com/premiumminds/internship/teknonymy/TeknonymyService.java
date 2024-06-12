package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.Person;
import java.time.LocalDateTime;

public class TeknonymyService implements ITeknonymyService {

  /**
   * Method to get a Person's Teknonymy Name
   * 
   * @param person the Person object to get the teknonymy for
   * @return String which is the Teknonymy Name 
   */
  public String getTeknonymy(Person person) {
    // If the person has no children, return an empty string
    if (person.children() == null) {
      return "";
    }

    // Create a DescendantInfo object to store information about the oldest descendant
    DescendantInfo oldestDescendantInfo = new DescendantInfo();
    // Find the deepest (oldest) descendant
    findOldestDescendant(person, 0, oldestDescendantInfo);

    // Determine the relation (father/mother) based on the person's sex
    String relation = person.sex() == 'M' ? "father" : "mother";
    // If the depth is greater than 1, adjust the relation to include "grand" or "great-grand"
    if (oldestDescendantInfo.depth > 1) {
      relation = "grand" + relation;
      
      for (int i = 2; i < oldestDescendantInfo.depth; i++) {
        relation = "great-" + relation;
      }
    }

    // Return the complete teknonymy string
    return (relation + " of " + oldestDescendantInfo.name);
  }

  /**
   * Inner class to hold information about the deepest (oldest) descendant
   */
  private static class DescendantInfo {
    int depth; // The depth of the descendant in the family tree
    String name; // The name of the descendant
    LocalDateTime dateOfBirth; // The date of birth of the descendant

    /**
     * Constructor to initialize the depth
     */
    DescendantInfo() {
      this.depth = -1; // Initial depth is set to -1 to ensure any valid depth will be greater
    }

    /**
     * Method to update the DescendantInfo with new values
     * 
     * @param depth the depth of the descendant in the family tree
     * @param name the name of the descendant
     * @param dateOfBirth the date of birth of the descendant
     */
    void update(int depth, String name, LocalDateTime dateOfBirth) {
      this.depth = depth;
      this.name = name;
      this.dateOfBirth = dateOfBirth;
    }
  }

  /**
   * Method to find the oldest (deepest) descendant recursively in a DFS manner
   * 
   * @param person the current Person object in the recursion
   * @param currentDepth the current depth in the family tree
   * @param deepestOldest the DescendantInfo object to update with the deepest (oldest) descendant
   */
  private void findOldestDescendant(Person person, int currentDepth, DescendantInfo deepestOldest) {
    // If the person has no children, check if this person is the deepest or oldest
    if (person.children() == null) {
      if (currentDepth > deepestOldest.depth || 
          (currentDepth == deepestOldest.depth && (deepestOldest.dateOfBirth == null || person.dateOfBirth().isBefore(deepestOldest.dateOfBirth)))) {
        deepestOldest.update(currentDepth, person.name(), person.dateOfBirth());
      }
      return;
    }

    // Recursively check each child
    for (Person child : person.children()) {
      findOldestDescendant(child, currentDepth + 1, deepestOldest);
    }
  }
}

