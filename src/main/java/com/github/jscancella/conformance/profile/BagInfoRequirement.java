package com.github.jscancella.conformance.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to define elements in a bag-info.txt file used by a
 * {@link BagitProfile}. Due to specification in version 1.1.0 all entries are
 * required=false and repeatable=true by default. See Bag-Info
 * {@link https://github.com/bagit-profiles/bagit-profiles-specification/tree/1.1.0#implementation-details}
 */
public class BagInfoRequirement {

  private boolean required; // false by default;
  private List<String> acceptableValues = new ArrayList<>();
  private boolean repeatable = true;

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof BagInfoRequirement)) {
      return false;
    }
    final BagInfoRequirement castOther = (BagInfoRequirement) other;
    return Objects.equals(required, castOther.required)
            && Objects.equals(acceptableValues, castOther.acceptableValues)
            && Objects.equals(repeatable, castOther.repeatable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(required, acceptableValues, repeatable);
  }

  /**
   * Constructs a new BagInfoRequirement setting {@link #repeatable} to true
   * (default) and {@link #required} to false (default).
   *
   * @param required Indicates whether or not the tag is required.
   * @param acceptableValues List of acceptable values.
   */
  public BagInfoRequirement() {
    //intentionally left empty
  }

  /**
   * Constructs a new BagInfoRequirement setting {@link #repeatable} to true
   * (default).
   *
   * @param required Indicates whether or not the tag is required.
   * @param acceptableValues List of acceptable values.
   */
  public BagInfoRequirement(final boolean required, final List<String> acceptableValues) {
    this(required, acceptableValues, true);
  }

  /**
   * Constructs a new BagInfoRequirement.
   *
   * @param required Indicates whether or not the tag is required.
   * @param acceptableValues List of acceptable values.
   * @param repeatable Indicates whether or not the tag is repeatable.
   */
  public BagInfoRequirement(final boolean required, final List<String> acceptableValues, final boolean repeatable) {
    this.required = required;
    this.acceptableValues = acceptableValues;
    this.repeatable = repeatable;
  }

  @Override
  public String toString() {
    return "[required=" + required + ", acceptableValues=" + acceptableValues + ", repeatable=" + repeatable + "]";
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(final boolean required) {
    this.required = required;
  }

  public List<String> getAcceptableValues() {
    return acceptableValues;
  }

  public void setAcceptableValues(final List<String> acceptableValues) {
    this.acceptableValues = acceptableValues;
  }

  public boolean isRepeatable() {
    return repeatable;
  }

  public void setRepeatable(final boolean repeatable) {
    this.repeatable = repeatable;
  }
}
