package com.ontotext.refine.client.command.reconcile;

import static java.util.Collections.emptyList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * Holds the response from {@link GuessColumnTypeCommand}.
 *
 * @author Antoniy Kunchev
 */
public class GuessColumnTypeCommandResponse {

  private final String project;
  private final String column;
  private final List<ReconciliationType> types;
  private final String error;

  private GuessColumnTypeCommandResponse(
      String project, String column, List<ReconciliationType> types, String error) {
    this.project = project;
    this.column = column;
    this.error = error;
    this.types = types;

    this.types.sort(Comparator.comparingDouble(ReconciliationType::getScore).reversed());
  }

  /**
   * Creates a OK response containing the suggested types for the column, sorted by score, where the
   * type with the highest score is the first element in the collection.
   *
   * @param project which column type is guessed
   * @param column the name of the column which type should guessed
   * @param types that were guessed by the service
   * @return new instance of the response containing the types for the column
   */
  static GuessColumnTypeCommandResponse ok(
      String project, String column, List<ReconciliationType> types) {
    return new GuessColumnTypeCommandResponse(project, column, types, null);
  }

  /**
   * Creates a error response containing the message for the failure of the process.
   *
   * @param project which column type is guessed
   * @param column the name of the column which type should guessed
   * @param message from the failed process
   * @return new instance of the response containing the error message
   */
  static GuessColumnTypeCommandResponse error(String project, String column, String message) {
    return new GuessColumnTypeCommandResponse(project, column, emptyList(), message);
  }

  public String getProject() {
    return project;
  }

  public String getColumn() {
    return column;
  }

  public List<ReconciliationType> getTypes() {
    return types;
  }

  public String getError() {
    return error;
  }

  /**
   * Represents one type of the reconciliation types that the user may select.
   *
   * @author Antoniy Kunchev
   */
  public static class ReconciliationType {

    private String id;
    private String name;
    private int count;
    private double score;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public double getScore() {
      return score;
    }

    public void setScore(double score) {
      this.score = score;
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append("ReconciliationTypes [id=").append(id)
          .append(", name=").append(name)
          .append(", count=").append(count)
          .append(", score=").append(score)
          .append("]").toString();
    }

    @Override
    public int hashCode() {
      return Objects.hash(count, id,name, score);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      ReconciliationType other = (ReconciliationType) obj;
      return count == other.count
          && Objects.equals(id, other.id)
          && Objects.equals(name, other.name)
          && Double.doubleToLongBits(score) == Double.doubleToLongBits(other.score);
    }
  }
}
